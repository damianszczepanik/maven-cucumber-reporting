package net.masterthought.cucumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.DirectoryScanner;
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
     * Location to output the HTML report to.
     *
     * @parameter default-value="${project.build.directory}/cucumber-reports"
     * @required
     */
    private File outputDirectory;

    /**
     * Location of the Cucumber JSON files to generate the report from.
     * Files in this directory will be matched against the patterns specified
     * in the jsonFiles property.
     * If not specified, defaults to the same value as outputDirectory.
     *
     * @parameter
     */
    private File inputDirectory;

    /**
     * Array of JSON files to process
     * @parameter
     * @required
     */
    private String[] jsonFiles;

    /**
     * Location of the classification files to add to the report.
     * Files in this directory will be matched against the patterns specified
     * in the classificationFiles property.
     * If not specified, defaults to the same value as outputDirectory.
     *
     * @parameter
     */
    private File classificationDirectory;

    /**
     * Array of PROPERTY files to process
     * @parameter
     */
    private String[] classificationFiles;

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

    /**
     * Set this to "true" to bypass generation of Cucumber Reports entirely.
     *
     * @parameter property="skip" default-value="false"
     */
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException {

        if (skip) {
            getLog().info("Cucumber report generation is skipped.");
            return;
        }

        if (inputDirectory == null) {
            inputDirectory = outputDirectory;
        }

        if (classificationDirectory == null) {
            classificationDirectory = outputDirectory;
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        // Find all json files that match json file include pattern...
        List<String> jsonFilesToProcess = genericFindFiles(inputDirectory,jsonFiles);

        // Find all json files that match classification file include pattern...
        List<String> classificationFilesToProcess = genericFindFiles(classificationDirectory,classificationFiles);

        try {
            Configuration configuration = new Configuration(outputDirectory, projectName);
            configuration.setBuildNumber(buildNumber);
            configuration.setParallelTesting(parallelTesting);
            if (!MapUtils.isEmpty(classifications)) {
                for (Map.Entry<String, String> entry : classifications.entrySet()) {
                    configuration.addClassifications(StringUtils.capitalise(entry.getKey()), entry.getValue());
                }
            }
            if(CollectionUtils.isNotEmpty(classificationFilesToProcess)) {
                configuration.addClassificationFiles(classificationFilesToProcess);
            }

            ReportBuilder reportBuilder = new ReportBuilder(jsonFilesToProcess, configuration);
            getLog().info("About to generate Cucumber report.");
            Reportable report = reportBuilder.generateReports();

            if (checkBuildResult && (report == null || report.getFailedSteps() > 0)) {
                throw new MojoExecutionException("BUILD FAILED - Check Report For Details");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Error Found:", e);
        }
    }

    static List<String> genericFindFiles(File targetDirectory, String[] fileIncludePattern) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(fileIncludePattern);
        scanner.setBasedir(targetDirectory);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        return fullPathToFiles(files, targetDirectory);
    }

    private static List<String> fullPathToFiles(String[] files, File targetDirectory) {
        List<String> fullPathList = new ArrayList<>();
        for (String file : files) {
            fullPathList.add(new File(targetDirectory, file).getAbsolutePath());
        }
        return fullPathList;
    }

}
