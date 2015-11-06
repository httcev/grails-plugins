package de.httc.plugins.competence

import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type

class RateAnswerRule extends Rule {
    def DELTA_QUESTIONER = (MAX_COMPETENCE * FACTOR_RATE_ANSWER_QUESTIONER) as int
    def DELTA_ANSWERER = (MAX_COMPETENCE * FACTOR_RATE_ANSWER_ANSWERER) as int
    def DELTA_RATER = (MAX_COMPETENCE * FACTOR_RATE_ANSWER_RATER) as int

    def RateAnswerRule(taxonomyService) {
	super(taxonomyService)
    }

    def match(interaction) {
	if (interaction.subjectType == Type.RATING && interaction.verb == Verb.CREATE) {
	    // first find interaction where the corresponding answer was created
	    def createInteraction = Interaction.findWhere(subjectId:interaction.objectId, verb:Verb.CREATE)
	    if (createInteraction) {
		if (createInteraction.subjectType == Type.ANSWER) {
		println "-------- CREATE ANSWER INT FOUND!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
		    log.warn "rule 'rate answer' matches..."
		    // now find corresponding question (which is referenced via objectId of "createInteraction")
		    def questionCreateInteraction = Interaction.findWhere(subjectId:createInteraction.objectId, subjectType:Type.QUESTION, verb:Verb.CREATE)
		    if (questionCreateInteraction) {
			def relatedTerms = _retrieveOrSuggestTermIds(questionCreateInteraction.subjectId, questionCreateInteraction.subjectContent)
			if (relatedTerms) {
			    def questioner = questionCreateInteraction.userId
			    def answerer = createInteraction.userId
			    def rater = interaction.userId

			    def result = []
			    result.addAll(relatedTerms.collect { [userId : questioner, term : it, delta : DELTA_QUESTIONER] })
			    if (questioner != answerer) {
				result.addAll(relatedTerms.collect { [userId : answerer, term : it, delta : DELTA_ANSWERER] })
			    }
			    if (questioner != rater && answerer != rater) {
				result.addAll(relatedTerms.collect { [userId : rater, term : it, delta : DELTA_RATER] })
			    }
			    return result
			}
		    }
		    else {
			log.error "rule 'rate answer': no question create interaction found for answer ${createInteraction.subjectId}"
		    }
		    return []
		}
		else {
		    // this is a question that has been rated, so return null because this rule actually didn't match
		    log.info "rule 'rate answer': false positive, a question has been rated..."
		    // TODO: maybe decrease competence for rating user?
		    return null
		}
	    }
	    else {
		log.error "rule 'rate answer': no create interaction found for rate interaction on ${interaction.objectId}"
	    }
	}
    }
}
