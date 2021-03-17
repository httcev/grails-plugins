class HttcUserUrlMappings {

	static mappings = {
		"/user/$action?/$id?(.$format)?" { controller = "user"; plugin = "httcUser" }
		"/register/$action?" { controller = "register"; plugin = "httcUser" }
	}
}
