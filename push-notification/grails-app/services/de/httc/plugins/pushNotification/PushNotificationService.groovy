package de.httc.plugins.pushNotification

import de.httc.plugins.user.User
import groovy.json.JsonBuilder

class PushNotificationService {
	def settingService

	def sendPushNotification(User target, message) {
		def gcmUrl = settingService.getValue("gcmUrl")
		def gcmApiKey = settingService.getValue("gcmApiKey")
		if (gcmUrl && gcmApiKey) {
			// decouple from current thead
			PushToken.async.task {
				try {

					def pushToken = PushToken.get(target.id)
					if (!pushToken) {
						log.warn "No push notification token registered for user ${target.username}. Not sending push notification."
						return
					}
					if (!message.notId && message.collapse_key) {
						message.notId = message.collapse_key.hashCode()
						log.debug "Appending notification id to message based on its collapse key: ${message.notId}"
					}
					log.info "Sending push notification to user ${target.username}: " + message
					log.info "Using GCM service url '${gcmUrl}'"
					URL url = new URL(gcmUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST")
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("Authorization", "key=" + gcmApiKey);
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setDoOutput(true);
					conn.setDoInput(true);

					message.to = pushToken.token
					def builder = new JsonBuilder(message)
					def payload = builder.toString().bytes
					conn.setFixedLengthStreamingMode(payload.length);
					conn.getOutputStream().write(payload)

					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						log.debug(line);
					}
					reader.close();
				}
				catch(e) {
					log.error "Sending push notification failed", e
				}
			}
		}
		else {
			log.warn "PushNotificationService not configured (missing gcm url and/or gcm api key). Not sending push notification."
		}
	}
}
