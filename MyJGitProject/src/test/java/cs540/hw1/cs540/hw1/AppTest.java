package cs540.hw1.cs540.hw1;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class AppTest {

	@Test
	public void testMain() throws IOException, GitAPIException, ParseException {
		App.main(new String[]{});
		
		App.main(new String[]{"1","./src/main/resources/java-full-data.json","./src/main/resources/output-project-names.json","./src/main/resources/output-repo-links.json","./Testing_output/WithParameters/"});
		
		App.main(new String[]{"-1","./src/main/resources/java-full-data.json","./src/main/resources/output-project-names.json","./src/main/resources/output-repo-links.json","./Testing_output/AllProjects/"});
	}

}
