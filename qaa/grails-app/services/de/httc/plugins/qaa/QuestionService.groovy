package de.httc.plugins.qaa

import grails.transaction.Transactional
import de.httc.plugins.user.User

@Transactional
class QuestionService {
	def pushNotificationService
	def springSecurityService
	def messageSource
	def lrsService
	def grailsLinkGenerator
	def settingService

	def saveQuestion(Question question) {
		def isNew = !question.isAttached()
		def result = question.save()
		if (result && isNew) {
			// log to LRS
			def activity = new gov.adlnet.xapi.model.Activity()
			activity.setId(grailsLinkGenerator.link(absolute:true, controller:"question", action:"show", id:question.id))
			activity.setDefinition(new gov.adlnet.xapi.model.ActivityDefinition(["de-DE":question.title], ["de-DE":question.text]))
			lrsService.log(gov.adlnet.xapi.model.Verbs.asked(), activity)

			def questionCreator = question.creator
			try {
				def msg = [
					"data" : [
						"title" : messageSource.getMessage("de.httc.plugin.qaa.push.question.title", null, Locale.GERMAN),
						"body" : question.title?.take(30),
						"category" : "new_questions",
						"referenceId":question.id,
						"referenceClass":Question.class.simpleName
					]
				]

				def hasTaskReference = question.reference != null
				def channel = settingService.getValue(hasTaskReference ? "questionChannelTask": "questionChannelGeneral")
				def targetUsers = [] as HashSet
				if (hasTaskReference && question.reference.creator != null) {
					targetUsers.add(question.reference.creator)
				}

				if ("broadcast" == channel) {
					// boradcast
					targetUsers.addAll(User.getAll())
				}
				else if ("company" == channel) {
					// send only to company of questioner
					def targetCompany = questionCreator.profile?.company
					if (targetCompany) {
						targetUsers.addAll(User.where {
							profile { company == targetCompany }
						}.list())
					}
				}
				else if ("groups" == channel) {
					// send only to members of groups of questioner
					def userGroups = questionCreator.profile?.organisations*.id
					if (userGroups?.size() > 0) {
						targetUsers.addAll(User.createCriteria().listDistinct {
							profile {
								organisations {
									'in'('id', userGroups)
								}
							}
						})
					}
				}
				else if ("creator" == channel) {
					// send only to creator / assignee
					def assignee
					if (question.reference?.hasProperty("task")) {
						assignee = question.reference?.task?.assignee
					}
					else {
						assignee = question.reference?.assignee
					}
					if (assignee && !targetUsers.contains(assignee)) {
						targetUsers.add(assignee)
					}
				}
				targetUsers.remove(questionCreator)
				targetUsers.each { user ->
					pushNotificationService.sendPushNotification(user, msg)
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
		if (result) {
			def question = answer.question
			if (answer.deleted && question.acceptedAnswer?.id == answer.id) {
				question.acceptedAnswer = null
			}
			question.touch().save()

			if (isNew) {
				def creator = answer.creator
				def targetUser = question?.creator

				// log to LRS
				def activity = new gov.adlnet.xapi.model.Activity()
				activity.setId(grailsLinkGenerator.link(absolute:true, controller:"question", action:"show", id:question.id, fragment:answer.id))
				activity.setDefinition(new gov.adlnet.xapi.model.ActivityDefinition(["de-DE":answer.text], [:]))
				lrsService.log(gov.adlnet.xapi.model.Verbs.answered(), activity)

				if (targetUser && targetUser != creator) {
					try {
						def msg = [
							"data" : [
								"title" : messageSource.getMessage("de.httc.plugin.qaa.push.answer.title", null, Locale.GERMAN),
								"message" : answer.text?.take(30),
								"category" : "new_answers",
								"referenceId":question.id,
								"referenceClass":Question.class.simpleName
							]
						]
						pushNotificationService.sendPushNotification(targetUser, msg)
					}
					catch(e) {
						log.warn "failed sending push notifications for new answer", e
					}
				}
			}
		}
		return result
	}

	def saveComment(Comment comment, rootObject = null) {
		def isNew = !comment.isAttached()
		def result = comment.save()
		if (result) {
			comment.reference.touch().save()

			if (isNew) {
				def creator = comment.creator
				def targetUser = comment.reference?.creator

				if (!rootObject) {
					rootObject = comment.reference
					if (rootObject instanceof Answer) {
						rootObject = rootObject.question
					}
				}

				// log to LRS
				def activity = new gov.adlnet.xapi.model.Activity()
				activity.setId(grailsLinkGenerator.link(absolute:true, controller:"question", action:"show", id:rootObject.id, fragment:comment.id))
				activity.setDefinition(new gov.adlnet.xapi.model.ActivityDefinition(["de-DE":comment.text], [:]))
				lrsService.log(gov.adlnet.xapi.model.Verbs.commented(), activity)

				if (targetUser && targetUser != creator) {
					def referenceClass = comment.reference.class.simpleName
					try {
						def msg = [
							"data" : [
								"title" : messageSource.getMessage("de.httc.plugin.qaa.push.comment.title", null, Locale.GERMAN),
								"body" : comment.text?.take(30),
								"category" : "new_comments",
								"referenceId":rootObject.id,
								"referenceClass":referenceClass
							]
						]
						pushNotificationService.sendPushNotification(targetUser, msg)
					}
					catch(e) {
						log.warn "failed sending push notifications for new comment", e
					}
				}
			}
		}
		return result
	}
}
