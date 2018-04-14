package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ContactsConsolidator {

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\krzysztof.dabkowski\\Documents\\2_ipt\\1_Backoffice\\Contacts\\contacts";

		String files, fileName;
		StringBuilder theWholeShit = new StringBuilder();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				fileName = listOfFiles[i].getAbsolutePath();
				// System.out.println(files);

				BufferedReader reader = new BufferedReader(new FileReader(
						fileName));
				String line = null;
				StringBuilder stringBuilder = new StringBuilder();
				String ls = System.getProperty("line.separator");

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append(ls);
				}

				reader.close();
				System.out.println(stringBuilder.toString());
				// break;
			}
		}
	}

}
