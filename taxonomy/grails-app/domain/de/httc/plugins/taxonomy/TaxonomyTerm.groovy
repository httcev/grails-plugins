package de.httc.plugins.taxonomy

class TaxonomyTerm extends TaxonomyNode {
	static constraints = {
		taxonomy nullable:true
		parent nullable:false
	}

	Taxonomy taxonomy
	TaxonomyNode parent

	def beforeInsert() {
		updateTaxonomyReference()
	}

	def beforeUpdate() {
		if (isDirty('label') || isDirty('parent')) {
			updateTaxonomyReference()
		}
	}

	private void updateTaxonomyReference() {
		def root = parent
		while (root?.hasProperty("parent") && root.parent != null) {
			root = root.parent
		}
		if (!(root instanceof Taxonomy)) {
			log.warn("couldn't infer taxonomy into term ${this.label}, root=${root?.properties}")
		}
		else {
			taxonomy = root
		}

		children?.each {
			it.updateTaxonomyReference()
		}
	}

	static _embedded = ["label"]
	static _referenced = ["taxonomy", "parent", "children"]

	static {
		grails.converters.JSON.registerObjectMarshaller(TaxonomyTerm) { term ->
			def doc = term.properties.findAll { k, v ->
				k in _embedded
			}
			_referenced.each {
				if (term."$it" instanceof List) {
					doc."$it" = term."$it"?.collect {
						it?.id
					}
				}
				else {
					doc."$it" = term."$it"?.id
				}
			}
			doc.id = term.id
			return [id:term.id, doc:doc]
		}
	}
}
