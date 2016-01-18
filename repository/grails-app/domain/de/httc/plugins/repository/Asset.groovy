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

    def transient repositoryService
//    def transient imageService

	static searchable = {
		//all = [analyzer: 'german']
        only = ['name', 'mimeType', 'indexText', 'deleted', 'type']
        //name boost:3.0
        //description boost:2.0
//        props index:"not_analyzed"
    }
    
    static constraints = {
    	// Limit upload file size to 100MB
        content maxSize: 1024 * 1024 * 100, nullable:true
        indexText nullable: true
        creator nullable:true
        type nullable:true
    }
    static mapping = {
        id generator: "assigned"
		content lazy: true
        indexText lazy: true, type:"text"
    }

    String id = UUID.randomUUID().toString()
    String name
    String mimeType
    String type
    boolean deleted
    Map<String, String> props
    byte[] content
    String indexText

    User creator
    Date dateCreated
    Date lastUpdated

/*
    def beforeInsert() {
        beforeUpdate()
    }

    def beforeUpdate() {
        // remove GPS tags from EXIF metadata if this is an image
        if (content?.length && mimeType?.startsWith("image/jp")) {
            content = imageService.removeExifGPS(content)
        }
    }
*/

    def getUrl() {
        repositoryService.createEncodedLink(this)
    }

    def afterInsert() {
        // withNewSession is needed, otherwise hibernate will silently NOT persist. STRANGE behaviour!
        Asset.withNewSession {
            repositoryService.deleteRepositoryFile(this)
        }
    }

    def afterUpdate() {
        afterInsert()
    }

    def afterDelete() {
        afterInsert()
    }

}
