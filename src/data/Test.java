package data;

public class Test {
	public static void main(String[] args) {
		FileManager manager = new FileManager();

		manager.setDirectory("C:\\Users\\Shawn\\Documents\\Notepad Things");
		manager.createFileHashList();

		manager.getFilemap().forEach(Long.MAX_VALUE - 1, (k, v) -> System.out.println("Key: " + k + " Value: " + v));
	}

}
