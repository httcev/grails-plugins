import de.httc.plugins.common.Setting

class HttcPushNotificationBootStrap {
	def settingService

	def init = { servletContext ->
		if (!settingService.exists("gcmUrl")) {
			new Setting(key:"gcmUrl", value:"https://fcm.googleapis.com/fcm/send", required:false, multiline:false, exported:false, weight:12.0).save()
			new Setting(key:"gcmApiKey", value:null, required:false, multiline:false, exported:false, weight:12.1).save()
			new Setting(key:"gcmSenderId", value:null, required:false, multiline:false, exported:false, weight:12.2).save()
		}
		if (!settingService.exists("gcmChannel")) {
			new Setting(key:"gcmChannel", value:null, required:false, multiline:false, exported:false, weight:12.3).save()
		}
	}
	def destroy = {
	}
}
