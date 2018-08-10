[![Build Status](https://img.shields.io/travis/damianszczepanik/maven-cucumber-reporting/master.svg)](https://travis-ci.org/damianszczepanik/maven-cucumber-reporting)
[![Maven Central](https://img.shields.io/maven-central/v/net.masterthought/maven-cucumber-reporting.svg)](http://search.maven.org/#search|gav|1|g%3A%22net.masterthought%22%20AND%20a%3A%22maven-cucumber-reporting%22)
[![Maven Dependencies](https://www.versioneye.com/user/projects/55d0942d15ff9b00220000f6/badge.svg)](https://www.versioneye.com/user/projects/55d0942d15ff9b00220000f6?child=summary)


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
                                <parallelTesting>false</parallelTesting>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>

