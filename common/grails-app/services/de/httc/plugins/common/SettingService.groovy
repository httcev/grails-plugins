package de.httc.plugins.common

import grails.transaction.Transactional
import org.springframework.context.ApplicationListener
import org.springframework.context.ApplicationEvent

@Transactional(readOnly = true)
class SettingService implements ApplicationListener<ApplicationEvent> {
	// cache is used to avoid problems with spring web-flow plugin
	private Map<String, Setting> cache = new HashMap()

	def exists(String key) {
		return Setting.findByKey(key) != null
	}

	def getValue(String key) {
		Setting result = cache.get(key)
		if (!result) {
			result = Setting.findByKey(key)
			if (result) {
				cache.put(key, result)
			}
		}
		return result?.value
	}

	void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof de.httc.plugins.common.SettingChangedEvent) {
			def key = event.source?.key
			if (key) {
				cache.remove(key)
			}
		}
	}
}
