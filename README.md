Maven mojo for the cucumber-reporting - put this into your pom.xml and run mvn clean install or mvn clean test and cucumber reports will be generated in target/cucumber-html-reports

Read more about the project and configuration here: [maven-cucumber-reports](http://masterthought.net/section/cucumber-reporting)

Run with: mvn clean install

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
                    <version>0.0.5</version>
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
                                <enableFlashCharts>false</enableFlashCharts>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>

You may also need the maven dependency:

        <dependency>
            <groupid>net.masterthought</groupid>
            <artifactid>maven-cucumber-reporting</artifactid>
            <version>0.0.24</version>
        </dependency>

You may also need to add the sonatype repository if it has not yet been synced with the maven central repository:

           <repositories>
                <repository>
                    <id>sonatype-releases</id>
                    <url>https://oss.sonatype.org/content/repositories/releases/</url>
                </repository>
            </repositories>

You may also need to include the Totally Lazy dependency and repository:

       <repositories>
           <repository>
               <id>repo.bodar.com</id>
               <url>http://repo.bodar.com</url>
           </repository>
       </repositories>

       <dependency>
               <groupId>com.googlecode.totallylazy</groupId>
               <artifactId>totallylazy</artifactId>
               <version>1.20</version>
       </dependency>

