package de.httc.plugins.repository

import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.user.User
import java.util.UUID

class Asset {
	static String PROP_FILENAME = "_filename"
	static String PROP_ANCHOR = "_anchor"
	static String PROP_EXTERNAL_URL = "_externalUrl"
	static String PROP_DESCRIPTION = "_description"

	static hasMany = [categories:TaxonomyTerm]
	static hasOne = [content:AssetContent]

	def repositoryService
	def imageService

	static searchable = {
		all = [analyzer: 'german']
		except = ['content', 'creator', 'dateCreated', 'lastUpdated']
		name boost:2.0
		//description boost:2.0
		//props index:"not_analyzed"
		typeLabel index:"not_analyzed"
	}

	static constraints = {
		// Limit upload file size to 100MB
		content nullable:true
		indexText nullable: true
		creator nullable:true
		typeLabel nullable:true
	}
	static mapping = {
		id generator: "assigned"
		content lazy: true
		indexText lazy: true, type:"text"
		props type:"text"
	}

	String id = UUID.randomUUID().toString()
	String name
	String mimeType
	String typeLabel
	boolean deleted
	Map<String, String> props
	String indexText

	User creator
	Date dateCreated
	Date lastUpdated

	def getDescription() {
		props?.get(PROP_DESCRIPTION)
	}

	def getUrl() {
		repositoryService.createEncodedLink(this)
	}

	def beforeInsert() {
		beforeUpdate()
	}

	def beforeUpdate() {
		// remove GPS tags from EXIF metadata if this is an image
		if (mimeType?.startsWith("image/jp") && content?.data?.length > 0) {
			content.data = imageService.removeExifGPS(content.data)
		}
	}

	def afterInsert() {
		// withNewSession is needed, otherwise hibernate will silently NOT persist. STRANGE behaviour!
//		Asset.withNewSession {
			repositoryService.deleteRepositoryFile(this)
//		}
	}

	def afterUpdate() {
		afterInsert()
	}

	def afterDelete() {
		afterInsert()
	}
}
