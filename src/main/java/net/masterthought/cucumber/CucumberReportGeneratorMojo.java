package net.masterthought.cucumber;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        List<String> list = new ArrayList<String>();
		for (File jsonFile : cucumberFiles(cucumberOutput)) {
			list.add(jsonFile.getAbsolutePath());
		}

        try {
            ReportBuilder reportBuilder = new ReportBuilder(list, outputDirectory, "", buildNumber, projectName, skippedFails, undefinedFails, enableFlashCharts, false, false, "", false);
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
		// I'm not entirely convinced we should break the build in this case.
		if (!file.exists()) {
			throw new MojoExecutionException(file.getAbsolutePath() + " does not exist.");
		}
		if (file.isFile()) {
			return Arrays.asList(file);
		}
		return FileUtils.listFiles(file, new String[] {"json"}, true);
	}
}
