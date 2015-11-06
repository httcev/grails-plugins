package de.httc.plugins.esa

import org.springframework.beans.factory.InitializingBean
import org.springframework.context.support.ClassPathXmlApplicationContext

import edu.kit.aifb.concept.ConceptVectorSimilarity
import edu.kit.aifb.concept.IConceptIndex
import edu.kit.aifb.concept.IConceptVector
import edu.kit.aifb.concept.scorer.CosineScorer
import edu.kit.aifb.document.TextDocument
import edu.kit.aifb.nlp.Language


class EsaService implements InitializingBean {
	// transactional needs to be set to "false", otherwise TaxnonmyTerm class will not be able to extract vectors during a save operation ("flush during cascade is dangerous...")
	static transactional = false
	private Map<String, IConceptIndex> ESA_INDICES = new HashMap<String, IConceptIndex>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// load esa indices
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(["/*_context.xml"] as String[]);
		long startTime = System.currentTimeMillis();
		for (IConceptIndex index : context.getBeansOfType(IConceptIndex.class).values()) {
			log.info("index size for language '" + index.getLanguage() + "'=" + index.size());
			ESA_INDICES.put(index.getLanguage().toString(), index);
		}
		log.info("loading indices took " + (System.currentTimeMillis() - startTime) + " ms.");
	}

	def IConceptVector extractEsaVector(String source, String sourceLanguage, boolean preprocess) throws Exception {
		IConceptIndex index = ESA_INDICES.get(sourceLanguage);
		if (index == null) {
			throw new Exception("Unsupported language: '" + sourceLanguage + "'");
		}
		if (preprocess) {
			source = preprocessSourceText(source);
		}
		Language language = Language.getLanguage(sourceLanguage);
		TextDocument doc = new TextDocument("text");
		doc.setText("content", language, source);

		// index access is not thread safe
		synchronized (index) {
			return index.getConceptExtractor().extract(doc);
		}
	}

	def List<ObjectSimilarity> getRelevantObjects(final IConceptVector cv, final int maxResults, final double minSimilarity, final Collection<? extends EsaComparable> objectsToCompare) {
		List<ObjectSimilarity> result = new ArrayList<ObjectSimilarity>();
		if (objectsToCompare != null && cv != null) {
			try {
				Set<ObjectSimilarity> similarities = new TreeSet<ObjectSimilarity>();
				// ConceptVectorSimilarity is not thread-safe, so it shouldn't be a member variable
				ConceptVectorSimilarity vectorSimilarity = new ConceptVectorSimilarity(new CosineScorer());
				for (EsaComparable object : objectsToCompare) {
					IConceptVector cv2 = object.getEsaVector();
					if (cv2 != null) {
						double similarity = vectorSimilarity.calcSimilarity(cv, cv2);
						if (!Double.isNaN(similarity) && (similarity >= minSimilarity)) {
							similarities.add(new ObjectSimilarity(object:object, similarity:similarity));
						}
					}
				}
	
				int count = 0;
				Iterator<ObjectSimilarity> it = similarities.iterator();
				while (it.hasNext() && count < maxResults) {
					count++;
					ObjectSimilarity o = it.next();
					result.add(o);
				}
			}
			catch(e) {
				log.warn e
			}
		}
		return result;
	}

	private String preprocessSourceText(String source) {
		// the stemmers in research esa don't cope with "-" and "/" characters, so replace these by whitespace
		source = source.replaceAll("-", " ");
		source = source.replaceAll("/", " ");
		return source;
	}
}

	
class ObjectSimilarity implements Comparable<ObjectSimilarity> {
	Object object
	double similarity

	@Override
	public int compareTo(ObjectSimilarity o) {
		int result = (int) Math.signum(o.similarity - similarity);
		if (0 == result) {
			return hashCode() - o.hashCode();
		}
		else {
			return result;
		}
	}
	
	@Override
	public String toString() {
		return object.toString() + " [${similarity }]"
	}
	
}
