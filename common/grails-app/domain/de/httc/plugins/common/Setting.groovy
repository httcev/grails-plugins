package de.httc.plugins.common

class Setting {
	def grailsApplication

	static hasMany = [possibleValues:String]
	static mapping = {
		value type: "text"
		weight defaultValue: "1.0" // must be unique, otherwise we'll possibly get databinding errors (mix ups due to different sorting when rendering vs binding)
		exported defaultValue: false
	}
	static constraints = {
		key unique:true
		weight unique:true
		possibleValues nullable:true
		value nullable: true, validator: { val, obj ->
			if(obj.required && !val) {
				return "value for ${obj.key} is required"
			}
			if(obj.required && obj.possibleValues?.size() > 0 && !obj.possibleValues.contains(val)) {
				return "value '${val}' for ${obj.key} must be one of: " + obj.possibleValues
			}
		}
	}

	String key
	String value
	List<String> possibleValues
	String prefix ="settings" // i18n message code prefix
	boolean required
	boolean multiline
	Boolean exported // exported to client app as config value
	float weight
	Date lastUpdated

	def afterUpdate() {
		publishChangedEvent()
	}

	def afterInsert() {
		publishChangedEvent()
	}

	def afterDelete() {
		publishChangedEvent()
	}

	private void publishChangedEvent() {
		grailsApplication.mainContext.publishEvent(new SettingChangedEvent(this))
	}
}
