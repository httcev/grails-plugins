package de.httc.plugins.taxonomy

import java.util.UUID;

class Taxonomy {
    static enum Type { DEFAULT, PRIMARY, SECONDARY }
    static hasMany = [terms:TaxonomyTerm]
    static constraints = {
	id bindable:true	// this is needed to import taxonomies from sharepoint and keeping the foreign ids.
    }	
    static mapping = { 
	terms cascade:"all-delete-orphan"
	id (generator: "assigned")	// this is needed to import taxonomies and terms from sharepoint and keeping the foreign ids.
	label type:"text"
    }

    String id = UUID.randomUUID().toString()
    String label
    Type type = Type.DEFAULT
    List<TaxonomyTerm> terms       // defined as list to keep order in which elements got added
    Date lastUpdated
}
