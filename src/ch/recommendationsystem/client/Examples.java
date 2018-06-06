package ch.recommendationsystem.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;
import maui.stemmers.FrenchStemmer;
import maui.stemmers.Stemmer;
import maui.stopwords.Stopwords;
import maui.stopwords.StopwordsFrench;

public class Examples {

	private MauiTopicExtractor topicExtractor;
	private MauiModelBuilder modelBuilder;

	public Examples() {
	}

	private void setGeneralOptions() {

		modelBuilder.debugMode = true;

		Stemmer stemmer = new FrenchStemmer();
		Stopwords stopwords = new StopwordsFrench("stopwords_fr.txt");
		String language = "fr";
		String encoding = "UTF-8";
		modelBuilder.stemmer = stemmer;
		modelBuilder.stopwords = stopwords;
		modelBuilder.documentLanguage = language;
		modelBuilder.documentEncoding = encoding;
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

		topicExtractor.debugMode = true;
		topicExtractor.topicsPerDocument = 10;
	}

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

	public void testAutomaticTagging() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		String trainDir = "data/automatic_tagging/train";
		String testDir = "data/automatic_tagging/test";

		String modelName = "test";

		modelBuilder.inputDirectoryName = trainDir;
		modelBuilder.modelName = modelName;

		modelBuilder.minNumOccur = 1;

		HashSet<String> fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();
		String vocabulary = "agrovoc_fr";
		String format = "skos";
		topicExtractor.inputDirectoryName = testDir;
		topicExtractor.modelName = modelName;
		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}

	public static void main(String[] args) throws Exception {

		Date todaysDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss");
		String formattedDate1 = formatter.format(todaysDate);
		Examples exampler;

		exampler = new Examples();
		exampler.testAutomaticTagging();

		todaysDate = new java.util.Date();
		String formattedDate2 = formatter.format(todaysDate);
		System.err.print("Run from " + formattedDate1);
		System.err.println(" to " + formattedDate2);
	}

}