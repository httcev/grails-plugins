package de.httc.plugins.qaa

import de.httc.plugins.user.User

class Comment {
/*    
    static searchable = {
    	only = ["text", "deleted"]
    }
*/    
    static constraints = {
        text blank:false
    }
    static mapping = {
        text type:"text"
    }

    String text
    User creator
    Date dateCreated
    Date lastUpdated
    boolean deleted
}
