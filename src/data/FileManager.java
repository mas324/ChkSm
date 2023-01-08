package data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private static ConcurrentHashMap<File, String> filemap;
	private File directory;
	private int algo;

	public FileManager() {
		directory = null;
		algo = 0;
	}

	public FileManager(File f) {
		directory = f;
		algo = 0;
	}

	public FileManager(File f, int algorithm) {
		directory = f;
		algo = algorithm;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public void setDirectory(String directory) throws IOException {
		File f = new File(directory);
		if (f.isFile())
			throw new IOException("Selection is not a directory");
		this.directory = new File(directory);
	}

	public ConcurrentHashMap<File, String> getFilemap() {
		createFileHashList();
		return filemap;
	}

	public double getPercent() {
		return filemap.values().size() / filemap.size();
	}

	private void createFileHashList() {
		filemap = new ConcurrentHashMap<>();
		createFileList(directory);

		System.gc();

		int THREADS;
		try {
			THREADS = Integer.parseInt(System.getenv("NUMBER_OF_PROCESSORS")) / 2;
		} catch (SecurityException e) {
			THREADS = 2;
		}

		switch (algo) {
		case 0:
			filemap.forEachKey(filemap.size() / THREADS, k -> filemap.compute(k, (key, value) -> Hashing.fast(key)));
			break;
		case 1:
			filemap.forEachKey(filemap.size() / THREADS, k -> filemap.compute(k, (key, value) -> Hashing.slow(key)));
			break;
		case 2:
			filemap.forEachKey(filemap.size() / THREADS,
					k -> filemap.compute(k, (key, value) -> Hashing.enhanced(key)));
			break;
		default:
			filemap.forEachKey(filemap.size() / THREADS, k -> filemap.compute(k, (key, value) -> Hashing.fast(key)));
			break;
		}

	}

	private static void createFileList(File f) {
		File[] list = f.listFiles();

		if (list != null)
			for (File file : list) {
				createFileList(file);
			}
		else
			filemap.put(f, "0");
	}
}
