package de.httc.plugins.repository

import de.httc.plugins.taxonomy.TaxonomyTerm
import de.httc.plugins.user.User
import java.util.UUID

class AssetContent {
    static belongsTo = [asset:Asset]
    static constraints = {
    	// Limit upload file size to 100MB
        data maxSize: 1024 * 1024 * 100
    }
    byte[] data
}
