package net.masterthought.cucumber;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.sorting.SortingMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Goal which generates a Cucumber Report.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.VERIFY)
@Execute(goal = "generate", phase = LifecyclePhase.VERIFY)
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
     * File where the trends is stored.
     * @parameter
     */
    private int trendsLimit;

    /**
     * File where the trends is stored.
     * @parameter
     */
    private File trendsFile;

    /**
     * Array of PROPERTY files to process
     *
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
     * Treat failed scenarios as build failure when using checkBuildResult=true.
     *
     * @parameter default-value="false"
     */
    private Boolean treatScenariosAsFailed;

    /**
     * Treat 'undefined' steps as failures when using checkBuildResult=true.
     *
     * @parameter default-value="false"
     */
    private Boolean treatUndefinedAsFailed;

    /**
     * Treat 'pending' steps as failures when using checkBuildResult=true.
     *
     * @parameter default-value="false"
     */
    private Boolean treatPendingAsFailed;

    /**
     * Treat 'skipped' steps as failures when using checkBuildResult=true.
     *
     * @parameter default-value="false"
     */
    private Boolean treatSkippedAsFailed;

    /**
     * Additional attributes to classify current test run.
     *
     * @parameter
     */
    private Map<String, String> classifications;

    /**
     * Set this to "true" to bypass generation of Cucumber Reports entirely.
     *
     * @parameter property="cucumber.report.skip" default-value="false"
     */
    private boolean skip;

    /**
     * Merge features with the same ID so scenarios are be merged into single feature.
     *
     * @parameter
     */
    private boolean mergeFeaturesById;

    /**
     * Merge features and scenarios from different JSON files of different runs
     * into a single report by features' and scenarios' ids.
     *
     * @parameter
     */
    private boolean mergeFeaturesWithRetest;

    /**
     * Skips JSON reports which have been parsed but have none features or are empty file at all.
     *
     * @parameter
     */
    private boolean skipEmptyJSONFiles;

    /**
     * Consider skipped tests as not failed ones.
     *
     * @parameter
     */
    private boolean setSkippedAsNotFailing;

    /**
     * Sorting method for features inside the report.
     *
     * @parameter
     */
    private String sortingMethod;

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
        List<String> classificationFilesToProcess = genericFindFiles(classificationDirectory, classificationFiles);

        try {
            Configuration configuration = new Configuration(outputDirectory, projectName);
            configuration.setBuildNumber(buildNumber);
            if (mergeFeaturesById) {
                configuration.addReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID);
            }
            if (MapUtils.isNotEmpty(classifications)) {
                for (Map.Entry<String, String> entry : classifications.entrySet()) {
                    configuration.addClassifications(StringUtils.capitalize(entry.getKey()), entry.getValue());
                }
            }
            if (CollectionUtils.isNotEmpty(classificationFilesToProcess)) {
                configuration.addClassificationFiles(classificationFilesToProcess);
            }
            if (mergeFeaturesById) {
                configuration.addReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID);
            }
            if (mergeFeaturesWithRetest) {
                configuration.addReducingMethod(ReducingMethod.MERGE_FEATURES_WITH_RETEST);
            }
            if (skipEmptyJSONFiles) {
                configuration.addReducingMethod(ReducingMethod.SKIP_EMPTY_JSON_FILES);
            }

            if (setSkippedAsNotFailing) {
                configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
            }

            if (trendsFile != null) {
                configuration.setTrends(trendsFile, trendsLimit);
            }

            if (sortingMethod != null){
                configuration.setSortingMethod(SortingMethod.valueOf(sortingMethod));
            }

            ReportBuilder reportBuilder = new ReportBuilder(jsonFilesToProcess, configuration);
            getLog().info("About to generate Cucumber report.");
            Reportable report = reportBuilder.generateReports();

            if (checkBuildResult && (report == null || report.getFailedSteps() > 0 ||
                    (treatScenariosAsFailed && report.getFailedScenarios() > 0) ||
                    (treatUndefinedAsFailed && report.getUndefinedSteps() > 0) ||
                    (treatPendingAsFailed && report.getPendingSteps() > 0) ||
                    (treatSkippedAsFailed && report.getSkippedSteps() > 0))) {
                throw new MojoExecutionException("BUILD FAILED - Check Report For Details");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Error Found:", e);
        }
    }

    static List<String> genericFindFiles(File targetDirectory, String[] fileIncludePattern) {
        if (ArrayUtils.isEmpty(fileIncludePattern)) {
            return Collections.emptyList();
        }
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
