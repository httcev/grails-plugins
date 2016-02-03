package de.httc.plugins.qaa

import de.httc.plugins.user.User
import de.httc.plugins.repository.Asset

class Answer {
/*	
    static searchable = {
    	only = [
    	    "text",
    	    //"creator",
    	    "comments"
    	]
    	comments component:true
//    	creator component:true
    }
*/
    static hasMany = [attachments:Asset, comments:Comment, ratingUsers:Long]
    static belongsTo = [question:Question]
    static constraints = { }
    static mapping = {
    	attachments cascade:"all-delete-orphan"
    	comments cascade:"all-delete-orphan"
    	text type:"text"
    }

    String text
    User creator
    Date dateCreated
    Date lastUpdated
    List<Comment> comments       // defined as list to keep sort order
    List<Asset> attachments // defined as list to keep sort order
}
