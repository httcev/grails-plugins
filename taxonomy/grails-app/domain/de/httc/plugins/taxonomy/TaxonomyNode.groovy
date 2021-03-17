package de.httc.plugins.taxonomy

import java.util.UUID;
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes="id")
abstract class TaxonomyNode {
	static searchable = { only = ["label"]}
	static mappedBy = [children:"parent"]
	static hasMany = [children:TaxonomyTerm]
	static constraints = {
		id bindable:true
	}
	static mapping = {
		id generator:"assigned"
		label type:"text"
		//children cascade:"all-delete-orphan"
	}

	String id = UUID.randomUUID().toString()
	String label
	List<TaxonomyTerm> children	   // defined as list to keep order in which elements got added
	Date lastUpdated
}
