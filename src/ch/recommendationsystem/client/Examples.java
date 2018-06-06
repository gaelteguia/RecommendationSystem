package ch.recommendationsystem.client;

import gnu.trove.TIntHashSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;
import maui.stemmers.FrenchStemmer;
import maui.stemmers.PorterStemmer;
import maui.stemmers.Stemmer;
import maui.stopwords.Stopwords;
import maui.stopwords.StopwordsEnglish;
import maui.stopwords.StopwordsFrench;

import org.wikipedia.miner.model.Wikipedia;
//import org.wikipedia.miner.util.ProgressNotifier;
import org.wikipedia.miner.util.text.CaseFolder;
import org.wikipedia.miner.util.text.TextProcessor;

/**
 * Demonstrates how to use Maui for three types of topic indexing <br>
 * 1. Keyphrase extraction - extracting significant phrases from the document,
 * also suitable for automatic tagging. <br>
 * 2. Term assignment - indexing documents with terms from a controlled
 * vocabulary in SKOS or text format. <br>
 * 3. Indexing with Wikipedia - indexing documents with terms from Wikipedia,
 * also suitable for keyphrase extraction and tagging, or any case where there
 * is no con trolled vocabulary available, but consistency is required.
 * 
 * @author Olena Medelyan (olena@cs.waikato.ac.nz)
 * 
 */
public class Examples {

	private MauiTopicExtractor topicExtractor;
	private MauiModelBuilder modelBuilder;

	private Wikipedia wikipedia;

	private String server;
	private String database;
	private String dataDirectory;
	private boolean cache = false;

	public Examples(String server, String database, String dataDirectory, boolean cache) throws Exception {
		this.server = server;
		this.database = database;
		this.dataDirectory = dataDirectory;
		this.cache = cache;
		// loadWikipedia();
	}

	public Examples() {
	}

	// private void loadWikipedia() throws Exception {
	//
	// wikipedia = new Wikipedia(server, database, "root", null);
	//
	// TextProcessor textProcessor = new CaseFolder();
	//
	// File dataDir = new File(dataDirectory);
	//
	// if (cache) {
	// ProgressNotifier progress = new ProgressNotifier(5);
	// // cache tables that will be used extensively
	// TIntHashSet validPageIds = wikipedia.getDatabase().getValidPageIds(dataDir,
	// 2, progress);
	// wikipedia.getDatabase().cachePages(dataDir, validPageIds, progress);
	// wikipedia.getDatabase().cacheAnchors(dataDir, textProcessor, validPageIds, 2,
	// progress);
	// wikipedia.getDatabase().cacheInLinks(dataDir, validPageIds, progress);
	// wikipedia.getDatabase().cacheGenerality(dataDir, validPageIds, progress);
	// }
	// }

	/**
	 * Sets general parameters: debugging printout, language specific options like
	 * stemmer, stopwords.
	 * 
	 * @throws Exception
	 */
	private void setGeneralOptions() {

		modelBuilder.debugMode = true;
		modelBuilder.wikipedia = wikipedia;

		// language specific options
		Stemmer stemmer = new FrenchStemmer();
		Stopwords stopwords = new StopwordsFrench("stopwords_fr.txt");
		String language = "fr";
		String encoding = "UTF-8";
		modelBuilder.stemmer = stemmer;
		modelBuilder.stopwords = stopwords;
		modelBuilder.documentLanguage = language;
		modelBuilder.documentEncoding = encoding;
		// vocabulary to use for term assignment
		String vocabulary = "agrovoc_fr";
		String format = "skos";
		modelBuilder.stemmer = stemmer;
		modelBuilder.stopwords = stopwords;
		modelBuilder.documentLanguage = language;
		modelBuilder.documentEncoding = encoding;

		modelBuilder.vocabularyFormat = format;
		modelBuilder.vocabularyName = vocabulary;
		topicExtractor.stemmer = stemmer;
		topicExtractor.stopwords = stopwords;
		topicExtractor.documentLanguage = language;

		/*
		 * specificity options modelBuilder.minPhraseLength = 1;
		 * modelBuilder.maxPhraseLength = 5;
		 */

		topicExtractor.debugMode = true;
		topicExtractor.topicsPerDocument = 10;
		topicExtractor.wikipedia = wikipedia;
	}

	/**
	 * Set which features to use
	 */
	private void setFeatures() {
		modelBuilder.setBasicFeatures(true);
		modelBuilder.setKeyphrasenessFeature(true);
		modelBuilder.setFrequencyFeatures(true);
		modelBuilder.setPositionsFeatures(true);
		modelBuilder.setLengthFeature(true);
		modelBuilder.setNodeDegreeFeature(true);
		modelBuilder.setBasicWikipediaFeatures(false);
		modelBuilder.setAllWikipediaFeatures(false);
	}

