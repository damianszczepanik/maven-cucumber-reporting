package net.masterthought.cucumber;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.util.ArrayList;
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

    public void execute() throws MojoExecutionException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        List<String> list = new ArrayList<String>();
        list.add(cucumberOutput.getAbsolutePath());
        ReportBuilder reportBuilder;

        try {
            System.out.println("About to generate");
            reportBuilder = new ReportBuilder(list, outputDirectory, "", buildNumber, projectName, skippedFails, undefinedFails, enableFlashCharts, false, false, "", false);
            reportBuilder.generateReports();

            boolean buildResult = reportBuilder.getBuildStatus();
            if (!buildResult) {
                throw new MojoExecutionException("BUILD FAILED - Check Report For Details");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Error Found:", e);
        }
    }
}
