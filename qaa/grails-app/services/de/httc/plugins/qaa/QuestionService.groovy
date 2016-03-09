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
					"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.question.summaryText", null, Locale.GERMAN)
		        ]
		        User.getAll().each {
					if (it != creator) {
			            pushNotificationService.sendPushNotification(it, msg)
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
		if (result && isNew) {
			def creator = answer.creator
			def targetUser = answer.question?.creator
			if (targetUser && targetUser != creator) {
				try {
					def msg = [
						"title":messageSource.getMessage("de.httc.plugin.qaa.push.answer.title", null, Locale.GERMAN),
						"message":answer.text?.take(30),
						"style":"inbox",
						"collapse_key":"new_answers",
						"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.answer.summaryText", null, Locale.GERMAN)
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

    def saveComment(Comment comment) {
		def isNew = !comment.isAttached()
		def result = comment.save()
		if (result && isNew) {
			def creator = comment.creator
			def targetUser = comment.reference?.creator
			if (targetUser && targetUser != creator) {
				try {
					def msg = [
						"title":messageSource.getMessage("de.httc.plugin.qaa.push.comment.title", null, Locale.GERMAN),
						"message":comment.text?.take(30),
						"style":"inbox",
						"collapse_key":"new_comments",
						"summaryText":messageSource.getMessage("de.httc.plugin.qaa.push.comment.summaryText", null, Locale.GERMAN)
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
