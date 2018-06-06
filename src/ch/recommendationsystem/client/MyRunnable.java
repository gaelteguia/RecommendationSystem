package ch.recommendationsystem.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.wikipedia.miner.model.Wikipedia;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import maui.main.MauiModelBuilder;
import maui.main.MauiTopicExtractor;
import maui.stemmers.FrenchStemmer;
import maui.stemmers.Stemmer;
import maui.stopwords.Stopwords;
import maui.stopwords.StopwordsFrench;

public class MyRunnable implements Runnable {
	final static String API_URI = "https://bdper.plandetudes.ch/api/v1/ressources/e-media/";
	// final static String API_URI =
	// "https://bdper.plandetudes.ch/api/v1/mer/sn-cycle3/";

	private static MauiTopicExtractor topicExtractor;
	private static MauiModelBuilder modelBuilder;

	// private static Wikipedia wikipedia;

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
	private static void setGeneralOptions() {

		modelBuilder.debugMode = true;
		// modelBuilder.wikipedia = wikipedia;

		// language specific options
		Stemmer stemmer = new FrenchStemmer();
		Stopwords stopwords = new StopwordsFrench("data\\stopwords\\stopwords_fr.txt");
		String language = "fr";
		String encoding = "UTF-8";
		// vocabulary to use for term assignment
		String vocabulary = "data\\vocabularies\\agrovoc_fr.rdf.gz";
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

		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;

		/*
		 * specificity options modelBuilder.minPhraseLength = 1;
		 * modelBuilder.maxPhraseLength = 5;
		 */

		topicExtractor.debugMode = true;
		topicExtractor.topicsPerDocument = 10;
		// topicExtractor.wikipedia = wikipedia;
	}

	/**
	 * Set which features to use
	 */
	private static void setFeatures() {
		modelBuilder.setBasicFeatures(true);
		modelBuilder.setKeyphrasenessFeature(true);
		modelBuilder.setFrequencyFeatures(true);
		modelBuilder.setPositionsFeatures(true);
		modelBuilder.setLengthFeature(true);
		modelBuilder.setNodeDegreeFeature(true);
		modelBuilder.setBasicWikipediaFeatures(false);
		modelBuilder.setAllWikipediaFeatures(false);

	}

	static String getContent(String URI) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		URL myUrl = new URL(URI);
		HttpsURLConnection conn = (HttpsURLConnection) myUrl.openConnection();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String message = org.apache.commons.io.IOUtils.toString(br);

