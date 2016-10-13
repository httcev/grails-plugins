package de.httc.plugins.lrs

import org.springframework.context.ApplicationListener
import org.springframework.context.ApplicationEvent
import grails.transaction.Transactional
import de.httc.plugins.user.User
import gov.adlnet.xapi.model.*
import gov.adlnet.xapi.client.StatementClient
import static grails.async.Promises.task

@Transactional(readOnly = true)
class LrsService implements ApplicationListener<ApplicationEvent> {
	def springSecurityService
	def settingService
	def grailsLinkGenerator
	def client

	def log(Verb verb, String activityId) {
		log(verb, createActivity(activityId))
	}

	def log(Verb verb, Activity activity) {
		log(springSecurityService.currentUser, verb, activity)
	}

	def log(User actor, Verb verb, String activityId) {
		log(actor, verb, createActivity(activityId))
	}

	def log(User actor, Verb verb, Activity activity) {
		task {
			if (!actor) {
				log.warn("not publishing to LRS because actor is undefined. Verb=${verb}, Activity=${activity}")
				return
			}
			if (!verb) {
				log.warn("not publishing to LRS because verb is undefined. Actor=${actor}, Activity=${activity}")
				return
			}
			if (!activity) {
				log.warn("not publishing to LRS because activity is undefined. Actor=${actor}, Verb=${verb}")
				return
			}
			if (client) {
				try {
					Agent agent = new Agent(actor.profile.displayName, "mailto:${actor.email}")
					Statement statement = new Statement();
					statement.setActor(agent);
		//			statement.setId(UUID.randomUUID().toString());
					statement.setVerb(verb);
					statement.setObject(activity);

					String publishedId = client.postStatement(statement)
					log.debug "--- publishedId=${publishedId}"
				}
				catch(e) {
					log.error e
				}
			}
			else {
				log.debug("not publishing to LRS since endpoint URL, user or password is not configured")
			}
		}
	}

	void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof de.httc.plugins.common.SettingChangedEvent) {
			if (event.source?.key in ["lrsUrl", "lrsUser", "lrsPassword"]) {
				readConfig()
			}
		}
		else if (event instanceof org.springframework.security.authentication.event.AuthenticationSuccessEvent || event instanceof org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent) {
			// log login event
			log(new Verb("https://w3id.org/xapi/adl/verbs/logged-in", ["en-US" : "logged-in"]), grailsLinkGenerator.link(absolute:true, uri:"/"))
		}
/*
		else if (event instanceof org.grails.datastore.mapping.engine.event.SaveOrUpdateEvent) {
			event.properties?.each {
				println it
			}
		}
		else {
			//log.info "--- ignoring event ${event}"
		}
*/
	}

	private def createActivity(String activityId) {
		Activity activity = new Activity()
		activity.setId(activityId)
		return activity
	}

	private def readConfig() {
		def lrsUrl = settingService.getValue("lrsUrl")
		def lrsUser = settingService.getValue("lrsUser")
		def lrsPassword = settingService.getValue("lrsPassword")
		if (lrsUrl && lrsUser && lrsPassword) {
			client = new StatementClient(lrsUrl, lrsUser, lrsPassword)
		}
		else {
			client = null
		}
		log.info "Using URL='${lrsUrl}', USER='${lrsUser}', PASS='${lrsPassword}'"
	}
}
