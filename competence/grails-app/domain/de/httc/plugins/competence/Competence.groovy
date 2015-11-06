package de.httc.plugins.competence

import java.util.Date;

import de.httc.plugins.taxonomy.TaxonomyTerm

class Competence {
    static enum Source {
	CERTIFICATE, OBJECTIVE_ASSESSMENT_SENIOR, OBJECTIVE_ASSESSMENT_PEER, SELF_ASSESSMENT, COMPUTED
    }
    static final MAX_COMPETENCE = 1000
    static transients = ["primaryTermLabel"]
    static hasMany = [secondaryTermIds:String]

    static constraints = {
	level min:0, max:MAX_COMPETENCE
	secondaryTermIds nullable:true
    }

    String userId
    String primaryTermId
    List<String> secondaryTermIds
    Source source
    int level
    
    Date dateCreated
    Date lastUpdated

    public String getPrimaryTermLabel() {
	return TaxonomyTerm.get(primaryTermId)?.label
    }
}
