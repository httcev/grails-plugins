package de.httc.plugins.repository

@grails.validation.Validateable
class CreateAssetCommand implements Serializable {
    private static final long serialVersionUID = 42L;

    static constraints = {
        // Limit upload file size to 100MB
        content maxSize: 1024 * 1024 * 100, nullable:true
        name blank:false
        indexText nullable: true
        typeLabel nullable: true
    }

    String name
    String mimeType
    String typeLabel = "learning-resource"
    String indexText
    byte[] content
    // need to define this as HashMap, otherwise grails instantiates a GrailsParameterMap which is not serializable
    HashMap<String, String> props
}
