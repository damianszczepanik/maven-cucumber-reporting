package com.example.test.cucumber;

import static org.fest.assertions.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.en.Given;

public class SimpleSteps {

	@Autowired
	public Holder holder;
	
	@Given("^I have no widgets$")
	public void setAWidget(){
	  holder.setValue(0);
	}
	
	@Given("^I add (\\d+) widgets$")
	public void addAWidget(int value){
	  holder.setValue(holder.getValue() + value);
	}

	@Given("^I break (\\d+) widgets$")
	public void breakAWidget(int value){
		if (value > holder.getValue()) throw new RuntimeException("Dont have that many widgets :(");
	  holder.setValue(holder.getValue() - value);
	}

	@Given("^I have (\\d+) widgets$")
	public void haveWidgets(int value){
		assertThat(holder.getValue()).isEqualTo(value);
	}
	
	
}
