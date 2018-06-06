package ch.recommendationsystem.client;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "/data/crawl/root";

		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		controller.addSeed("http://www.ciip.ch/");
		controller.addSeed("http://www.ciip.ch/La-CIIP/Portrait/Portrait-de-la-CIIP");
		controller.addSeed("http://www.ciip.ch/Activites/Programme-dactivite-2016-2019/Programme-dactivite-2016-2019");

		controller.start(MyCrawler.class, numberOfCrawlers);

	}
}