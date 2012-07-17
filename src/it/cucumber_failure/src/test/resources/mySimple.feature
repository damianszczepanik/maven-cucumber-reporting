@UnusedTag
Feature: Simple Widget Counting Test
  Lets count some widgets!!!

  Scenario Outline: widget counting.
    Given I have no widgets
    And I add <baseCount> widgets
    When I break <breakWidgets> widgets
    Then I have <count> widgets
    
    Examples:  widget counts
    | baseCount | breakWidgets | count |
    | 15        | 5            | 10    |
    | 12        | 5            | 7     |
    | 2         | 1            | 1     |    
    | 5         | 5            | 0     |    
    | 13        | 5            | 7     |    
    | 42        | 29           | 12    |
    
        