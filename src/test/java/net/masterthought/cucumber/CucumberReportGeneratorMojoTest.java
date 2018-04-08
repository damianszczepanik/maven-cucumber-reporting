package net.masterthought.cucumber;

import org.junit.Test;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import static net.masterthought.cucumber.CucumberReportGeneratorMojo.genericFindFiles;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CucumberReportGeneratorMojoTest {

    @Test
    public void testJsonFilesRegexPattern() throws Exception {

        // Given
        File targetDirectory = testResource("json");
        String[] jsonFiles = new String[]{"**/*.json"};

        // When
        List<String> files = genericFindFiles(targetDirectory,jsonFiles);

        // Then
        assertThat(files, hasSize(2));
        assertThat(files, containsInAnyOrder(
                testResource("json/cuc.json").getAbsolutePath(),
                testResource("json/subdir/cuc.json").getAbsolutePath()));

    }

    @Test
    public void testJsonFilesNamePattern() throws Exception {

        // Given
        File targetDirectory = testResource("json");
        String[] jsonFiles = new String[]{"cuc.json"};

        // When
        List<String> files = genericFindFiles(targetDirectory,jsonFiles);

        // Then
        assertThat(files, hasSize(1));
        assertThat(files, contains(testResource("json/cuc.json").getAbsolutePath()));

    }

    @Test
    public void testJsonFilesNameAndRegexPattern() throws Exception {

        // Given
        File targetDirectory = testResource("json");
        String[] jsonFiles = new String[]{"*/cuc.json"};

        // When
        List<String> files = genericFindFiles(targetDirectory,jsonFiles);

        // Then
        assertThat(files, hasSize(1));
        assertThat(files, contains(testResource("json/subdir/cuc.json").getAbsolutePath()));

    }

    @Test
    public void testJsonFilesInvalidPath() throws Exception {

        // Given
        File targetDirectory = testResource("json");
        String[] jsonFiles = new String[]{"a/not/existing/path"};

        // When
        List<String> files = genericFindFiles(targetDirectory,jsonFiles);

        // Then
        assertThat(files, hasSize(0));

    }

    @Test
    public void testClassificationFilesRegexPattern() throws Exception {

        // Given
        File targetDirectory = testResource("classification");
        String[] propertyFiles = new String[]{"**/*.properties"};

        // When
        List<String> files = genericFindFiles(targetDirectory,propertyFiles);

        // Then
        assertThat(files, hasSize(2));
        assertThat(files, containsInAnyOrder(
                testResource("classification/classifications.properties").getAbsolutePath(),
                testResource("classification/subdir/classifications.properties").getAbsolutePath()));

    }

    @Test
    public void testClassificationFilesNamePattern() throws Exception {

        // Given
        File targetDirectory = testResource("classification");
        String[] propertyFiles = new String[]{"classifications.properties"};

        // When
        List<String> files = genericFindFiles(targetDirectory,propertyFiles);

        // Then
        assertThat(files, hasSize(1));
        assertThat(files, contains(testResource("classification/classifications.properties").getAbsolutePath()));

    }

    @Test
    public void testClassificationFilesNameAndRegexPattern() throws Exception {

        // Given
        File targetDirectory = testResource("classification");
        String[] propertyFiles = new String[]{"*/classifications.properties"};

        // When
        List<String> files = genericFindFiles(targetDirectory,propertyFiles);

        // Then
        assertThat(files, hasSize(1));
        assertThat(files, contains(testResource("classification/subdir/classifications.properties").getAbsolutePath()));

    }

    @Test
    public void testClassificationFilesInvalidPath() throws Exception {

        // Given
        File targetDirectory = testResource("classification");
        String[] jsonFiles = new String[]{"a/not/existing/path"};

        // When
        List<String> files = genericFindFiles(targetDirectory,jsonFiles);

        // Then
        assertThat(files, hasSize(0));

    }

    private File testResource(String relPath) throws URISyntaxException {

        return new File(getClass().getResource("/" + relPath).toURI());

    }
}
