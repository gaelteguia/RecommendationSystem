package ch.recommendationsystem.client;

import java.io.File;
import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.apache.lucene.index.CorruptIndexException;

public class CustomSystemEventListener implements SystemEventListener {

	@Override
	public boolean isListenerForSource(Object value) {

		return (value instanceof Application);
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {

		if (event instanceof PostConstructApplicationEvent) {
			System.out.println("Application Started. PostConstructApplicationEvent occurred!");

			File inputFile = new File("data/automatic_tagging/test/contents.txt");
			// File inputFile = File.createTempFile("contents", ".txt");
			System.out.println(inputFile.getAbsolutePath());

			Thread thread = new Thread(new MyRunnable());
			thread.start();

		}

		if (event instanceof PreDestroyApplicationEvent) {
			try {
				IndexerSingleton.getInstance().close();
			} catch (CorruptIndexException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			System.out.println("PreDestroyApplicationEvent occurred. Application is stopping.");
		}
	}
}