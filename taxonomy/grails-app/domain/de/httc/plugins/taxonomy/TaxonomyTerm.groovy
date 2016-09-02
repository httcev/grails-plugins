package de.httc.plugins.taxonomy


import de.httc.plugins.esa.EsaComparable
import edu.kit.aifb.concept.IConceptVector;

class TaxonomyTerm implements EsaComparable, Comparable {
    def transient esaService

    static searchable = { only = ["label"]}

    static belongsTo = [parent:TaxonomyTerm, taxonomy:Taxonomy]
    static hasMany = [children:TaxonomyTerm]
    static transients = ["esaVector"]
    static mapping = {
		//parent cascade:"all-delete-orphan", nullable:true
		//children cascade:"all-delete-orphan"
		id (generator: "assigned")	// this is needed to import taxonomies and terms from sharepoint and keeping the foreign ids.
		label type:"text"
		children cascade: "all-delete-orphan"
    }
    static constraints = {
		esaVectorData(maxSize:1024*1024*50, nullable:true)
		parent nullable:true
		id bindable:true	// this is needed to import taxonomies and terms from sharepoint and keeping the foreign ids.
    }

    String id = UUID.randomUUID().toString()
    String label
    boolean isPrimaryDomain
    List<TaxonomyTerm> children       // defined as list to keep order in which elements got added
    IConceptVector esaVector
    byte[] esaVectorData
    //Date lastUpdated

    def beforeInsert() {
		updateEsaVector()
    }

    def beforeUpdate() {
		if (isDirty('label') || isDirty('parent')) {
		    updateEsaVector()
		}
    }

    /*
    used in hasMany relationships
    */
    int compareTo(obj) {
        label.compareTo(obj.label)
    }

    public IConceptVector getEsaVector() {
		try {
		    if (esaVector == null && esaVectorData != null) {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(esaVectorData));
			esaVector = (IConceptVector) ois.readObject();
			ois.close();
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		return esaVector;
    }

    private void updateEsaVector() {
		// TODO: update ESA vector for children when node label changes or parent changes!
		def text = label
		try {
		    def cursor = this;
		    while (cursor.parent != null) {
				cursor = cursor.parent
				text += " ${cursor.label}"
		    }
		    this.esaVector = esaService.extractEsaVector(text, "de", true)

		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(bos);
		    oos.writeObject(esaVector);
		    oos.close();
		    esaVectorData = bos.toByteArray();
		}
		catch (Exception e) {
		    log.warn "Couldn't extract ESA vector for '${text}'"
		}
    }
}
