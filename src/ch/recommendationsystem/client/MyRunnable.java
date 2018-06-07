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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

	private static MauiTopicExtractor topicExtractor;
	private static MauiModelBuilder modelBuilder;

	private static void setGeneralOptions() {

		modelBuilder.debugMode = true;

		Stemmer stemmer = new FrenchStemmer();
		Stopwords stopwords = new StopwordsFrench("C:\\data\\stopwords\\stopwords_fr.txt");
		String language = "fr";
		String encoding = "UTF-8";
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

		topicExtractor.vocabularyName = vocabulary;
		topicExtractor.vocabularyFormat = format;

		topicExtractor.debugMode = true;
		topicExtractor.topicsPerDocument = 10;
	}

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

	public static void testAutomaticTagging() throws Exception {
		topicExtractor = new MauiTopicExtractor();
		modelBuilder = new MauiModelBuilder();
		setGeneralOptions();
		setFeatures();

		String trainDir = "C:\\glassfish4\\glassfish\\domains\\domain1\\config\\data\\automatic_tagging\\train";
		String testDir = "C:\\glassfish4\\glassfish\\domains\\domain1\\config\\data\\automatic_tagging\\test";

		String modelName = "test";

		modelBuilder.inputDirectoryName = trainDir;
		modelBuilder.modelName = modelName;

		modelBuilder.minNumOccur = 1;

		HashSet<String> fileNames = modelBuilder.collectStems();
		modelBuilder.buildModel(fileNames);
		modelBuilder.saveModel();

		topicExtractor.inputDirectoryName = testDir;
		topicExtractor.modelName = modelName;

		topicExtractor.loadModel();
		fileNames = topicExtractor.collectStems();
		topicExtractor.extractKeyphrases(fileNames);
	}

	static String getContent(String URI) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

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

	static void getKeywords() throws Exception {

		Date todaysDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss");
		String formattedDate1 = formatter.format(todaysDate);

		testAutomaticTagging();

		todaysDate = new java.util.Date();
		String formattedDate2 = formatter.format(todaysDate);
		System.err.print("Run from " + formattedDate1);
		System.err.println(" to " + formattedDate2);

	}

	public void run() {
		System.out.println("MyRunnable running");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(getContent(API_URI));

			String crawlStorageFolder = Files.createTempDir().getAbsolutePath();
			int numberOfCrawlers = 1;
			int maxDownloadSize = 104857600;
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);
			config.setIncludeBinaryContentInCrawling(true);
			// config.setPolitenessDelay(100);
			config.setMaxDepthOfCrawling(2);
			config.setMaxDownloadSize(maxDownloadSize);
			config.setIncludeHttpsPages(true);
			config.setResumableCrawling(true);
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

			JSONArray array = new JSONArray();
			array.add(obj);

			Iterator<Object> i = array.iterator();

			while (i.hasNext()) {

				String titre, href, URL;
				Long id;
				JSONObject jsonObject;
				JSONArray innerObj = (JSONArray) i.next();

				for (int j = 0; j < innerObj.size(); j++) {
					id = (Long) ((JSONObject) innerObj.get(j)).get("id");
					titre = (String) ((JSONObject) innerObj.get(j)).get("titre");

					href = (String) ((JSONObject) innerObj.get(j)).get("href");

					jsonObject = (JSONObject) parser.parse(getContent(href));
					URL = (String) jsonObject.get("URL");

					controller.addSeed(URL);
					System.out.println("The id is: " + id);
					System.out.println("The titre is: " + titre);
					System.out.println("The href is: " + href);
					System.out.println("The URL is: " + URL);
					ResourceSingleton.getResources().put(URL, new Resource(String.valueOf(id), titre, URL));

				}
			}

			controller.startNonBlocking(MyCrawler.class, numberOfCrawlers);

			// Thread.sleep(1000);
			// controller.shutdown();
			controller.waitUntilFinish();

			getKeywords();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}