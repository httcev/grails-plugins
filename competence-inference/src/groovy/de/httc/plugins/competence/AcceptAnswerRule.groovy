package de.httc.plugins.competence

import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type

class AcceptAnswerRule extends Rule {
    def DELTA = (MAX_COMPETENCE * FACTOR_ACCEPT_ANSWER) as int

    def AcceptAnswerRule(taxonomyService) {
	super(taxonomyService)
    }

    def match(interaction) {
	// 'accept answer' rule: increase level of all question-related competences
	if (interaction.subjectType == Type.ACCEPTANCE && interaction.verb == Verb.CREATE) {
	    log.warn "rule 'accept answer' matches..."

	    // first find interaction where the corresponding answer was created
	    def answerId = interaction.objectId;
	    def answerCreateInteraction = Interaction.findWhere(subjectId:answerId, subjectType:Type.ANSWER, verb:Verb.CREATE)
	    if (answerCreateInteraction) {
		// now find corresponding question (which is referenced via objectId of "answerCreateInteraction")
		def questionCreateInteraction = Interaction.findWhere(subjectId:answerCreateInteraction.objectId, subjectType:Type.QUESTION, verb:Verb.CREATE)
		if (questionCreateInteraction) {
		    def relatedTerms = _retrieveOrSuggestTermIds(questionCreateInteraction.subjectId, questionCreateInteraction.subjectContent)
		    if (relatedTerms) {
			return relatedTerms.collect { [userId : answerCreateInteraction.userId, term : it, delta : DELTA] }
		    }
		}
		else {
		    log.error "rule 'accept answer': no question create interaction found for answer ${interaction.objectId}"
		}
	    }
	    else {
		log.error "rule 'accept answer': no answer create interaction found for answer ${interaction.objectId}"
	    }
	    return []
	}
    }
}
