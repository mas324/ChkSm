package data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {

	private static ConcurrentHashMap<File, String> filemap;
	private File directory;

	public FileManager() {
		directory = null;
	}

	public FileManager(File f) throws IOException {
		directory = f;
		createFileHashList();
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public void setDirectory(String directory) {
		this.directory = new File(directory);
	}

	public ConcurrentHashMap<File, String> getFilemap() {
		return filemap;
	}

	protected void createFileHashList() {
		filemap = new ConcurrentHashMap<>();
		createFileList(directory);

		filemap.forEachKey(filemap.size() / 8, k -> filemap.compute(k, (key, value) -> Hashing.fast(key)));
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
