package de.httc.plugins.user

class User implements Serializable {
	private static final long serialVersionUID = 1

	def springSecurityService

	static mapping = {
		password column: 'passwd'
		profile cascade: 'all'
	}
	static constraints = {
		username blank: false, unique: true
		password blank: false
		email email: true, blank: false
	}
	static hasOne = [profile:Profile]

	String username
	String password
	String email
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	boolean termsOfUseAccepted
    Date lastUpdated

	User(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	@Override
	int hashCode() {
		username?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof User && other.username == username)
	}

	@Override
	String toString() {
		username
	}

	Set<Role> getAuthorities() {
		// findAllByUser with a non-persisted user object produces error messages, so early-out here.
		if (!id) {
			return null
		}
		try {
			return UserRole.findAllByUser(this)*.role
		}
		catch(Exception e) {
			log.warn e
			return null
		}
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	def beforeDelete() {
		UserRole.removeAll(this)
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
