package de.httc.plugins.common

import grails.transaction.Transactional

@Transactional(readOnly = true)
class SettingService {
	def exists(String key) {
		return Setting.findByKey(key) != null
	}

	def getValue(String key) {
		return Setting.findByKey(key)?.value
	}
}
