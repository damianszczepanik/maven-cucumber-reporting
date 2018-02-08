package net.masterthought.cucumber;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

import static net.masterthought.cucumber.CucumberReportGeneratorMojo.cucumberFiles;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class CucumberReportGeneratorMojoTest {

	@Test
	public void testCucumberFiles() throws Exception {
		Collection<File> files = cucumberFiles(testResource("cucumber"));
		assertThat(files, hasSize(2));
		assertThat(files, containsInAnyOrder(
				testResource("cucumber/cuc.json"),
				testResource("cucumber/subdir/cuc.json")));

		files = cucumberFiles(testResource("cuc.json"));
		assertThat(files, hasSize(1));
		assertThat(files, contains(testResource("cuc.json")));
	}

	@Test
	public void testNotExistingCucumberFile() throws Exception {
		//given a not existing resource
		File notExistingFile = new File(testResource("cucumber"), "a/not/existing/path");

		//when I get the list of report files files
		Collection<File> files = cucumberFiles(notExistingFile);

		//then the list should be empty
		assertThat(files, hasSize(0));
	}
	
	@Test
	public void percentCalculation() {
		float total = 100;
		float pass = 34;
		float percent = (pass/total)*100;
		System.out.println(percent);
	}

	private File testResource(String relPath) throws URISyntaxException {
		return new File(getClass().getResource("/" + relPath).toURI());
	}
}
