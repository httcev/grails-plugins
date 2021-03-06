package de.httc.plugins.repository

import grails.converters.JSON
import grails.converters.XML
import java.io.RandomAccessFile
import java.nio.file.Files
import org.springframework.security.access.annotation.Secured
import static org.springframework.http.HttpStatus.*
import de.httc.plugins.repository.Asset

@Secured(["IS_AUTHENTICATED_REMEMBERED"])
class AssetController {
	def repositoryService

	def index() {
		params.offset = params.offset ? (params.offset as int) : 0
		params.max = Math.min(params.max?.toInteger() ?: 10, 100)
		params.typeLabel = params.containsKey("typeLabel") ? params.typeLabel : "learning-resource"
		params.sort = params.sort ?: "lastUpdated"
		params.order = params.order ?: "desc"

		def result = Asset.createCriteria().list(max:params.max, offset:params.offset) {
			eq("deleted", false)
			eq("typeLabel", params.typeLabel)
			if (params.key) {
				or {
					ilike("name", "%${params.key}%")
					ilike("props", "%${params.key}%")
					ilike("indexText", "%${params.key}%")
				}
			}
			order(params.sort, params.order)
		}
		respond result, model:[assetCount: result.totalCount]
	}

	def show(String id) {
		def asset = repositoryService.readAsset(id)
		if (!asset) {
			notFound()
			return
		}
		respond asset
	}

	@Secured(['permitAll'])
	def viewAsset(String id, String file) {
		def asset = repositoryService.readAsset(id)
		if (!asset) {
			response.sendError(404)
			return
		}

		def externalUrl = asset.props?."${Asset.PROP_EXTERNAL_URL}"
		if (externalUrl) {
			// external asset
			response.sendRedirect(externalUrl)
			return
		}
		// local asset
		def repoFile = repositoryService.getOrCreateRepositoryFile(asset)
		if (!repoFile) {
			response.sendError(404)
			return
		}
		if (repoFile.isDirectory()) {
			// handle special case: zip files
			viewZipFile(asset, file, repoFile)
			return
		}
		renderFile(repoFile, asset.mimeType)
	}

	protected void viewZipFile(Asset asset, String file, File repoFile) {
		if (!file) {
			def anchor = asset.props."${Asset.PROP_ANCHOR}"
			log.debug "redirecting to anchor " + anchor
			redirect(url: repositoryService.createEncodedLink(asset, anchor))
			return
		}
		File zipEntry = new File(repoFile, file)
		if (!zipEntry.exists()) {
			render status: NOT_FOUND
			return
		}
		renderFile(zipEntry, Files.probeContentType(zipEntry.toPath()))
	}

	protected void renderFile(File file, String contentType) {
		response.setHeader("Accept-Ranges", "bytes")
		String range = request.getHeader("range")
		if (range != null && range.length() > 0) {
			def matcher = range =~ /bytes=(\d+)-(\d*)/
			def start = matcher[0][1].toInteger()
			def end = matcher[0][2]
			def fileLength = file.length()
			if (!end) {
				end = fileLength - 1
			}
			else {
				end = end.toInteger()
			}
			// check bounds and conditionally return status 416 ("Requested Range not satisfiable")
			if (end < start || start < 0 || end < 0 || end >= fileLength) {
				response.status = 416
				return
			}
			def length = end - start + 1
			response.status = 206
			response.contentLength = length
			response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength)
			// TODO: try to not allocate array in memory
			RandomAccessFile raf = new RandomAccessFile(file, "r")
			raf.seek(start)
			byte[] buf = new byte[length]
			raf.readFully(buf)

			//response.outputStream << buf
			render(file:buf, contentType:contentType)
		}
		else {
			render(file:file, contentType:contentType)
		}
	}

	protected void notFound() {
		flash.error = message(code: 'default.not.found.message', args: [message(code:'de.httc.plugin.repository.asset', default: 'Asset'), params.id])
		redirect action: "index", method: "GET"
	}
/*
	def afterInterceptor = { data ->
		if (!response.committed) {
			if (data?.model != null) {
				withFormat {
					json { render data.model as JSON }
					xml { render data.model as XML }
				}
			}
			else {
				// empty response, e.g. for DELETEs (otherwise grails creates a 404)
				render(text:"")
			}
		}
	}
*/
}
