package de.httc.plugins.competence

import de.httc.plugins.competence.Competence.Source

class CompetenceService {
	public void changeLevel(String userId, String primaryTermId, int amount) {
		log.warn "change level with userId=${userId}, primaryTermId=${primaryTermId}, amount=${amount}"
		Competence competence = _getOrCreateCompetence(userId, primaryTermId)
		competence.level = Math.max(0, Math.min(competence.level + amount, Competence.MAX_COMPETENCE))
		if (!competence.save()) {
			competence.errors.allErrors.each {
				log.error it 
			}
		}
	}
		
	def _getOrCreateCompetence(userId, primaryTermId) {
		Competence competence = Competence.findWhere(userId:userId, primaryTermId:primaryTermId, source:Source.COMPUTED)
		if (competence == null) {
			competence = new Competence()
			competence.userId = userId
			competence.primaryTermId = primaryTermId
			competence.source = Source.COMPUTED
			competence.level = 0
		}
		return competence
	}
}
