package ch.recommendationsystem.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexerSingleton {

	private static Indexer indexer;

	// Constructeur priv�
	private IndexerSingleton() throws IOException {

		indexer = new Indexer();

	}

	// M�thode qui va nous retourner notre instance et la cr�er si elle n'existe pas
	public static Indexer getInstance() throws IOException {
		if (indexer == null) {
			new IndexerSingleton();
		}
		return indexer;
	}

}
