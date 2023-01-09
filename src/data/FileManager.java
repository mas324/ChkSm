package data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private ConcurrentHashMap<File, String> filemap;
	private File directory;
	private int algo;
	public int fileComplete;

	public FileManager() {
		this(null, 0);
	}

	public FileManager(File f) {
		this(f, 0);
	}

	public FileManager(File f, int algorithm) {
		directory = f;
		algo = algorithm;
		filemap = new ConcurrentHashMap<>();
		createFileList(directory);
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
		filemap.clear();
		createFileList(getDirectory());
	}

	public void setDirectory(String directory) throws IOException {
		File f = new File(directory);
		if (f.isFile())
			throw new IOException("Selection is not a directory or does not exist");
		setDirectory(f);
	}

	public void setAlgo(int algo) {
		this.algo = algo;
	}

	public int getSize() {
		return filemap.size();
	}

	public int getFileComplete() {
		return fileComplete;
	}

	public ConcurrentHashMap<File, String> getFilemap() {
		createFileHashList();
		return filemap;
	}

	private void createFileHashList() {
		fileComplete = 0;

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

	private void createFileList(File f) {
		File[] list = f.listFiles();

		if (list != null)
			for (File file : list) {
				createFileList(file);
			}
		else
			filemap.put(f, "");
	}
}
