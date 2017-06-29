package de.httc.plugins.pushNotification

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured
import grails.transaction.Transactional

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
@Transactional
class PushTokenController {
	static allowedMethods = [index:"GET", update:"POST"]
	def springSecurityService
	def settingService

	@Secured("permitAll")
	def index() {
		render(contentType: 'text/json') {[
			"senderId": settingService.getValue("gcmSenderId")
		]}
	}

	def update() {
		def json = request.JSON
		if (!(json.token && json.token instanceof String && json.token.length() > 0)) {
			render(status:400, text:"missing token definition")
			return
		}
		def currentUser = springSecurityService.currentUser
		log.info "Registering push notification token for user ${currentUser.username}"
		def pushToken = PushToken.get(currentUser.id)
		if (!pushToken) {
			pushToken = new PushToken(user:currentUser)
		}

		pushToken.token = json.token
		if (pushToken.save()) {
			render(status:204)
		}
		else {
			pushToken.errors.allErrors.each {
				println it
			}
			render(status:500)
		}
	}
}