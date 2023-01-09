package data;

import java.io.File;

public class FileChecker extends FileManager {

	public FileChecker() {
		this(null, 0);
	}

	public FileChecker(File f) {
		super(f);
	}

	public FileChecker(File f, int algorithm) {
		super(f, algorithm);
	}

}
