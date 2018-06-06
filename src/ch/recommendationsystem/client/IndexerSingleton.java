package ch.recommendationsystem.client;

import java.io.IOException;

public class IndexerSingleton {

	private static Indexer indexer;

	private IndexerSingleton() throws IOException {

		indexer = new Indexer();

	}

	public static Indexer getInstance() throws IOException {
		if (indexer == null) {
			new IndexerSingleton();
		}
		return indexer;
	}

}