		br.close();
		return message;
	}

	public void run() {
		System.out.println("MyRunnable running");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(getContent(API_URI));

			// for (Address s : listeAddresss) {
			// s.setWeather(new Weather(RemoteFetch.getWeather(s.toString())));
			// // s.setPhone("YYYYYY");
			// s = addressDao.update(s);
			//
			// }
			// System.out.println("timer: " + websiteDao.setContent());
			// System.out.println("timer: " + addressDao.setWeather());

			// Indexer indexer = new Indexer();
			// String crawlStorageFolder = "/data/crawl/root";
			String crawlStorageFolder = Files.createTempDir().getAbsolutePath();
			int numberOfCrawlers = 1;
			int maxDownloadSize = 104857600;
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);
			config.setIncludeBinaryContentInCrawling(true);
			config.setPolitenessDelay(100);
			config.setMaxDepthOfCrawling(2);
			config.setMaxDownloadSize(maxDownloadSize);
			config.setIncludeHttpsPages(true);
			config.setResumableCrawling(true);
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

			// String httpsURL = "https://your.https.url.here/";

			// String inputLine;
			//
			// while ((inputLine = br.readLine()) != null) {
			// System.out.println(inputLine);
			// }

			// FileReader reader = new FileReader(filePath);

			// JSONParser parser = new JSONParser();
			// Object obj = parser.parse(getContent(API_URI));
			JSONArray array = new JSONArray();
			array.add(obj);

			// JSONParser jsonParser = new JSONParser();
			// JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
			//
			// // get a String from the JSON object
			// String titre = (String) jsonObject.get("titre");
			// System.out.println("The titre is: " + titre);
			//
			// String href = (String) jsonObject.get("href");
			// System.out.println("The href is: " + href);
			//
			// // get a number from the JSON object
			// String id = (String) jsonObject.get("id");
			// System.out.println("The id is: " + id);

			// get an array from the JSON object
			// JSONArray lang = (JSONArray) jsonObject.get(message);

			// take the elements of the json array
			// for (int i = 0; i < array.size(); i++) {
			// System.out.println("The " + i + " element of the array: " + array.get(i));
			// }
			Iterator<Object> i = array.iterator();

			// take each value from the json array separately
			while (i.hasNext()) {

				String titre, description, href, URL;
				Long id;
				JSONObject jsonObject;
				JSONArray innerObj = (JSONArray) i.next();

				for (int j = 0; j < innerObj.size(); j++) {
					// System.out.println("The " + j + " element of the array: " + innerObj.get(j));
					id = (Long) ((JSONObject) innerObj.get(j)).get("id");
					titre = (String) ((JSONObject) innerObj.get(j)).get("titre");
					// description = (String) ((JSONObject) innerObj.get(j)).get("Description
					// g\\u00e9n\\u00e9rale");

					href = (String) ((JSONObject) innerObj.get(j)).get("href");

					jsonObject = (JSONObject) parser.parse(getContent(href));
					URL = (String) jsonObject.get("URL");
					description = (String) jsonObject.get("Description g\\u00e9n\\u00e9rale");

					controller.addSeed(URL);
					System.out.println("The id is: " + id);
					System.out.println("The titre is: " + titre);
					System.out.println("The description is: " + description);
					System.out.println("The href is: " + href);
					System.out.println("The URL is: " + URL);
					// ResourceSingleton.getInstance();
					ResourceSingleton.getResources().put(URL, new Resource(String.valueOf(id), titre, URL));
					// System.out.println(
					// IndexerSingleton.getInstance().indexResource(new Resource(String.valueOf(id),
					// titre, URL)));

					// r.getKeywords().addAll(KeywordsExtractor.getKeywordsList(titre + " " +
					// description));
				}
			}

			controller.startNonBlocking(MyCrawler.class, numberOfCrawlers);

			// Thread.sleep(1000);
			// controller.shutdown();
			controller.waitUntilFinish();

			System.out.println("Voila c'est fini");

			getKeywords();
			ResourceSingleton.getInstance();
			// Get a set of the entries
			Set<Entry<String, Resource>> setMap = ResourceSingleton.getResources().entrySet();
			// Get an iterator
			Iterator<Entry<String, Resource>> iteratorMap = setMap.iterator();

			System.out.println(ResourceSingleton.getResources().entrySet());
			System.out.println("\nHashMap with Multiple Values");
			String text = "";
			// File inputFile = new File("content.txt");
			// File inputFile = File.createTempFile("content", ".txt");
			// PrintWriter writer = new PrintWriter(inputFile, "UTF-8");
			System.out.println("indexation lucene");
			// display all the elements
			while (iteratorMap.hasNext()) {
				Map.Entry<String, Resource> entry = (Map.Entry<String, Resource>) iteratorMap.next();
				String key = entry.getKey();
				Resource values = entry.getValue();

				System.out.println(IndexerSingleton.getInstance().indexResource(values));

				// if (values.getKeywords() != null) {
				// // On récupère un ListIterator
				// ListIterator<CardKeyword> it = (ListIterator<CardKeyword>)
				// values.getKeywords().listIterator();
				// while (it.hasNext()) {
				// keywords.addAll(it.next().getTerms());
				//
				// }
				// }

				// if (values.getContent() != null) {
				//
				// text = text + " " + values.getContent().trim();
				// // writer.write(values.getContent() + "\n");
				// // System.out.println(Files.toString(inputFile, Charset.forName("UTF-8")));
				//
				// }
				System.out.println("Key = '" + key + "' has values: " + values.getId());
			}
			// IndexerSingleton.getInstance().close();
			//
			// handle a structure into the json object
			// JSONObject structure = (JSONObject) jsonObject.get("job");
			// System.out.println("Into job structure, name: " + structure.get("name"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void getKeywords() throws Exception {

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
		// exampler = new Examples();
		testAutomaticTagging();
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
		// // Set<String> keywords = new HashSet<String>();
		//
		// // writer.println("Fin !");
		// // writer.close();
		// String text = new String(Files.readAllBytes(Paths.get("contents.txt")));
		// List<CardKeyword> keywords = KeywordsExtractor.getKeywordsList(text);
		//
		// System.out.println("Keywords : " + keywords);
		// System.out.println("Keywords size: " + keywords.size());
		// Document doc = new Document(keywords.toString());
		// RakeAnalyzer rake = new RakeAnalyzer();
		// rake.loadDocument(doc);
		// rake.runWithoutOffset();
		// System.out.println("Rake Keywords : " + doc.termListToString());
	}

	/**
	 * Demonstrates how to perform automatic tagging. Also applicable to keyphrase
	 * extraction.
	 * 
	 * @throws Exception
	 */
	public static void testAutomaticTagging() throws Exception {
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

		// Settings for topic extractor
		topicExtractor.inputDirectoryName = testDir;
		topicExtractor.modelName = modelName;

		// Run topic extractor
		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}
}