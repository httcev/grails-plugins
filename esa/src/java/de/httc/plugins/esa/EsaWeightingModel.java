package de.httc.plugins.esa;

import org.terrier.matching.models.Idf;
import org.terrier.matching.models.WeightingModel;

public class EsaWeightingModel extends WeightingModel {
	private static final long serialVersionUID = 0x129837ab412L;
	
	private static double B = 1.1d;
	private static double K_1 = 1.1d;

	@Override
	public String getInfo() {
		return "TF_IDF";
	}

	/*
	 * public final double score(double tf, double docLength) {
	 * // TFIDF
	 * double rtf = tf / docLength;
	 * double idf = Math.log(numberOfDocuments / documentFrequency);
	 * return rtf * idf * keyFrequency;
	 * }
	 */
	/*
	 * public final double score(double tf, double docLength) {
	 * // TFIDF2
	 * double rtf = tf / docLength;
	 * double idf = Math.log(numberOfDocuments / documentFrequency);
	 * return rtf * idf * idf * keyFrequency;
	 * }
	 */
	/*
	 * public final double score(double tf, double docLength) {
	 * // TFIDF3
	 * double rtf = tf / docLength;
	 * double idf = Math.log(numberOfDocuments / documentFrequency);
	 * return rtf * idf * idf * idf * keyFrequency;
	 * }
	 */
	@Override
	public final double score(double tf, double docLength) {
		// LemurTF_IDF -> http://www.cs.cmu.edu/~lemur/1.0/tfidf.ps
//        final double k_1 = 1000d;
//        final double b = 0.5d;
		double Robertson_tf = (K_1 * tf) / (tf + K_1 * ((1.0D - B) + (B * docLength) / averageDocumentLength));
		return keyFrequency * Robertson_tf * Math.pow(Idf.log(numberOfDocuments / documentFrequency), 2D);
	}
	
	public static void setB(double b) {
		B = b;
	}
	
	public static void setK_1(double k_1) {
		K_1 = k_1;
	}

	/*
	 * public final double score(double tf, double docLength) {
	 * // BM25
	 * double k_1 = 2d;
	 * double b = 0.75d;
	 * 
	 * double idf = Math.log(((numberOfDocuments - documentFrequency) + 0.5D) / (documentFrequency + 0.5D));
	 * double K = k_1 * ((1.0D - b) + (b * docLength) / averageDocumentLength) + tf;
	 * return idf * (((k_1 + 1.0D) * tf) / K) * keyFrequency;
	 * }
	 */

	@Override
	public final double score(double tf, double docLength, double documentFrequency, double termFrequency, double keyFrequency) {
		throw new RuntimeException("oh here");
		// double rtf = tf / docLength;
		// double idf = Math.log(numberOfDocuments / documentFrequency);
		// return rtf * idf * idf * idf * keyFrequency;
	}
}
