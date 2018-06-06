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

		// only for Application
		return (value instanceof Application);
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {

		if (event instanceof PostConstructApplicationEvent) {
			// String path = getServletContext().getRealPath("/filename.txt");
			System.out.println("Application Started. PostConstructApplicationEvent occurred!");

			File inputFile = new File("C:\\data\\automatic_tagging\\test\\contents.txt");
			// File inputFile = File.createTempFile("contents", ".txt");
			System.out.println(inputFile.getAbsolutePath());
			// System.out.println("Euye Voila X");
			Thread thread = new Thread(new MyRunnable());
			thread.start();

		}

		if (event instanceof PreDestroyApplicationEvent) {
			try {
				IndexerSingleton.getInstance().close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PreDestroyApplicationEvent occurred. Application is stopping.");
		}
	}
}