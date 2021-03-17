package de.httc.plugins.taxonomy

import grails.converters.JSON
import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

import de.httc.plugins.taxonomy.Taxonomy
import de.httc.plugins.taxonomy.TaxonomyTerm

@Secured(['ROLE_ADMIN', 'ROLE_TEACHER'])
class TaxonomiesController {

	def index(Integer max) {
		params.offset = params.offset ? (params.offset as int) : 0
		params.max = Math.min(max ?: 10, 100)
		params.sort = params.sort ?: "label"
		params.order = params.order ?: "asc"
		respond Taxonomy.list(params), model:[taxonomyCount: Taxonomy.count()]
	}

	def create() {
		respond new Taxonomy(params)
	}

	def save(Taxonomy taxonomyInstance) {
		// import uploaded file
		def f = request.getFile("file")
		if (f && !f.empty) {
			f.inputStream.eachLine { line ->
				def term = line.trim()
				if (term.length() > 0) {
					def matcher = line =~ /^(\t*).*$/
					def level = matcher[0][1].length()
					println level + " " + term
					def termInstance = new TaxonomyTerm(label:term, taxonomy:taxonomyInstance)

					def parent = taxonomyInstance
					if (level <=0) {
						parent.addToChildren(termInstance)
					}
					else {
						def children = parent.children
						while (level-- > 0) {
							parent = children.get(children.size() - 1)
							children = parent.children
						}
						parent.addToChildren(termInstance)
					}
				}
			}
		}

		if (!taxonomyInstance.save(flush:true)) {
			respond taxonomyInstance.errors, view:'create'
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), taxonomyInstance.id])
		redirect(action:"show", id:taxonomyInstance.id, plugin:"httcTaxonomy")
	}

	def show(Taxonomy taxonomyInstance) {
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), params.id])
			redirect(action: "index", plugin:"httcTaxonomy")
			return
		}
		withFormat {
			html { respond taxonomyInstance }
			json { render taxonomyInstance as JSON }
			xml { render taxonomyInstance as XML }
		}
	}

	def edit(Taxonomy taxonomyInstance) {
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index", plugin:"httcTaxonomy")
			return
		}

		respond taxonomyInstance
	}

	def update(Taxonomy taxonomyInstance, Long version) {
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), params.id])
			redirect(action: "index", plugin:"httcTaxonomy")
			return
		}
		if (version != null) {
			if (taxonomyInstance.version > version) {
				taxonomyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy')] as Object[],
						  "Another user has updated this Taxonomy while you were editing")
				render(view: "edit", model: [taxonomyInstance: taxonomyInstance])
				return
			}
		}
		if (params.data) {
			def data = JSON.parse(params.data)
			def deletedTermIds = taxonomyInstance.allTermIds
			updateTermsRecursive(taxonomyInstance, taxonomyInstance, data.terms, deletedTermIds)
			// bulk delete to avoid conflicting with referential integrity constraints
			bulkDeleteTermIds(deletedTermIds)
		}

		if (!taxonomyInstance.save(flush:true)) {
			respond taxonomyInstance.errors, view:'edit'
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), message(code: 'de.httc.plugin.taxonomy.label.' + taxonomyInstance.label, default:taxonomyInstance.label)])
		redirect(action:"show", id:taxonomyInstance.id, plugin:"httcTaxonomy")
	}

	def delete(Taxonomy taxonomyInstance) {
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), params.id])
			redirect(action: "index", plugin:"httcTaxonomy")
			return
		}

		try {
			bulkDeleteTermIds(taxonomyInstance.allTermIds)
			taxonomyInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), taxonomyInstance.id])
			redirect(action: "index", plugin:"httcTaxonomy")
		}
		catch (DataIntegrityViolationException e) {
			flash.error = message(code: 'default.not.deleted.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), taxonomyInstance.id])
			redirect(action: "show", id: taxonomyInstance.id, plugin:"httcTaxonomy")
		}
	}

	def updateTermsRecursive(taxonomy, currentNode, jsonChildren, deletedTermIds) {
		currentNode.children?.clear()
		jsonChildren?.eachWithIndex() { jsonChild, i ->
			def termLabel = jsonChild.label?.trim()
			def termId = jsonChild.id?.trim()
			if (termLabel?.length() > 0) {
				def term
				if (termId?.length() > 0) {
					term = TaxonomyTerm.get(termId)
					deletedTermIds.remove(termId)
				}
				else {
					term = new TaxonomyTerm()
				}
				term.label = termLabel
				currentNode.addToChildren(term)
				updateTermsRecursive(taxonomy, term, jsonChild.children, deletedTermIds)
			}
		}
	}

	private def bulkDeleteTermIds(termIds) {
		TaxonomyTerm.where {
			id in termIds
		}.deleteAll()
	}
}