	/**
	 * Demonstrates how to perform automatic tagging. Also applicable to keyphrase
	 * extraction.
	 * 
	 * @throws Exception
	 */
	public void testAutomaticTagging() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		// Directories with train & test data
		String trainDir = "data/automatic_tagging/train";
		String testDir = "data/automatic_tagging/test";

		// name of the file to save the model
		String modelName = "test";

		// Settings for the model builder
		modelBuilder.inputDirectoryName = trainDir;
		modelBuilder.modelName = modelName;

		// change to 1 for short documents
		modelBuilder.minNumOccur = 1;

		// Run model builder
		HashSet<String> fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();
		String vocabulary = "agrovoc_fr";
		String format = "skos";
		// Settings for topic extractor
		topicExtractor.inputDirectoryName = testDir;
		topicExtractor.modelName = modelName;
		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;
		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}

	/**
	 * Demonstrates how to perform term assignment. Applicable to any vocabulary in
	 * SKOS or text format.
	 * 
	 * @throws Exception
	 */
	public void testTermAssignment() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		// Directories with train & test data
		String trainDir = "data/term_assignment/train";
		String testDir = "data/term_assignment/test";

		// Vocabulary
		String vocabulary = "agrovoc_sample";
		String format = "skos";

		// name of the file to save the model
		String modelName = "test";
		HashSet<String> fileNames;

		// Settings for the model builder
		modelBuilder.inputDirectoryName = trainDir;
		modelBuilder.modelName = modelName;
		modelBuilder.vocabularyFormat = format;
		modelBuilder.vocabularyName = vocabulary;
		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;

		// Run model builder
		fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		// Settings for topic extractor
		topicExtractor.inputDirectoryName = testDir;
		topicExtractor.modelName = modelName;
		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;

		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);

	}

	/**
	 * Demonstrates how to perform topic indexing with Wikipedia.
	 * 
	 * @throws Exception
	 */
	public void testIndexingWithWikipedia() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		// Directories with train & test data
		String trainDir = "data/wikipedia_indexing/test";
		String testDir = "/Users/alyona/Documents/corpora/term_assignment/FAO_780/1doc2";

		// Vocabulary
		String vocabulary = "wikipedia";

		// name of the file to save the model
		String modelName = "test";
		HashSet<String> fileNames;

		// Settings for the model builder
		modelBuilder.inputDirectoryName = trainDir;
		modelBuilder.modelName = modelName;
		modelBuilder.vocabularyName = vocabulary;

		// Run model builder
		fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		// // Settings for topic extractor
		// topicExtractor.setDirName(testDir);
		// topicExtractor.setModelName(modelName);
		// topicExtractor.setVocabularyName(vocabulary);
		//
		// // Run topic extractor
		// topicExtractor.loadModel();
		// fileNames = topicExtractor.collectStems();
		// topicExtractor.extractKeyphrases(fileNames);
	}

	/**
	 * Main method for running the three types of topic indexing. Comment out the
	 * required one.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String fileName = "C:\\glassfish4\\glassfish\\domains\\domain1\\config\\contents.key";

		// read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			// data = stream.collect(Collectors.toList());

			stream.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// String mode = args[0];
		//
		// if (!mode.equals("tagging") && !mode.equals("term_assignment") &&
		// !mode.equals("indexing_with_wikipedia")) {
		// throw new Exception("Choose one of the three modes: tagging, term_assignment
		// or indexing_with_wikipedia");
		// }

		Date todaysDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss");
		String formattedDate1 = formatter.format(todaysDate);
		Examples exampler;

		// if (mode.equals("tagging")) {
		exampler = new Examples();
		exampler.testAutomaticTagging();
		// } else if (mode.equals("term_assignment")) {
		// exampler = new Examples();
		// exampler.testTermAssignment();
		// } else if (mode.equals("indexing_with_wikipedia")) {
		// // Access to Wikipedia
		// String server = "localhost";
		// String database = "database";
		// String dataDirectory = "path/to/data/directory";
		// boolean cache = false;
		// exampler = new Examples(server, database, dataDirectory, cache);
		// exampler.testIndexingWithWikipedia();
		// }

		todaysDate = new java.util.Date();
		String formattedDate2 = formatter.format(todaysDate);
		System.err.print("Run from " + formattedDate1);
		System.err.println(" to " + formattedDate2);
	}

}