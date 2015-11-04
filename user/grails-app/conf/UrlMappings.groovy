class UrlMappings {

	static mappings = {
        "/admin/$controller/$action?/$id?(.$format)?" { namespace = "admin" }
        //"/$namespace/$controller/$action?/$id?(.$format)?" { }
        "/register/$action?" {
            // need to define this, otherwise RegisterController complains about the 'format' parameter
            controller = "register"
        }
        "/$controller/$action?/$id?(.$format)?" { }
	}
}
