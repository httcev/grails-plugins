package de.httc.plugins.competence

import java.util.Collection;
import de.httc.plugins.competence.Competence.Source

class ExpertService {

    public Collection<Competence> findBestCompetences(String primaryTermId, int limit=10) {
	return Competence.createCriteria().list() {
	    eq("primaryTermId", primaryTermId)
	    and { gt("level", 0) }
	    maxResults(limit)
	    order("level", "desc")
	}
    }
}
