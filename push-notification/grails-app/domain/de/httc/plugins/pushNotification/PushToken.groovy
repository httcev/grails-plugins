package de.httc.plugins.pushNotification

import de.httc.plugins.user.User

class PushToken {
    static belongsTo = [user : User]
    static mapping = {
        user column:"id", insertable:false, updateable:false
    }

    String token
    Date lastUpdated
}
