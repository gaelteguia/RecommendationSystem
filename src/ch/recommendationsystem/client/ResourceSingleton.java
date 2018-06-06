package ch.recommendationsystem.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResourceSingleton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Instance unique pr�-initialis�e */
	private static Map<String, Resource> resources = new HashMap<String, Resource>();
	private static ResourceSingleton instance = null;

	// Constructeur priv�
	private ResourceSingleton() {

	}

	// M�thode qui va nous retourner notre instance et la cr�er si elle n'existe pas
	public static ResourceSingleton getInstance() {
		if (instance == null) {
			new ResourceSingleton();
		}
		return instance;
	}

	public static Map<String, Resource> getResources() {
		if (instance == null) {
			instance = new ResourceSingleton();
		}
		return resources;
	}

	public void put(String s, Resource r) {
		resources.put(s, r);
	}

	private Object readResolve() {
		return instance;
	}

}
