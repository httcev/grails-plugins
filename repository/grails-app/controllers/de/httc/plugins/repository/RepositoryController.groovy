package de.httc.plugins.repository

import grails.converters.JSON
import grails.converters.XML

import org.springframework.security.access.annotation.Secured

@Secured(["IS_AUTHENTICATED_FULLY"])
class RepositoryController {
	def repositoryService 
	
	def list() {
//		Thread.sleep(1000);
		println "--- type='" + params.type + "'"
		def filter = [:]
		filter.offset = params["start"] ?: 0
		filter.max = Math.min(params.max?.toInteger() ?: 100, 1000)
		filter.type = params.containsKey("type") ? params.type : null

		return [model:repositoryService.list(filter)]
	}

	def show(Long id) {
		return [model:repositoryService.get(id)]
	}
	
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
}
