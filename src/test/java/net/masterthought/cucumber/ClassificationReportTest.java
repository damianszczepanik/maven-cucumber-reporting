package net.masterthought.cucumber;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ClassificationReportTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void generatesReportWithClassificationFiles() throws Exception {
        File inputDirectory = temporaryFolder.newFolder("input");
        try (InputStream input = getClass().getResourceAsStream("/reporting/cucumber.json")) {
            Files.copy(input, new File(inputDirectory, "cucumber.json").toPath());
        }
        Files.write(new File(inputDirectory, "classification.properties").toPath(),
                "Environment=Local fixture\n".getBytes(StandardCharsets.UTF_8));
        File outputDirectory = temporaryFolder.newFolder("output");

        CucumberReportGeneratorMojo mojo = new CucumberReportGeneratorMojo();
        configure(mojo, "projectName", "Classification fixture");
        configure(mojo, "inputDirectory", inputDirectory);
        configure(mojo, "outputDirectory", outputDirectory);
        configure(mojo, "jsonFiles", new String[]{"*.json"});
        configure(mojo, "classificationDirectory", inputDirectory);
        configure(mojo, "classificationFiles", new String[]{"*.properties"});
        configure(mojo, "checkBuildResult", false);

        mojo.execute();

        String report = Files.readString(new File(outputDirectory,
                "cucumber-html-reports/overview-features.html").toPath());
        assertThat(report, containsString("Environment"));
        assertThat(report, containsString("Local fixture"));
    }

    private static void configure(CucumberReportGeneratorMojo mojo, String name, Object value) throws Exception {
        Field field = CucumberReportGeneratorMojo.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(mojo, value);
    }
}
