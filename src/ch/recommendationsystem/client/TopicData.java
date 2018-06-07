package ch.recommendationsystem.client;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "topicData", eager = true)
@ApplicationScoped
public class TopicData implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<String> data = Arrays.asList("topic1", "topic2", "topic3");

	public List<String> results = Arrays.asList("topic1", "topic2", "topic3");

	public TopicData() {
		System.out.println("ApplicationContainer constructed");

		String fileName = "C:\\glassfish4\\glassfish\\domains\\domain1\\config\\data\\automatic_tagging\\test\\contents.key";

		try (Stream<String> stream = Files.lines(Paths.get(fileName), Charset.forName("UTF-8"))) {
			data = stream.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String submit() throws IOException {
		System.out.println("Checked values: " + data);
		String listString = "";

		for (String s : data) {
			listString += s + "\t";
		}
		results = IndexerSingleton.getInstance().searchIndex(listString);
		// results = data;
		return "result";
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	@PostConstruct
	public void init() {

	}
}
