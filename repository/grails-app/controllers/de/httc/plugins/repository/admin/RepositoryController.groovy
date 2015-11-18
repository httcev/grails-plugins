package de.httc.plugins.repository.admin

import grails.transaction.Transactional
import org.apache.tika.mime.MediaType
import org.apache.tika.metadata.Metadata
import org.apache.tika.config.TikaConfig
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.html.BoilerpipeContentHandler
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.sax.WriteOutContentHandler
import org.xml.sax.ContentHandler
import java.util.zip.ZipInputStream
import org.springframework.security.access.annotation.Secured
import org.springframework.web.context.request.RequestContextHolder
import de.httc.plugins.repository.ZipUtil
import de.httc.plugins.repository.Asset

@Transactional
@Secured(['ROLE_ADMIN', 'ROLE_REPOSITORY_ADMIN'])
class RepositoryController {
    static namespace = "admin"

    //static allowedMethods = [save: "POST", update: ["PUT", "POST"], delete: "DELETE"]

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def index(Integer max) {
        params.offset = params.offset ? (params.offset as int) : 0
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: "lastUpdated"
        params.order = params.order ?: "desc"
        def query = Asset.where { deleted == false }
        respond query.list(params), model:[assetInstanceCount: query.count()]
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def show(Asset assetInstance) {
        if (assetInstance == null) {
            notFound()
            return
        }
        respond assetInstance
    }

    def createFlow = {
        initiliaze {
            action {
                flow.cmd = new CreateAssetCommand()
//                flow.cmd.props =  MapUtils.lazyMap([:], FactoryUtils.constantFactory(''))
//                flow.cmd.props = [:]
            }
            on("success").to "uploadOrLink"
            on(Exception).to "error"
        }
        
        uploadOrLink {
            on("submit") {
                bindData(flow.cmd, params)
                if (!flow.cmd.content && !flow.cmd.props?."${Asset.PROP_EXTERNAL_URL}") {
                    flow.cmd.errors.rejectValue('content', 'nullable')
                    flow.cmd.errors.rejectValue('props', 'nullable')
                    error()
                }
            }.to "checkUploadOrLink"
        }
        checkUploadOrLink {
            action {
                try {
                    enrichAsset(flow.cmd)
                    if (flow.cmd.content && "application/zip".equals(flow.cmd.mimeType?.toLowerCase())) {
                        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new ByteArrayInputStream(flow.cmd.content)))
                        def possibleAnchors = ZipUtil.getFilenames(zin, false)
                        if (possibleAnchors.length > 0) {
                            flow.possibleAnchors = possibleAnchors
                            return chooseAnchor()
                        }

                    }
                    return metadata()
                }
                catch(e) {
                    println e
                    log.error e
                    if (flow.cmd.props?."${Asset.PROP_EXTERNAL_URL}") {
                        flow.cmd.errors.rejectValue('props', 'urlNotValid')
                    }
                    return error()
                }
            }
            on("chooseAnchor").to "chooseAnchor"
            on("metadata").to "metadata"
            on("error").to "uploadOrLink"
        }
        chooseAnchor {
            on("submit") {
                bindData(flow.cmd, params)
            }.to "metadata" 
        }
        metadata {
            on("submit") {
                bindData(flow.cmd, params)
                if (!flow.cmd.validate()) {
                    return error()
                }
                def assetInstance = new Asset()
                assetInstance.properties = flow.cmd
                assetInstance.validate()
                assetInstance.errors.allErrors.each { println it }

                if (assetInstance.save(true)) {
                    RequestContextHolder.currentRequestAttributes().flashScope.message = message(code: 'default.created.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
                    // need to clear the flow's persistence context, otherwise we get a NotSerializableException for the newly saved Asset
                    flow.persistenceContext.clear()
                    return success()
                }
                else {
                    return error()  
                }
                return success()
            }.to "finish"
        }
        finish {
        }
    }

    def edit(Asset assetInstance) {
        if (assetInstance == null) {
            notFound()
            return
        }
        respond assetInstance
    }

    @Transactional
    def update(Asset assetInstance) {
        if (assetInstance == null) {
            notFound()
            return
        }

        enrichAsset(assetInstance)

        if (assetInstance.hasErrors()) {
            respond assetInstance.errors, view:'edit'
            return
        }

        assetInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
                redirect action:"show", id:assetInstance.id
            }
            '*'{ respond assetInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Asset assetInstance) {

        if (assetInstance == null) {
            notFound()
            return
        }

        assetInstance.deleted = true
        assetInstance.save flush:true
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
        redirect action:"index", method:"GET"
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), params.id])
        redirect action: "index", method: "GET"
    }

    protected void enrichAsset(assetOrCommand) {
        def detector = TikaConfig.getDefaultConfig().getDetector();
        Metadata metadata = new Metadata()
        def inputStream
        def externalUrl = assetOrCommand.props."${Asset.PROP_EXTERNAL_URL}"
        if (assetOrCommand.content || externalUrl) {
            try {
                if (assetOrCommand.content) {
                    // remove externalUrl since we're now expecting a local asset
                    assetOrCommand.props[Asset.PROP_EXTERNAL_URL] = null
                    def uploadFile = request.getFile("content")
                    if (uploadFile) {
                        assetOrCommand.props."${Asset.PROP_FILENAME}" = uploadFile.getOriginalFilename()
                    }

                    inputStream = new ByteArrayInputStream(assetOrCommand.content)
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

@grails.validation.Validateable
class CreateAssetCommand implements Serializable {
    private static final long serialVersionUID = 42L;

    static constraints = {
        // Limit upload file size to 100MB
        content maxSize: 1024 * 1024 * 100, nullable:true
        name blank:false
        indexText nullable: true
        type nullable: true
    }

    String name
    String mimeType
    String type
    String indexText
    byte[] content
    // need to define this as HashMap, otherwise grails instantiates a GrailsParameterMap which is not serializable
    HashMap<String, String> props


/*
    String subType = "learning-resource"
*/
}
