package ch.recommendationsystem.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	final String solrUrl = "http://localhost:8983/solr/coreExample";
	SolrClient client = new HttpSolrClient.Builder(solrUrl).build();

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	// private List<SolrInputDocument> documentsIndexed = new
	// CopyOnWriteArrayList<SolrInputDocument>();
	// private int NO_OF_DOCUMENT_TO_COMMIT = 1;

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && href.startsWith("https://bdper.plandetudes.ch/");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// URLConnection connection = u.openConnection();

		// InputStream in = connection.getInputStream();
		try {
			File inputFile = File.createTempFile("temp-file-name", ".pdf");

			FileUtils.copyURLToFile(u, inputFile);

			// Loading an existing document
			// File file = new File(url);
			PDDocument document = null;
			try {
				document = PDDocument.load(inputFile);
			} catch (InvalidPasswordException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			// Getting the PDDocumentInformation object
			PDDocumentInformation pdd = document.getDocumentInformation();
			// Instantiate PDFTextStripper class
			PDFTextStripper pdfStripper = new PDFTextStripper();
			// SolrInputDocument doSolrInputDocument = new SolrInputDocument();
			// doSolrInputDocument.setField("id", page.hashCode());
			String text;
			// Retrieving the info of a PDF document
			// System.out.println("Author of the document is :" + pdd.getAuthor());
			text = pdd.getAuthor();
			// doSolrInputDocument.setField("author", pdd.getAuthor());
			// System.out.println("Title of the document is :" + pdd.getTitle());
			text = text + " " + pdd.getTitle();
			// doSolrInputDocument.setField("title", pdd.getTitle());
			// System.out.println("Subject of the document is :" + pdd.getSubject());
			text = text + " " + pdd.getSubject();
			// doSolrInputDocument.setField("subject", pdd.getSubject());
			// System.out.println("Creator of the document is :" + pdd.getCreator());
			text = text + " " + pdd.getCreator();
			// doSolrInputDocument.setField("creator", pdd.getCreator());
			// System.out.println("Creation date of the document is :" +
			// pdd.getCreationDate());
			// text = text + " " + pdd.getCreationDate();//
			// doSolrInputDocument.setField("creation_date",
			// pdd.getCreationDate());
			// System.out.println("Modification date of the document is :" +
			// pdd.getModificationDate());
			// text = text + " " + pdd.getModificationDate();//
			// doSolrInputDocument.setField("modification_date",
			// pdd.getModificationDate());
			// System.out.println("Keywords of the document are :" + pdd.getKeywords());
			text = text + " " + pdd.getKeywords();// doSolrInputDocument.setField("keywords", pdd.getKeywords());
			// Retrieving text from PDF document
			text = text + " " + pdfStripper.getText(document);

			ResourceSingleton.getInstance();
			// List<CardKeyword> keywordsList = KeywordsExtractor.getKeywordsList(text);
			ResourceSingleton.getResources().get(url).setContent(text.trim());
			// PrintWriter writer = new
			// PrintWriter("C:\\glassfish4\\glassfish\\domains\\domain1\\config\\contents.txt",
			// "UTF-8");

			Files.write(Paths.get("C:\\data\\automatic_tagging\\test\\contents.txt"), text.trim().getBytes(),
					StandardOpenOption.APPEND);

			// writer.write(text.trim() + "\n");
			// writer.close();
			// getKeywords();
			// ResourceSingleton.getInstance().get(url).setKeywords(KeywordsExtractor.getKeywordsList(text));
			// System.out.println(IndexerSingleton.getInstance().indexResource(ResourceSingleton.getInstance().get(url)));
			// System.out.println("Crawl content : " +
			// ResourceSingleton.getInstance().get(url).getContent());

			// r.getKeywords().addAll(KeywordsExtractor.getKeywordsList(titre + " " +
			// description));

			// System.out.println(keywordsList);
			// doSolrInputDocument.setField("text", text);
			// documentsIndexed.add(doSolrInputDocument);
			//
			// if (documentsIndexed.size() > NO_OF_DOCUMENT_TO_COMMIT) {
			// try {
			// client.add(doSolrInputDocument);
			//
			// client.commit(true, true);
			// } catch (Exception e) {
			// System.out.println(e.getMessage());
			// e.printStackTrace();
			// }
			// }

			// Closing the document
			try {
				document.close();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			// Document d = null;
			//
			// d = Jsoup.parse(inputFile, page.getContentEncoding());
			// System.out.println(d.toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			// } catch (URISyntaxException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// System.out.println("Data: " + page.getContentType());

			// if (page.getParseData() instanceof HtmlParseData) {
			// HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();
			// String html = htmlParseData.getHtml();
			// Set<WebURL> links = htmlParseData.getOutgoingUrls();
			// Document doc = Jsoup.parse(html);
			// SolrInputDocument doSolrInputDocument = new SolrInputDocument();
			// doSolrInputDocument.setField("id", page.hashCode());
			// Elements linksList = doc.getElementsByTag("a");
			//
			// for (Element link : linksList) {
			// String linkHref = link.attr("href");
			// System.out.println(linkHref + "printed attribute \n");
			// String linkText = link.text();
			// System.out.println(linkText + "printed text \n");
			// doSolrInputDocument.setField("features", linkHref);
			// ;
			// }
			//
			// Elements paragraphList = doc.getElementsByTag("p");
			// for (Element parElement : paragraphList) {
			// String paragraphText = parElement.text();
			// System.out.println(paragraphText + "printed para text \n");
			// doSolrInputDocument.setField("features", paragraphText);
			// }
			//
			// Element body = doc.body();
			// Elements news = body.getElementsByClass("news-item");
			// for (Element n : news) {
			// String newsText = n.text();
			// System.out.println(newsText + "printed news text \n");
			// doSolrInputDocument.setField("news", newsText);
			//
			// }
			// doSolrInputDocument.setField("title", doc.title());
			//
			// documentsIndexed.add(doSolrInputDocument);
			//
			// if (documentsIndexed.size() > NO_OF_DOCUMENT_TO_COMMIT) {
			// try {
			// client.add(doSolrInputDocument);
			//
			// client.commit(true, true);
			// } catch (Exception e) {
			// System.out.println(e.getMessage());
			// e.printStackTrace();
			// }
			// }
			// System.out.println("Text length: " + text.length());
			// System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());
			// }
		}
	}

}