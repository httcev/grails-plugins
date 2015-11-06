package de.httc.plugins.competence

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import de.httc.plugins.taxonomy.TaxonomyTerm;

class Interaction {
    def grailsApplication

    static enum Verb {
	CREATE, VIEW, UPDATE, DELETE, SEARCH
    }
    static enum Type {
	QUESTION, ANSWER, COMMENT, MICROLEARNING, ERRORREPORT, RATING, ACCEPTANCE, ATTACHMENT, DOCUMENT, RECOMMENDATION, TAXONOMYRELATION, GUIDEPAGE, LEXICALKNOWLEDGE
    }

    static constraints = {
	subjectId nullable:true
	subjectType nullable:true
	subjectContent nullable:true
	objectId nullable:true
	objectType nullable:true
    }

    String userId
    Verb verb
    String subjectId
    Type subjectType
    String subjectContent
    String objectId
    Type objectType

    boolean processed

    Date dateCreated

    def beforeInsert() {
	// dispatch event
	Interaction.withNewSession {
	    grailsApplication.mainContext.publishEvent(new InteractionEvent(this))
	}
    }
}

class InteractionEvent extends ApplicationEvent {
    InteractionEvent(Interaction source) {
	super(source)
    }
}
