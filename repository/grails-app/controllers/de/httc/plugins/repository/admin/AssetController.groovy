package de.httc.plugins.repository.admin

import grails.transaction.Transactional
import java.util.zip.ZipInputStream
import org.springframework.security.access.annotation.Secured
import org.springframework.web.context.request.RequestContextHolder
import de.httc.plugins.repository.ZipUtil
import de.httc.plugins.repository.Asset
import de.httc.plugins.repository.AssetContent
import de.httc.plugins.repository.CreateAssetCommand

@Transactional
@Secured(['ROLE_ADMIN', 'ROLE_REPOSITORY_ADMIN'])
class AssetController {
    static namespace = "admin"
    def repositoryService

    //static allowedMethods = [save: "POST", update: ["PUT", "POST"], delete: "DELETE"]

    def createFlow = {
        initialize {
            action {
                flow.cmd = new CreateAssetCommand()
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
                    repositoryService.enrichAsset(flow.cmd, request.getFile("content"))
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
                assetInstance.properties = flow.cmd.properties.findAll { !(it.key in ["content", "errors", "class", "constraints"]) }
                if (flow.cmd.content?.length > 0) {
                    assetInstance.content = new AssetContent(data:flow.cmd.content)
                }
              
                if (assetInstance.save(true)) {
                    RequestContextHolder.currentRequestAttributes().flashScope.message = message(code: 'default.created.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
                    // need to clear the flow's persistence context, otherwise we get a NotSerializableException for the newly saved Asset
                    flow.persistenceContext.clear()
                    return success()
                }
                else {
                    assetInstance.errors.allErrors.each { println it }
                    return error()  
                }
                return success()
            }.to "finish"
        }
        finish {
            redirect(action:"index")
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

        repositoryService.enrichAsset(assetInstance)

        if (assetInstance.hasErrors()) {
            respond assetInstance.errors, view:'edit'
            return
        }

        assetInstance.save flush:true
/*
        request.withFormat {
            html {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
                redirect action:"show", id:assetInstance.id
            }
            '*' {
                respond assetInstance, [status: 200]
            }
        }
        */
        flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.repository.asset', default: 'Asset'), assetInstance.name])
        redirect action:"show", id:assetInstance.id
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
}