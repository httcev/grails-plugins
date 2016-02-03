package de.httc.plugins.qaa

import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.user.User
import de.httc.plugins.repository.Asset

class Question {
/*    
    static searchable = {
    	except = ["attachments", "version"]
    	dateCreated index:"not_analyzed"
    	lastUpdated index:"not_analyzed"
    	answers component:true
    	comments component:true
    	terms component:true
    	//creator component:true
    	metadata component:true
    }
*/    
    static constraints = {
    	title blank:false
    	text blank:false
    	acceptedAnswer nullable:true
    }
    static hasMany = [attachments:Asset, answers:Answer, comments:Comment, ratingUsers:User, terms:TaxonomyTerm, metadata:Metadata]
    static mapping = {
    	attachments cascade:"all-delete-orphan"
    	comments cascade:"all-delete-orphan"
    	title type:"text"
    	text type:"text"
    	sort "dateCreated":"desc"
    	answers sort:"dateCreated", "id"
    }

    String title
    String text
    User creator
    Date dateCreated
    Date lastUpdated
    Answer acceptedAnswer

    List<Answer> answers		 // defined to get indexed
    List<Comment> comments       // defined as list to keep order in which elements got added
    List<Asset> attachments // defined as list to keep order in which elements got added
    List<TaxonomyTerm> terms // defined as list to keep order in which elements got added
}

