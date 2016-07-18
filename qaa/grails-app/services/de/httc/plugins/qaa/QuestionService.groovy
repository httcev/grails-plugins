package de.httc.plugins.qaa

import grails.transaction.Transactional
import de.httc.plugins.user.User

@Transactional
class QuestionService {
	def pushNotificationService
	def springSecurityService
	def messageSource

	def saveQuestion(Question question) {
		def isNew = !question.isAttached()
		def result = question.save()
		if (result && isNew) {
			def creator = question.creator
			try {
				def msg = [
					"title":messageSource.getMessage("de.httc.plugin.qaa.push.question.title", null, Locale.GERMAN),
					"message":question.title?.take(30),
					"style":"inbox",
					"collapse_key":"new_questions",
					"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.question.summaryText", null, Locale.GERMAN),
					"referenceId":question.id,
					"referenceClass":Question.class.simpleName
				]
				// only send notification to user that are able to sync the new question
				/*
				def targetCompany = creator.profile?.company
				if (targetCompany) {
					def targetUsers = User.where {
						profile { company == targetCompany }
					}.list()
					targetUsers?.each {
						println "--- found user in same company = " + it
						if (it != creator) {
							pushNotificationService.sendPushNotification(it, msg)
						}
					}
				}
				*/

				// for now, send notification to all users (except creator)
				User.getAll().each { user ->
					if (user != creator) {
						pushNotificationService.sendPushNotification(user, msg)
					}
				}
			}
			catch(e) {
				log.warn "failed sending push notifications for new question", e
			}
		}
		return result
	}

	def saveAnswer(Answer answer) {
		def isNew = !answer.isAttached()
		def result = answer.save()
		if (result && answer.deleted && answer.question) {
			def question = answer.question
			if (question.acceptedAnswer?.id == answer.id) {
				question.acceptedAnswer = null
			}
			question.save()
		}
		else if (result && isNew) {
			def question = answer.question
			def creator = answer.creator
			def targetUser = question?.creator
			if (targetUser && targetUser != creator) {
				try {
					def msg = [
						"title":messageSource.getMessage("de.httc.plugin.qaa.push.answer.title", null, Locale.GERMAN),
						"message":answer.text?.take(30),
						"style":"inbox",
						"collapse_key":"new_answers",
						"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.answer.summaryText", null, Locale.GERMAN),
						"referenceId":question.id,
						"referenceClass":Question.class.simpleName
					]
					pushNotificationService.sendPushNotification(targetUser, msg)
				}
				catch(e) {
					log.warn "failed sending push notifications for new answer", e
				}
			}
		}
		return result
	}

	def saveComment(Comment comment, rootObject = null) {
		def isNew = !comment.isAttached()
		def result = comment.save()
		if (result && isNew) {
			def creator = comment.creator
			def targetUser = comment.reference?.creator
			if (targetUser && targetUser != creator) {
				if (!rootObject) {
					rootObject = comment.reference
					if (rootObject instanceof Answer) {
						rootObject = rootObject.question
					}
				}
				def referenceClass = comment.reference.class.simpleName
				println "--- comment refers (indirectly) to object " + rootObject + ", referenceClass=" + referenceClass
				try {
					def msg = [
						"title":messageSource.getMessage("de.httc.plugin.qaa.push.comment.title", null, Locale.GERMAN),
						"message":comment.text?.take(30),
						"style":"inbox",
						"collapse_key":"new_comments",
						"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.comment.summaryText", null, Locale.GERMAN),
						"referenceId":rootObject.id,
						"referenceClass":referenceClass
					]
					pushNotificationService.sendPushNotification(targetUser, msg)
				}
				catch(e) {
					log.warn "failed sending push notifications for new comment", e
				}
			}
		}
		return result
	}
}
