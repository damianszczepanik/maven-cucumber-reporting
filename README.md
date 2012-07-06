Maven mojo for the cucumber-reporting

     <build>
            <plugins>
                <plugin>
                    <groupId>net.masterthought</groupId>
                    <artifactId>maven-cucumber-reporting</artifactId>
                    <version>0.0.1</version>
                    <executions>
                        <execution>
                            <id>execution</id>
                            <phase>verify</phase>
                            <goals>
                                <goal>generate</goal>
                            </goals>
                            <configuration>
                                <projectName>cucumber-jvm-example</projectName>
                                <outputDirectory>${project.build.directory}/cucumber-html-reports</outputDirectory>
                                <cucumberOutput>${project.build.directory}/cucumber.json</cucumberOutput>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
