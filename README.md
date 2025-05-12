[![Github build](https://github.com/damianszczepanik/maven-cucumber-reporting/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/damianszczepanik/maven-cucumber-reporting/actions/workflows/build.yml)

![GitHub tag](https://img.shields.io/github/v/tag/damianszczepanik/maven-cucumber-reporting?label=maven%20central)
[![License](https://img.shields.io/badge/license-GNU%20LGPL%20v2.1-blue.svg)](https://raw.githubusercontent.com/damianszczepanik/maven-cucumber-reporting/master/LICENCE)

[![Live Demo](https://img.shields.io/badge/Live%20Demo-Online-blue.svg)](https://damianszczepanik.github.io/cucumber-html-reports/overview-features.html)

Maven mojo for the cucumber-reporting - put this into your `pom.xml` and run `mvn verify` so cucumber reports will be generated in `target/cucumber-html-reports`

Read more about the project and configuration here: [maven-cucumber-reports](https://github.com/damianszczepanik/cucumber-reporting)

Run with: mvn verify

     <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <testFailureIgnore>true</testFailureIgnore>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>net.masterthought</groupId>
                    <artifactId>maven-cucumber-reporting</artifactId>
                    <version>(check version above)</version>
                    <executions>
                        <execution>
                            <id>execution</id>
                            <phase>verify</phase>
                            <goals>
                                <goal>generate</goal>
                            </goals>
                            <configuration>
                                <projectName>cucumber-jvm-example</projectName>
                                <!-- optional, per documentation set this to "true" to bypass generation of Cucumber Reports entirely, defaults to false if not specified -->
                                <skip>false</skip>
                                <!-- output directory for the generated report -->
                                <outputDirectory>${project.build.directory}</outputDirectory>
                                <!-- optional, defaults to outputDirectory if not specified -->
                                <inputDirectory>${project.build.directory}/jsonReports</inputDirectory>
                                <jsonFiles>
                                    <!-- supports wildcard or name pattern -->
                                    <param>**/*.json</param>
                                </jsonFiles>
                                <!-- optional, defaults to outputDirectory if not specified -->
                                <classificationDirectory>${project.build.directory}/classifications</classificationDirectory>
                                <classificationFiles>
                                        <!-- supports wildcard or name pattern -->
                                        <param>sample.properties</param>
                                        <param>other.properties</param>
                                </classificationFiles>
                                <!-- optional, set true to group features by its Ids -->
                                <mergeFeaturesById>false</mergeFeaturesById>
                                <!-- optional, set true to get a final report with latest results of the same test from different test runs -->
                                <mergeFeaturesWithRetest>false</mergeFeaturesWithRetest>
                                <!-- optional, set true to fail build on test failures -->
                                <checkBuildResult>false</checkBuildResult>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>

