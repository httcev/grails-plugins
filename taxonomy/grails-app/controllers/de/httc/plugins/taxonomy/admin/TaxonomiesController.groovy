package de.httc.plugins.taxonomy.admin

import grails.converters.JSON
import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.access.annotation.Secured

import de.httc.plugins.taxonomy.Taxonomy
import de.httc.plugins.taxonomy.TaxonomyTerm

@Secured(['ROLE_ADMIN'])
class TaxonomiesController {
	static namespace = "admin"
	//static allowedMethods = [save: "POST", update: "POST", delete: "POST", list:"GET", index:"GET"]
/*
	def index() {
		redirect(action: "list", controller:"taxonomies", namespace:"admin", plugin:"httcTaxonomy", params: params)
	}
*/
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

	def save() {
		def taxonomyInstance = new Taxonomy(params)

		// import uploaded file
		def f = request.getFile("file")
		if (f && !f.empty) {
			println ("--- import!")
			f.inputStream.eachLine { line ->
				def term = line.trim()
				if (term.length() > 0) {
					def matcher = line =~ /^(\t*).*$/
					def level = matcher[0][1].length()
					println level + " " + term
					def termInstance = new TaxonomyTerm(label:term, taxonomy:taxonomyInstance)

					def parent = taxonomyInstance
					if (level <=0) {
						parent.addToTerms(termInstance)
					}
					else {
						def children = parent.terms
						while (level-- > 0) {
							parent = children.get(children.size() - 1)
							children = parent.children
						}
						parent.addToChildren(termInstance)
					}
				}
			}
		}

		if (!taxonomyInstance.save(flush: true)) {
			respond taxonomyInstance.errors, view:'create'
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), taxonomyInstance.id])
		redirect(action:"show", id:taxonomyInstance.id, namespace:"admin", plugin:"httcTaxonomy")
	}

	def show(String id) {
		def taxonomyInstance = Taxonomy.get(id)
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index")
			return
		}
		withFormat {
			html { respond taxonomyInstance }
			json { render taxonomyInstance as JSON }
			xml { render taxonomyInstance as XML }
		}
	}

	def edit(String id) {
		def taxonomyInstance = Taxonomy.get(id)
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index")
			return
		}

		respond taxonomyInstance
	}

	def update(String id, Long version) {
		def taxonomyInstance = Taxonomy.get(id)
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index")
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
		if (params.label) {
			taxonomyInstance.label = params.label
		}
		if (params.type) {
			taxonomyInstance.type = Taxonomy.Type.valueOf(params.type)
		}

		if (params.data) {
			def data = JSON.parse(params.data)
			def deletedTerms = []
			updateChildrenRecursive(taxonomyInstance, taxonomyInstance.terms, data.terms, deletedTerms)

		}
		if (!taxonomyInstance.save(flush: true)) {
			respond taxonomyInstance.errors, view:'edit'
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), taxonomyInstance.id])
		redirect(action:"show", id:taxonomyInstance.id, plugin:"httcTaxonomy")
	}

	def delete(String id) {
		def taxonomyInstance = Taxonomy.get(id)
		if (!taxonomyInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index")
			return
		}

		try {
			taxonomyInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "index")
		}
		catch (DataIntegrityViolationException e) {
			flash.error = message(code: 'default.not.deleted.message', args: [message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy'), id])
			redirect(action: "show", id: id)
		}
	}

	def updateChildrenRecursive(currentNode, currentChildren, jsonChildren, deletedNodes) {
		currentChildren.clear()
		jsonChildren?.eachWithIndex() { jsonChild, i ->
			println " ${i}: ${jsonChild}"
			def term = jsonChild.id ? TaxonomyTerm.get(jsonChild.id) : new TaxonomyTerm()
			term.label = jsonChild.label
			currentChildren.add(term)
		}
		currentChildren.eachWithIndex() { child, i ->
			println "child.children=" + child.children
			if (child.children == null) {
				child.children = []
			}
			updateChildrenRecursive(child, child.children, jsonChildren.get(i).children, deletedNodes)
		}

/*
		if (jsonChildren?.size() > 0) {
			jsonChildren.eachWithIndex() { jsonChild, i ->
				println " ${i}: ${jsonChild}"
			};
		}
*/
	}
}
