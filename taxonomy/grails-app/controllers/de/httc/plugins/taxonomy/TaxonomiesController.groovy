package de.httc.plugins.taxonomy

import grails.converters.JSON
import grails.converters.XML

import org.apache.commons.io.IOUtils
import org.apache.catalina.connector.ClientAbortException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class TaxonomiesController {

	def index() {
		// "list-mode" toggles short/verbose JSON serialization of Taxonomy objects in BootStrap.groovy
		request["list-mode"] = true

		def result = Taxonomy.list()
		withFormat {
			json { render result as JSON }
			xml { render result as XML }
		}
	}

	def show(String id) {
		def taxonomy = Taxonomy.get(id)
		if (!taxonomy) {
			throw new Exception(message(code : "default.not.found.message", args : [message(code : "taxonomy.label", default : "Taxonomy"), id] as Object[]))
		}
		def modifiedSinceHeader = request.getHeader("If-Modified-Since");
		def compareDate = null;
		if (modifiedSinceHeader) {
			try {
				compareDate = new Date(modifiedSinceHeader)
			}
			catch(Exception e) {
				// NOP
			}
		}

		if (compareDate != null && taxonomy.lastUpdated.compareTo(compareDate) <= 0) {
			render(status:304)
			return
		}

		withFormat {
			json { render taxonomy as JSON }
			xml { render taxonomy as XML }
		}
	}
}
