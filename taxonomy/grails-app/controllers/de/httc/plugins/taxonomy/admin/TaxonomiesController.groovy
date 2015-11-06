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
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [taxonomyInstanceList: Taxonomy.list(params), taxonomyInstanceTotal: Taxonomy.count()]
    }

    def create() {
        [taxonomyInstance: new Taxonomy(params)]
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
            render(view: "create", model: [taxonomyInstance: taxonomyInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), taxonomyInstance.id])
        redirect(action: "show", id: taxonomyInstance.id)
    }

    def show(String id) {
        def taxonomyInstance = Taxonomy.get(id)
        if (!taxonomyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
            redirect(action: "list")
            return
        }
		withFormat {
			html { [taxonomyInstance: taxonomyInstance] }
			json { render taxonomyInstance as JSON }
			xml { render taxonomyInstance as XML }
		}
    }

    def edit(String id) {
        def taxonomyInstance = Taxonomy.get(id)
        if (!taxonomyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
            redirect(action: "list")
            return
        }

        [taxonomyInstance: taxonomyInstance]
    }

    def update(String id, Long version) {
        def taxonomyInstance = Taxonomy.get(id)
        if (!taxonomyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
            redirect(action: "list")
            return
        }
        if (version != null) {
            if (taxonomyInstance.version > version) {
                taxonomyInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'taxonomy.label', default: 'Taxonomy')] as Object[],
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
            render(view: "edit", model: [taxonomyInstance: taxonomyInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), taxonomyInstance.id])
        redirect(action: "show", id: taxonomyInstance.id)
    }

    def delete(String id) {
        def taxonomyInstance = Taxonomy.get(id)
        if (!taxonomyInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
            redirect(action: "list")
            return
        }

        try {
            taxonomyInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'taxonomy.label', default: 'Taxonomy'), id])
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
