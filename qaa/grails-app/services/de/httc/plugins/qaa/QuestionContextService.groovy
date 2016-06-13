package de.httc.plugins.qaa

import grails.transaction.Transactional

@Transactional(readOnly = true)
class QuestionContextService {
	def getPossibleQuestionReferences(contextId) {
		log.info "DEFAULT IMPLEMENTATION, returning null"
		return null
	}
}
