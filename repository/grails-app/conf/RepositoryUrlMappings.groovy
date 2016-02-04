class RepositoryUrlMappings {
	static mappings = {
        name viewAsset: "/v/$id"{
            controller = "asset"
            action = "viewAsset"
            constraints {
                // apply constraints here
            }
        }
        name viewAssetFile: "/v/$id/$file**"{
            controller = "asset"
            action = "viewAsset"
            constraints {
                // apply constraints here
            }
        }
        "/asset/$action?/$id?(.$format)?" {
            controller = "asset"
            plugin = "repository"
            constraints {
                action(matches:/index|show/)
            }
        }
        "/asset/$action?/$id?(.$format)?" {
            controller = "asset"
            plugin = "repository"
            namespace = "admin"
            constraints {
                action(matches:/create|update|delete|edit/)
            }
        }
	}
}
