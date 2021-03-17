import de.httc.plugins.common.Setting
import de.httc.plugins.lrs.LrsService

class HttcQaaBootStrap {
	def settingService

	def init = { servletContext ->
		if (!settingService.exists("questionChannelGeneral")) {
			def valueList = ["broadcast", "company", "groups"]
			new Setting(key:"questionChannelGeneral", value:"groups", possibleValues:valueList, required:true, multiline:false, exported:false, weight:9.13).save()
		}
		if (!settingService.exists("questionChannelTask")) {
			def valueList = ["broadcast", "company", "groups", "creator"]
			new Setting(key:"questionChannelTask", value:"creator", possibleValues:valueList, required:true, multiline:false, exported:false, weight:9.14).save()
		}
	}
	def destroy = {
	}
}
