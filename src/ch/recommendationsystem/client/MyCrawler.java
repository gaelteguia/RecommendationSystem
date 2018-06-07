package ch.recommendationsystem.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

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
			File inputFile = File.createTempFile("temp-file-name", ".pdf");
			FileUtils.copyURLToFile(u, inputFile);
			PDDocument document = null;
			document = PDDocument.load(inputFile);
			PDDocumentInformation pdd = document.getDocumentInformation();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String text;
			text = pdd.getAuthor();
			text = text + " " + pdd.getTitle();
			text = text + " " + pdd.getSubject();
			text = text + " " + pdd.getCreator();
			text = text + " " + pdd.getKeywords();
			text = text + " " + pdfStripper.getText(document);
			// ResourceSingleton.getInstance();
			ResourceSingleton.getResources().get(url).setContent(text.trim());
			Files.write(Paths.get(
					"C:\\glassfish4\\glassfish\\domains\\domain1\\config\\data\\automatic_tagging\\test\\contents.txt"),
					text.trim().getBytes(), StandardOpenOption.APPEND);
			System.out.println(IndexerSingleton.getInstance().indexResource(ResourceSingleton.getResources().get(url)));

			document.close();

		} catch (InvalidPasswordException e3) {
			e3.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}

	}

}