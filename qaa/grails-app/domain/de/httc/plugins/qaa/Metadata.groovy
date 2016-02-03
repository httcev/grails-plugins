package de.httc.plugins.qaa

class Metadata {
//    static searchable = { only = ["values"]}
    static constraints = {
    }

    String key
    Map<String, String> values = new HashMap<String, String>()
}

