package data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private static ConcurrentHashMap<File, String> filemap;
	private File directory;
	private int algo;
	public static int fileComplete;
	public static int fileTotal;

	public FileManager() {
		this(null, 0);
	}

	public FileManager(File f) {
		this(f, 0);
	}

	public FileManager(File f, int algorithm) {
		directory = f;
		algo = algorithm;
		createFileTotal(directory);
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public void setAlgo(int algo) {
		this.algo = algo;
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

	private void createFileHashList() {
		filemap = new ConcurrentHashMap<>();
		createFileList(directory);

		fileComplete = 0;

		System.gc();

		int THREADS;
		try {
			THREADS = Integer.parseInt(System.getenv("NUMBER_OF_PROCESSORS")) / 4;
		} catch (SecurityException e) {
			THREADS = 2;
		}

		switch (algo) {
		case 0:
			filemap.forEachKey(filemap.size() / THREADS, (key) -> {
				filemap.put(key, Hashing.fast(key));
				fileComplete++;
			});
			break;
		case 1:
			filemap.forEachKey(filemap.size() / THREADS, (key) -> {
				filemap.put(key, Hashing.slow(key));
				fileComplete++;
			});
			break;
		case 2:
			filemap.forEachKey(filemap.size() / THREADS, (key) -> {
				filemap.put(key, Hashing.enhanced(key));
				fileComplete++;
			});
			break;
		default:
			filemap.forEachKey(filemap.size() / THREADS, (key) -> {
				filemap.put(key, Hashing.fast(key));
				fileComplete++;
			});
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

	private void createFileTotal(File f) {
		File[] list = f.listFiles();
		if (list != null)
			for (File file : list) {
				createFileTotal(file);
			}
		else {
			fileTotal++;
		}
	}
}
