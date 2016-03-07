package de.httc.plugins.qaa

import grails.transaction.Transactional
import de.httc.plugins.user.User

@Transactional
class QuestionService {
    def pushNotificationService
    def springSecurityService
    def messageSource

    def saveQuestion(Question question) {
        def result = question.save()
        if (result) {
            sendNewQuestionNotification(question)
        }
        return result
    }

    private void sendNewQuestionNotification(question) {
        def msg = [
                "title":messageSource.getMessage("kola.push.assigned.title", null, Locale.GERMAN),
                "message":question.title,
                "style":"inbox",
                "collapse_key":"new_questions",
                "summaryText":messageSource.getMessage("kola.push.assigned.summaryText", null, Locale.GERMAN)
        ]
        User.getAll().each {
            pushNotificationService.sendPushNotification(it, msg)
        }
    }
}
