package de.httc.plugins.lrs

import org.springframework.context.ApplicationListener
import grails.transaction.Transactional
import de.httc.plugins.common.Setting
import de.httc.plugins.common.SettingChangedEvent

@Transactional(readOnly = true)
class LrsService implements ApplicationListener<SettingChangedEvent> {
	def lrsUrl, lrsUser, lrsPassword

	def log() {
		println request
	}

	void onApplicationEvent(SettingChangedEvent event) {
		if (event.source?.key in ["lrsUrl", "lrsUser", "lrsPassword"]) {
			readConfig()
		}
	}

	private def readConfig() {
		lrsUrl = Setting.getValue("lrsUrl")
		lrsUser = Setting.getValue("lrsUser")
		lrsPassword = Setting.getValue("lrsPassword")

		println "--- read config: URL='${lrsUrl}', USER='${lrsUser}', PASS='${lrsPassword}'"
	}
}
