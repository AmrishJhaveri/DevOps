package cs540.hw1.cs540.hw1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utility {

	public static void writeMyFile(String outputFileLocation, String outputFileNames) throws IOException {
		
		File outputFileNamesLoc = new File(outputFileLocation);

		if (outputFileNamesLoc.exists()) {
			outputFileNamesLoc.delete();
		}
		
		outputFileNamesLoc.createNewFile();
		
		FileWriter fw = new FileWriter(outputFileNamesLoc);
		fw.write(outputFileNames.toString());
		fw.close();
	}
}
