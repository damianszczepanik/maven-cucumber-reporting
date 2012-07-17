package com.example.test.cucumber;

import org.junit.runner.RunWith;

import cucumber.junit.Cucumber;
import cucumber.junit.Cucumber.Options;

@RunWith(Cucumber.class)
@Options(dryRun=false, features={"classpath:"}, glue="com.example.test.cucumber",format={"json:target/cucumber2.json","junit:target/surefire-reports/report.xml"}, monochrome=true, tags="@Desktop")
public class BasicITCase {

}
