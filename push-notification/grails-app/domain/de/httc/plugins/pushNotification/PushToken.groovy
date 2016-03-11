package de.httc.plugins.pushNotification

import de.httc.plugins.user.User

class PushToken {
    static belongsTo = [user : User]
    static mapping = {
		id column: 'user_id', generator: 'foreign', params: [ property: 'user']
		user column:'user_id', insertable: false, updateable: false
    }

    String token
    Date lastUpdated
}
