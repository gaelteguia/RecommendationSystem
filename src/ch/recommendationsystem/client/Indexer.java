package ch.recommendationsystem.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Indexer {

	private IndexWriter writer;
	private StandardAnalyzer analyzer;

	private Directory index = new RAMDirectory();

	public Indexer() throws IOException {

		analyzer = new StandardAnalyzer();

		IndexWriterConfig conf = new IndexWriterConfig(analyzer);

		writer = new IndexWriter(index, conf);
	}

	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}

	private void updateDocument(Resource resource) throws IOException {

		Document doc = new Document();
		doc.add(new TextField("title", resource.getTitle(), Field.Store.YES));
		doc.add(new TextField("url", resource.getURL(), Field.Store.YES));
		doc.add(new TextField("content", resource.getContent(), Field.Store.YES));
		if (resource.getKeywords() != null && !resource.getKeywords().isEmpty())
			doc.add(new TextField("keywords", resource.getKeywords().toString(), Field.Store.YES));

		doc.add(new StringField("id", resource.getId(), Field.Store.YES));

		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			System.out.println("adding ");
			writer.addDocument(doc);
		} else {
			System.out.println("updating ");
			writer.updateDocument(new Term("id", resource.getId()), doc);
		}

	}

	public int indexResource(Resource resource) throws IOException {
		System.out.println("Updating index: " + resource.getTitle());
		updateDocument(resource);
		return writer.numDocs();
	}

	public List<String> searchIndex(String queryStr) throws IOException {
		writer.commit();
		IndexReader reader = null;
		List<String> results = new ArrayList<String>();
		try {

			reader = DirectoryReader.open(index);

			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser qp = new QueryParser("content", analyzer);
			Query query = qp.parse(queryStr);

			TopDocs foundDocs = searcher.search(query, 10);

			System.out.println("Total Results :: " + foundDocs.totalHits);

			for (ScoreDoc sd : foundDocs.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				System.out.println("Document Name : " + d.get("title") + "  :: url : " + d.get("url") + "  :: Score : "
						+ sd.score);
				results.add("Document Name : " + d.get("title") + "  :: url : " + d.get("url") + "  :: Score : "
						+ sd.score);
			}

			reader.close();
		} catch (IOException | ParseException e) {

			e.printStackTrace();
		}
		return results;
	}

}