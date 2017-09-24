package net.masterthought.cucumber;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

/**
 * Goal which generates a Cucumber Report.
 *
 * @goal generate
 * @phase verify
 */
public class CucumberReportGeneratorMojo extends AbstractMojo {

    /**
     * Name of the project.
     *
     * @parameter property="project.name"
     * @required
     */
    private String projectName;

    /**
     * Build number.
     *
     * @parameter property="build.number" default-value="1"
     */
    private String buildNumber;
    
    /**
     * Location of the file.
     *
     * @parameter default-value="${project.build.directory}/cucumber-reports"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Location of the JSON file to process.
     *
     * @parameter default-value="${project.build.directory}/cucumber.json"
     * @required
     * @deprecated
     */
    private File cucumberOutput;

    /**
     * List of JSON files to process
     * @parameter
     */
    private List<String> jsonFiles = Collections.emptyList();

    /**
     * Skip check for failed build result.
     *
     * @parameter default-value="true"
     * @required
     */
    private Boolean checkBuildResult;

    /**
     * Build reports from parallel tests.
     *
     * @parameter property="true" default-value="false"
     * @required
     */
    private Boolean parallelTesting;

    /**
     * Additional attributes to classify current test run.
     *
     * @parameter
     */
    private Map<String, String> classifications;

    @Override
    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // this is deprecated but processing to keep backwards compatibility
        List<String> list = new ArrayList<>();
		for (File jsonFile : cucumberFiles(cucumberOutput)) {
			list.add(jsonFile.getAbsolutePath());
		}

		list.addAll(jsonFiles);

        try {
            Configuration configuration = new Configuration(outputDirectory, projectName);
            configuration.setBuildNumber(buildNumber);
            configuration.setParallelTesting(parallelTesting);
            if (!MapUtils.isEmpty(classifications)) {
                for (Map.Entry<String, String> entry : classifications.entrySet()) {
                    configuration.addClassifications(StringUtils.capitalise(entry.getKey()), entry.getValue());
                }
            }

            ReportBuilder reportBuilder = new ReportBuilder(list, configuration);
            getLog().info("About to generate Cucumber report.");
            Reportable report = reportBuilder.generateReports();

            if (checkBuildResult && report == null) {
                throw new MojoExecutionException("BUILD FAILED - Check Report For Details");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Error Found:", e);
        }
    }

	// Normally, I'd keep this private and use mocks for testing the public contract.
	// I'm not sure that the author wants to get that serious with this..
	static Collection<File> cucumberFiles(File file) throws MojoExecutionException {
		if (!file.exists()) {
            return Collections.emptyList();
        }
		if (file.isFile()) {
			return Arrays.asList(file);
		}
		return FileUtils.listFiles(file, new String[] {"json"}, true);
	}
}
