package de.httc.plugins.competence

import org.springframework.context.ApplicationListener
import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.taxonomy.Taxonomy

import grails.transaction.Transactional
import de.httc.plugins.competence.Interaction.Verb
import de.httc.plugins.competence.Interaction.Type
@Transactional
class CompetenceInferenceService implements ApplicationListener<InteractionEvent> {
    def taxonomyService
    def competenceService

    def rules

    def setTaxonomyService(taxonomyService) {
	rules = []
	rules.add(new CreateAnswerRule(taxonomyService))
	// CreateCommentRule is defined as "ignored" in the paper...
	//rules.add(new CreateCommentRule(taxonomyService))
	rules.add(new CreateQuestionRule(taxonomyService))
	rules.add(new RateAnswerRule(taxonomyService))
	rules.add(new AcceptAnswerRule(taxonomyService))
    }

    void onApplicationEvent(InteractionEvent event) {
	//Competence.async.task {
	 //   withTransaction {
    		_handleInteraction(event.source)
	    //}
	//}
    }

    def _handleInteraction(interaction) {
	try {
	    log.debug "capturing interaction: " + interaction?.properties
	    rules.find {
		def competenceDifferences = it.match(interaction)
		if (competenceDifferences != null) {
		    competenceDifferences.each {
			competenceService.changeLevel(it.userId, String.valueOf(it.term), it.delta)
		    }
		    return true // break
		}
		return false // keep looping
	    }
	}
	catch(e) {
	    log.warn e
	}
    }
}
