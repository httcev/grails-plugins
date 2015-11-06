package de.httc.plugins.competence

import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type

class CreateQuestionRule extends Rule {
    def DELTA = (MAX_COMPETENCE * FACTOR_CREATE_QUESTION) as int

    def CreateQuestionRule(taxonomyService) {
	super(taxonomyService)
    }

    def match(interaction) {
	// 'create question' rule: decrease level of all question-related competences
	if (interaction.subjectType == Type.QUESTION && interaction.verb == Verb.CREATE) {
	    log.warn "rule 'create question' matches..."
	    def relatedTerms = _retrieveOrSuggestTermIds(interaction.subjectId, interaction.subjectContent)
	    if (!relatedTerms) {
		return []
	    }
	    return relatedTerms.collect { [userId : interaction.userId, term : it, delta : DELTA] }
	}
    }
}
