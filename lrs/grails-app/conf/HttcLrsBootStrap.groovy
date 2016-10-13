import de.httc.plugins.common.Setting
import de.httc.plugins.lrs.LrsService

class HttcLrsBootStrap {
	def lrsService
	def settingService

	def init = { servletContext ->
		if (!settingService.exists("lrsUrl")) {
			new Setting(key:"lrsUrl", value:null, required:false, multiline:false, weight:10.0).save()
			new Setting(key:"lrsUser", value:null, required:false, multiline:false, weight:10.1).save()
			new Setting(key:"lrsPassword", value:null, required:false, multiline:false, weight:10.2).save()
		}
		lrsService.readConfig()
	}
	def destroy = {
	}
}
