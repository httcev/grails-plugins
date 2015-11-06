package de.httc.plugins.competence

import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.taxonomy.Taxonomy
import de.httc.plugins.competence.Interaction.Type
import de.httc.plugins.competence.Interaction.Verb

abstract class Rule {
    def taxonomyService
    
    def MAX_COMPETENCE = 1000
    def FACTOR_CREATE_QUESTION = -0.02
    def FACTOR_CREATE_ANSWER = 0.02
    def FACTOR_RATE_ANSWER_QUESTIONER = 0.005
    def FACTOR_RATE_ANSWER_ANSWERER = 0.01
    def FACTOR_RATE_ANSWER_RATER = 0.005
    def FACTOR_COMMENT_QUESTION = -0.02
    def FACTOR_COMMENT_ANSWER = -0.02
    def FACTOR_ACCEPT_ANSWER = 0.02
    
    def Rule(taxonomyService) {
	this.taxonomyService = taxonomyService
    }
 
    abstract match(interaction);
    
    def _retrieveOrSuggestTermIds(questionId, questionText) {
	// first search for interactions where users have associated taxonomy terms to the question. if there are none, auto-suggest best term matches
	def terms = [] as Set
	def suggested = false
	Interaction.findAll { objectId == questionId && subjectType == Type.TAXONOMYRELATION && objectType == Type.QUESTION }?.each {
	    if (Verb.CREATE == it.verb) {
		terms.add(it.subjectId)
	    }
	    else if (Verb.DELETE == it.verb) {
		terms.remove(it.subjectId)
	    }
	}
	if (terms.size() == 0) {
	    terms = taxonomyService.suggestTerms(questionText, true)?.collect { print "term ${it.object.id} sim=${it.similarity}"; it.object.id }
	    suggested = true
	}
	/*
	if (terms) {
	    println "--- TERMS (suggested=${suggested}) for ${questionText}:"
	    terms.each {
		println TaxonomyTerm.findWhere(id:it)?.label
	    }
	}
	*/
	return terms
    }
}


