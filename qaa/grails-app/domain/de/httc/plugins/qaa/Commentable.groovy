package de.httc.plugins.qaa

import java.util.UUID

class Commentable {
    static mapping = {
        //tablePerHierarchy false
        id generator: "assigned"
    }

    String id = UUID.randomUUID().toString()
}
