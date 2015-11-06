package de.httc.plugins.competence

import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type

class CreateAnswerRule extends Rule {
    def DELTA = (MAX_COMPETENCE * FACTOR_CREATE_ANSWER) as int

    def CreateAnswerRule(taxonomyService) {
	super(taxonomyService)
    }

    def match(interaction) {
	// 'create answer' rule: increase level of all question-related competences
	if (interaction.subjectType == Type.ANSWER && interaction.verb == Verb.CREATE) {
	    log.warn "rule 'create answer' matches..."

	    def questionCreateInteraction = Interaction.findWhere(subjectId:interaction.objectId, subjectType:Type.QUESTION, verb:Verb.CREATE)
	    if (questionCreateInteraction) {
		// now get question-related competences
		def relatedTerms = _retrieveOrSuggestTermIds(questionCreateInteraction.subjectId, questionCreateInteraction.subjectContent)
		if (relatedTerms) {
		    return relatedTerms.collect { [userId : interaction.userId, term : it, delta : DELTA] }
		}
	    }
	    else {
		log.error "rule 'create answer': no question create interaction found for answer ${interaction.subjectId}"
	    }
	    return []
	}
    }
}
