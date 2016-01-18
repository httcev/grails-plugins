package de.httc.plugins.pushNotification

import de.httc.plugins.user.User

class PushToken {
    static belongsTo = User
    static mapping = {
        id column: "user_id"
    }
 
    String token
    Date lastUpdated
}
