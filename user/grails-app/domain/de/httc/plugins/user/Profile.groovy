package de.httc.plugins.user

import de.httc.plugins.taxonomy.TaxonomyTerm

class Profile {
	static constraints = {
		firstName blank: false, unique: "lastName"
		lastName blank: false
		company nullable:true, validator: { val, obj ->
			return val == null || val.taxonomy.label == "companies"
		}
		phone nullable: true
		mobile nullable: true
		lastUpdated nullable: true // needed for downward compatibility
		// photo max size 2 MB
		photo maxSize: 1024 * 1024 * 2, nullable: true
		organisations nullable:true, validator: { val, obj ->
			def valid = true
			obj.organisations?.each { organisationTerm ->
				if (!organisationTerm.taxonomy.label == "organisations") {
					valid = false
				}
			}
			return valid
		}
	}
	static belongsTo = [user:User]
	static hasMany = [organisations:TaxonomyTerm]

	String firstName
	String lastName
	TaxonomyTerm company
	String phone
	String mobile
	byte[] photo
	Date lastUpdated
	List<TaxonomyTerm> organisations

	def getDisplayName() {
		firstName + " " + lastName
	}

	def getDisplayNameReverse() {
		lastName + ", " + firstName
	}
}
