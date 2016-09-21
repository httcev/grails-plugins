package de.httc.plugins.taxonomy


import de.httc.plugins.esa.EsaComparable
import edu.kit.aifb.concept.IConceptVector;

class TaxonomyTerm extends TaxonomyNode implements EsaComparable {
	def transient esaService

	static transients = ["esaVector"]
	static constraints = {
		esaVectorData maxSize:1024*1024*50, nullable:true
		taxonomy nullable:true
		parent nullable:false
	}

	Taxonomy taxonomy
	TaxonomyNode parent
	IConceptVector esaVector
	byte[] esaVectorData

	def beforeInsert() {
		updateTaxonomyReference()
		updateEsaVector()
	}

	def beforeUpdate() {
		if (isDirty('label') || isDirty('parent')) {
			updateTaxonomyReference()
			updateEsaVector()
		}
	}
/*
	def setParent(TaxonomyNode parent) {
		this.parent = parent
		updateTaxonomyReference()
	}
*/
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
			while (cursor.hasProperty("parent") && cursor.parent != null) {
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

	private void updateTaxonomyReference() {
		def root = parent
		while (root?.hasProperty("parent") && root.parent != null) {
			root = root.parent
		}
		if (!(root instanceof Taxonomy)) {
			log.warn("couldn't infer taxonomy into term ${this.label}, root=${root?.properties}")
		}
		else {
			taxonomy = root
		}

		children?.each {
			it.updateTaxonomyReference()
		}
	}
}
