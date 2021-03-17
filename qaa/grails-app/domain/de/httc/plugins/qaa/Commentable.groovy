package de.httc.plugins.qaa

import java.util.UUID

class Commentable {
    static mapping = {
        //tablePerHierarchy false
        id generator: "assigned"
    }
    static constraints = {
      updateTrigger nullable:true
    }

    String id = UUID.randomUUID().toString()
    Long updateTrigger

    public touch() {
      if (updateTrigger == null) {
        updateTrigger = 0
      }
      updateTrigger++
      return this
    }
}
