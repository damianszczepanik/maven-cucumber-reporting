package net.masterthought.cucumber;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
     * @parameter expression="${project.name}"
     * @required
     */
    private String projectName;

    /**
     * Build number.
     *
     * @parameter expression="${build.number}" default-value="1"
     */
    private String buildNumber;
    
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/cucumber-reports"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/cucumber.json"
     * @required
     */
    private File cucumberOutput;

    /**
     * Skipped fails
     *
     * @parameter expression="false" default-value="false"
     * @required
     */
    private Boolean skippedFails;

    /**
     * Undefined fails
     *
     * @parameter expression="false" default-value="false"
     * @required
     */
    private Boolean undefinedFails;

    /**
     * Pending fails
     *
     * @parameter expression="false" default-value="false"
     * @required
     */
    private Boolean pendingFails;

    /**
     * Missing fails
     *
     * @parameter expression="false" default-value="false"
     * @required
     */
    private Boolean missingFails;

    /**
     * Enable Flash Charts.
     *
     * @parameter expression="true"
     * @required
     */
    private Boolean enableFlashCharts;

    /**
     * Skip check for failed build result
     *
     * @parameter expression="true" default-value="true"
     * @required
     */
    private Boolean checkBuildResult;

    /**
     * Build reports from parallel tests
     *
     * @parameter expression="true" default-value="true"
     * @required
     */
    private Boolean parallelTesting;

    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        List<String> list = new ArrayList<String>();
		for (File jsonFile : cucumberFiles(cucumberOutput)) {
			list.add(jsonFile.getAbsolutePath());
		}

        if (list.isEmpty()) {
            getLog().warn(cucumberOutput.getAbsolutePath() + " does not exist.");
            return;
        }

        try {
            ReportBuilder reportBuilder = new ReportBuilder(list, outputDirectory, "", buildNumber, projectName, skippedFails, pendingFails, undefinedFails, missingFails, enableFlashCharts, false, false, "", false, parallelTesting);
            getLog().info("About to generate Cucumber report.");
            reportBuilder.generateReports();

            if (checkBuildResult) {
                boolean buildResult = reportBuilder.getBuildStatus();
                if (!buildResult) {
                    throw new MojoExecutionException("BUILD FAILED - Check Report For Details");
                }
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
