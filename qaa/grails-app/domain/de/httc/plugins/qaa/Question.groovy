package de.httc.plugins.qaa

import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.user.User
import de.httc.plugins.repository.Asset
import java.util.UUID
import java.util.SortedSet

class Question extends Commentable {
    def springSecurityService
    static searchable = {
		all = [analyzer: 'german']
        except = ['creator', 'dateCreated', 'lastUpdated', 'reference', 'acceptedAnswer', 'ratingUsers']
        title boost:2.0
        metadata index:"not_analyzed"
        answers component:true
        comments component:true
        attachments component:true
    }

    static constraints = {
    	title blank:false
    	text blank:false
    	acceptedAnswer nullable:true
        reference nullable:true
    }
    static hasMany = [attachments:Asset, answers:Answer, comments:Comment, ratingUsers:Long, terms:TaxonomyTerm]
    static mapping = {
    	title type:"text"
    	text type:"text"
    	sort "dateCreated":"desc"
    	answers sort:"dateCreated", "id"
		comments sort:"dateCreated", "id"
        metadata type:"text"
    }

    String title
    String text
    User creator
    Date dateCreated
    Date lastUpdated
    Answer acceptedAnswer
    QuestionReference reference
    boolean deleted
    Map<String, String> metadata

//    List<Answer> answers        // defined to get indexed
    SortedSet<Comment> comments       // sorted by Comment.compareTo
    List<Asset> attachments     // defined as list to keep order in which elements got added
    SortedSet<TaxonomyTerm> terms    // defined as list to keep order in which elements got added

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
