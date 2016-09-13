package de.httc.plugins.lrs

import org.springframework.context.ApplicationListener
import grails.transaction.Transactional
import de.httc.plugins.user.User
import de.httc.plugins.common.Setting
import de.httc.plugins.common.SettingChangedEvent
import gov.adlnet.xapi.model.*
import gov.adlnet.xapi.client.StatementClient

@Transactional(readOnly = true)
class LrsService implements ApplicationListener<SettingChangedEvent> {
	def springSecurityService
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

	/*
				ActivityDefinition ad = new ActivityDefinition();
				ad.setChoices(new ArrayList<InteractionComponent>());
				InteractionComponent ic = new InteractionComponent();
				ic.setId("http://example.com");
				ic.setDescription(new HashMap<String, String>());
				ic.getDescription().put("en-US", "test");
				ad.getChoices().add(ic);
				ad.setInteractionType("choice");
				ad.setMoreInfo("http://example.com");
				a.setDefinition(ad);
	*/
				String publishedId = client.postStatement(statement)
				log.debug "--- publishedId=${publishedId}"
			}
			catch(e) {
				log.error e
			}
		}
		else {
			log.warn("not publishing to LRS since URL, user or password is not configured")
		}
	}

	void onApplicationEvent(SettingChangedEvent event) {
		if (event.source?.key in ["lrsUrl", "lrsUser", "lrsPassword"]) {
			readConfig()
		}
	}

	private def createActivity(String activityId) {
		Activity activity = new Activity()
		activity.setId(activityId)
		return activity
	}

	private def readConfig() {
		def lrsUrl = Setting.getValue("lrsUrl")
		def lrsUser = Setting.getValue("lrsUser")
		def lrsPassword = Setting.getValue("lrsPassword")
		if (lrsUrl && lrsUser && lrsPassword) {
			client = new StatementClient(lrsUrl, lrsUser, lrsPassword)
		}
		else {
			client = null
		}
		log.info "Using URL='${lrsUrl}', USER='${lrsUser}', PASS='${lrsPassword}'"
	}
}
