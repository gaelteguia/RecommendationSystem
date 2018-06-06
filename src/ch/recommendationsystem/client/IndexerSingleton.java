package ch.recommendationsystem.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexerSingleton {

	private static Indexer indexer;

	// Constructeur privé
	private IndexerSingleton() throws IOException {

		indexer = new Indexer();

	}

	// Méthode qui va nous retourner notre instance et la créer si elle n'existe pas
	public static Indexer getInstance() throws IOException {
		if (indexer == null) {
			new IndexerSingleton();
		}
		return indexer;
	}

}
