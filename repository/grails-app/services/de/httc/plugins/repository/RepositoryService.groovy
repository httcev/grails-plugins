package de.httc.plugins.repository

import grails.transaction.Transactional
import java.io.File
import java.util.zip.ZipInputStream
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.tika.mime.MediaType
import org.apache.tika.metadata.Metadata
import org.apache.tika.config.TikaConfig
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.html.BoilerpipeContentHandler
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.sax.WriteOutContentHandler
import org.xml.sax.ContentHandler

class RepositoryService {
	def grailsLinkGenerator
	def repoDir
    static transactional = false

    @Transactional(readOnly = true)
    def readAsset(encodedId) {
        /*
    	def id = hashIds.decode(encodedId)
    	if (id) {
	    	id = id[0]
	    	return Asset.read(id)
    	}
        */
        Asset.read(encodedId)
    }

    def createEncodedLink(asset, file=null) {
        def mapping = "viewAsset"
        def params = [:]
    	if (file) {
            mapping = "viewAssetFile"
            params.file = file
    	}
//        return grailsLinkGenerator.link(mapping:mapping, id:hashIds.encode(asset.id), params:params, absolute:true)
	    return grailsLinkGenerator.link(mapping:mapping, id:asset.id, params:params, absolute:true)
    }

    def getOrCreateRepositoryFile(asset) {
    	def file = getRepositoryFile(asset)
    	if (!file.exists()) {
            if (!asset.content) {
                return null
            }
            def anchor = asset.props?."${Asset.PROP_ANCHOR}"
    		if (asset.mimeType == "application/zip" && anchor) {
				ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(asset.content.data)))
				ZipUtil.unzip(zin, file)
    		}
    		else {
				// non-zip files
				def input = new ByteArrayInputStream(asset.content.data)
				def output = new FileOutputStream(file)
				try {
					IOUtils.copy(input, output)
				}
				finally {
					input.close()
					output.close()
				}
			}
		}
		return file
    }

    def getRepositoryFile(asset) {
        if (asset?.id) {
        	return new File(repoDir, asset.id as String)
        }
    }

    def deleteRepositoryFile(asset) {
    	def file = getRepositoryFile(asset)
    	if (file?.exists()) {
    		log.debug "deleting repository file " + file.getName()
    		if (file.isDirectory()) {
    			FileUtils.deleteDirectory(file)
    		}
    		else {
    			file.delete()
    		}
    	}
    }

    def enrichAsset(assetOrCommand, uploadFile = null) {
        def detector = TikaConfig.getDefaultConfig().getDetector();
        Metadata metadata = new Metadata()
        def inputStream
        def externalUrl = assetOrCommand.props?."${Asset.PROP_EXTERNAL_URL}"

        if (assetOrCommand.content || externalUrl) {
            try {
                if (assetOrCommand.content) {
                    // remove externalUrl since we're now expecting a local asset
                    assetOrCommand.props[Asset.PROP_EXTERNAL_URL] = null
                    if (uploadFile) {
                        def filename = uploadFile.getOriginalFilename()
                        assetOrCommand.props."${Asset.PROP_FILENAME}" = filename
                        if (!assetOrCommand.name) {
                            assetOrCommand.name = filename?.replaceAll("\\..*", "")
                        }
                    }

                    def data = (assetOrCommand.content instanceof byte[]) ? assetOrCommand.content : assetOrCommand.content.data
                    inputStream = new ByteArrayInputStream(data)
                    metadata.add(Metadata.RESOURCE_NAME_KEY, assetOrCommand.props."${Asset.PROP_FILENAME}")
                }
                else {
                    inputStream = new BufferedInputStream(new URL(externalUrl).openStream())
                }

                MediaType mediaType = detector.detect(inputStream, metadata)
                assetOrCommand.mimeType = mediaType.toString()

                def titleAndText = extractText(inputStream, assetOrCommand.mimeType)
                if (titleAndText.title && !assetOrCommand.name) {
                    assetOrCommand.name = titleAndText.title
                }
                if (titleAndText.text) {
                    assetOrCommand.indexText = titleAndText.text
                }
            }
            finally {
                if (inputStream) {
                    inputStream.close()
                }
            }
            if (!assetOrCommand.mimeType) {
                assetOrCommand.mimeType = "application/octet-stream"
            }
        }
    }

    protected Object extractText(InputStream inputStream, String mimeType) {
        StringWriter writer = new StringWriter();
        ContentHandler handler;
        if (mimeType.toLowerCase().indexOf("html") > -1) {
            // BoilerpipeContentHandler extracts the "main" content from HTML pages
            handler = new BoilerpipeContentHandler(new BodyContentHandler(writer));
        }
        else {
            handler = new WriteOutContentHandler(writer);
        }

        Metadata metadata = new Metadata();
        metadata.add(Metadata.CONTENT_TYPE, mimeType);

        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(inputStream, handler, metadata);
        [title:metadata.get(TikaCoreProperties.TITLE), text:writer.toString().trim()]
    }
}
