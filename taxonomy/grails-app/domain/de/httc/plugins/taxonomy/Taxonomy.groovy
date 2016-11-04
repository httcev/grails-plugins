package de.httc.plugins.taxonomy

import java.util.UUID;

class Taxonomy extends TaxonomyNode {
	static enum Type { DEFAULT, PRIMARY, SECONDARY }
	static constraints = {
//		parent nullable:true
	}

	Type type = Type.DEFAULT

	def getTermCount() {
		return TaxonomyTerm.countByTaxonomy(this)
	}

	/**
		Retrieves all terms ids of this taxonomy disregarding the hierarchy
	**/
	def getAllTermIds() {
		return TaxonomyTerm.executeQuery("select t.id from TaxonomyTerm t where t.taxonomy = ?", [this])
	}

	static _embedded = ["label", "type"]
	static _referenced = ["parent", "children"]

	static {
		grails.converters.JSON.registerObjectMarshaller(Taxonomy) { taxonomy ->
			def doc = taxonomy.properties.findAll { k, v ->
				k in _embedded
			}
			_referenced.each {
				if (taxonomy."$it" instanceof List) {
					doc."$it" = taxonomy."$it"?.collect {
						it?.id
					}
				}
				else {
					doc."$it" = taxonomy."$it"?.id
				}
			}
			doc.id = taxonomy.id
			return [id:taxonomy.id, doc:doc]
		}
	}
}
