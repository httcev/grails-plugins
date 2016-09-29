class HttcUserUrlMappings {

	static mappings = {
		"/user/$action?/$id?(.$format)?" { controller = "user"; namespace="admin"; plugin = "httcUser" }
		"/register/$action?" { controller = "register"; plugin = "httcUser" }
	}
}
