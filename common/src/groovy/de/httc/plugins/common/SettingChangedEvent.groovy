package de.httc.plugins.common

import org.springframework.context.ApplicationEvent

class SettingChangedEvent extends ApplicationEvent {
	SettingChangedEvent(Object source) {
		super(source)
	}
}
