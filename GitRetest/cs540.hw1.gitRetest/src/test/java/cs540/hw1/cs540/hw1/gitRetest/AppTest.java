package cs540.hw1.cs540.hw1.gitRetest;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import cs540.hw1.cs540.hw1.gitRetest.App;

public class AppTest {

	@Test
	public void testMain() throws IOException, GitAPIException, ParseException {
		App.main(new String[] {});
	
	}
	
	@Test
	public void testMainWithParameters() throws NoHeadException, IOException, GitAPIException {
		App.main(new String[] {"./Testing_output/tdd-tetris-tutorial/","./src/main/resources/output_retest_data.txt","./src/main/resources/Tetris_FileDependencies.csv"});
	}

}