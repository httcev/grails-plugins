package de.httc.plugins.taxonomy

import grails.transaction.Transactional


class TaxonomyService {
	def esaService

	def suggestTerms(text) {
		return suggestTerms(text, null)
	}

	def suggestTerms(text, primary) {
		def terms
		if (primary) {
			terms = TaxonomyTerm.where { taxonomy.type == Taxonomy.Type.PRIMARY }.list()
		}
		else {
			terms = TaxonomyTerm.list()
		}
		if (terms) {
			try {
				def cv = esaService.extractEsaVector(text, "de", true)
				return esaService.getRelevantObjects(cv, 5, 0.1d, terms)
			}
			catch(e) {
				log.warn e
			}
		}
	}
}
