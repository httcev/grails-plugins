package de.httc.plugins.qaa

import de.httc.plugins.user.User
import de.httc.plugins.repository.Asset
import java.util.UUID
import java.util.SortedSet

class Answer extends Commentable {
    def springSecurityService
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
    boolean deleted
    Date dateCreated
    Date lastUpdated
    SortedSet<Comment> comments       // sorted by Comment.compareTo
    List<Asset> attachments // defined as list to keep sort order

    def getRated() {
        def userId = springSecurityService.currentUser?.id
        if (userId != null && ratingUsers) {
            return ratingUsers.contains(userId)
        }
        return false
    }

    def setRated(boolean rated) {
        def userId = springSecurityService.currentUser?.id
        if (userId != null) {
            if (rated) {
                addToRatingUsers(userId)
            }
            else {
                removeFromRatingUsers(userId)
            }
        }
    }

    def getRating() {
        if (ratingUsers) {
            return ratingUsers.size()
        }
        else {
            return 0
        }
    }
}
