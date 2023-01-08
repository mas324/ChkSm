package data;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

public class Test {
	public static void main(String[] args) throws IOException, InterruptedException {
		FileManager manager = new FileManager();

		manager.setDirectory("C:\\Users\\Shawn\\Documents\\Notepad Things");

		Thread t = new Thread(() -> {
			for (Entry<File, String> e : manager.getFilemap().entrySet()) {
				System.out.println("Key: " + e.getKey() + " Value: " + e.getValue());
			}
		});

		t.start();

		while (FileManager.fileComplete < 100)
			System.out.println(FileManager.fileComplete);

	}

}
