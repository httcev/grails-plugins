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
}
