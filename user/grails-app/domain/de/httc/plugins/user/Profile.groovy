package de.httc.plugins.user

class Profile {
	static constraints = {
		firstName blank: false, unique: "lastName"
        lastName blank: false
		company nullable: true
		phone nullable: true
        mobile nullable: true
        lastUpdated nullable: true // needed for downward compatibility
		// photo max size 2 MB
		photo maxSize: 1024 * 1024 * 2, nullable: true
	}
    static belongsTo = [user:User]

   	String firstName
    String lastName
    String company
    String phone
    String mobile
    byte[] photo
    Date lastUpdated

    def getDisplayName() {
        firstName + " " + lastName
    }

    def getDisplayNameReverse() {
        lastName + ", " + firstName
    }
}
