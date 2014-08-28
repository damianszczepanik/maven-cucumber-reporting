package net.masterthought.cucumber;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

import static net.masterthought.cucumber.CucumberReportGeneratorMojo.cucumberFiles;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

	private File testResource(String relPath) throws URISyntaxException {
		return new File(getClass().getResource("/" + relPath).toURI());
	}
}
