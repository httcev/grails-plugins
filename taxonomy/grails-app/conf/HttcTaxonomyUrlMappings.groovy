class HttcTaxonomyUrlMappings {
	static mappings = {
		"/taxonomies/$action?/$id?(.$format)?" { controller = "taxonomies"; plugin = "httcTaxonomy" }
	}
}
