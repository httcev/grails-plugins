package de.httc.plugins.qaa

import java.util.UUID

class QuestionReference {
    static mapping = {
        //tablePerHierarchy false
        id generator: "assigned"
    }

    String id = UUID.randomUUID().toString()
}

