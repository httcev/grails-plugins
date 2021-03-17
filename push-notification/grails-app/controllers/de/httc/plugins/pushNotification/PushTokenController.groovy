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
		def responseData = [
			"senderId": settingService.getValue("gcmSenderId"),
			"channel": settingService.getValue("gcmChannel")
		]
		render responseData as JSON
	}

	def update() {
		def json = request.JSON
		if (!(json.token && json.token instanceof String && json.token.length() > 0)) {
			render(status:400, text:"missing token definition")
			return
		}
		// prevent duplicates, so first remove this token from all other users.
		// this can be the case when a user changes login credentials in the app.
		PushToken.where { token == json.token }.deleteAll()

		def currentUser = springSecurityService.currentUser
		log.info "Registering push notification token for user ${currentUser.username}"
		def pushToken = PushToken.get(currentUser.id)
		if (!pushToken) {
			pushToken = new PushToken(user:currentUser)
		}
		pushToken.token = json.token
		if (pushToken.save(flush: true)) {
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
