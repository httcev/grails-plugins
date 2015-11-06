package de.httc.plugins.competence

import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type

class CreateCommentRule extends Rule {
    def DELTA_QUESTION = (MAX_COMPETENCE * FACTOR_COMMENT_QUESTION) as int
    def DELTA_ANSWER = (MAX_COMPETENCE * FACTOR_COMMENT_ANSWER) as int

    def CreateCommentRule(taxonomyService) {
	super(taxonomyService)
    }

    def match(interaction) {
	if (interaction.subjectType == Type.COMMENT && interaction.verb == Verb.CREATE) {
	    log.warn "rule 'create comment' matches..."

	    def questionCreateInteraction
	    def createInteraction = Interaction.findWhere(subjectId:interaction.objectId, verb:Verb.CREATE)
	    if (createInteraction) {
		def DELTA
		if (createInteraction.subjectType == Type.QUESTION) {
		    questionCreateInteraction = createInteraction
		    DELTA = DELTA_QUESTION
		}
		else {
		    questionCreateInteraction = Interaction.findWhere(subjectId:createInteraction.objectId, verb:Verb.CREATE)
		    DELTA = DELTA_ANSWER
		}
		if (questionCreateInteraction) {
		    def relatedTerms = _retrieveOrSuggestTermIds(questionCreateInteraction.subjectId, questionCreateInteraction.subjectContent)
		    if (relatedTerms) {
			return relatedTerms.collect {
			    [userId : interaction.userId, term : it, delta : DELTA]
			}
		    }
		}
		else {
		    log.error "rule 'create comment': no question create interaction found for comment on ${interaction.objectId}"
		}
	    }
	    else {
		log.error "rule 'create comment': no create interaction found for comment on ${interaction.objectId}"
	    }
	    return []
	}
    }
}
