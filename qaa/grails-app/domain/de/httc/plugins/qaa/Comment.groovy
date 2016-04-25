package de.httc.plugins.qaa

import de.httc.plugins.user.User
import java.util.UUID

class Comment implements Comparable {
	static searchable = {
		root false
		all = [analyzer: 'german']
		except = ['creator', 'dateCreated', 'lastUpdated', 'reference']
	}
	static constraints = {
		text blank:false
	}
	static mapping = {
		id generator: "assigned"
		text type:"text"
	}
	static belongsTo = [reference:Commentable]

	String id = UUID.randomUUID().toString()
	String text
	User creator
	Date dateCreated
	Date lastUpdated
	boolean deleted

	/*
	used in hasMany relationships of question/answer
	*/
	int compareTo(obj) {
		if (dateCreated && obj.dateCreated) {
			return dateCreated.compareTo(obj.dateCreated)
		}
		else {
			return id.compareTo(obj.id)
		}
	}
}
