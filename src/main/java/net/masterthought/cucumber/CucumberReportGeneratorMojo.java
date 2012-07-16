package net.masterthought.cucumber;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Goal which generates a Cucumber Report.
 * 
 * @goal generate
 * @phase verify
 */
public class CucumberReportGeneratorMojo extends AbstractMojo {

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @readonly
     */
     private MavenProject project;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}/cucumber-reports"
     * @required
     */
    private File outputDirectory;

    /**
     * List of ant path globs, that will be used as cucumber json output.
     * examples
     * <pre>
     *   <cucumberIncludePatterns>
     *     <param>target/**&#47;*.json</param> 
     *   <cucumberIncludePatterns>
     * <pre/>
     * @parameter 
     * @required
     */
    private String[] cucumberIncludePatterns;
    
    private String[] scanPatternsForAbsolutePaths(){
	File baseFile = new File(project.getBuild().getDirectory()).getAbsoluteFile();
	DirectoryScanner ds = new DirectoryScanner();
        ds.setCaseSensitive(false);
        ds.setFollowSymlinks(false);
        ds.setBasedir(baseFile);
        ds.setIncludes(cucumberIncludePatterns);
        ds.scan();
        String[] fileNames = ds.getIncludedFiles();
        String[] absolutePaths = new String[fileNames.length];
        int count = 0;
        for (String fileName : fileNames){
            absolutePaths[count++] = new File(baseFile, fileName).getAbsolutePath();
        }
        return absolutePaths;
    }
    
    private String calcName(){
	return project.getBuild().getFinalName();
    }
    
    private String calcVersion(){
	return project.getVersion();
    }
    
    public void execute() throws MojoExecutionException {
	if (!outputDirectory.exists()) {
	    outputDirectory.mkdirs();
	}
        List<String> list = Arrays.asList(scanPatternsForAbsolutePaths());
	ReportBuilder reportBuilder;
	try {
	    System.out.println("About to generate");
	    reportBuilder = new ReportBuilder(list, outputDirectory, "", calcVersion() ,  calcName(), false, false, true, false);
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
