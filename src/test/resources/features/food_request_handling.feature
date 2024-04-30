Feature: Handle Food Request

  Scenario: Successfully fetching food details
    Given the food "Beef" exists in the system
    When a user requests details for "Beef"
    Then the details of "Beef" should be provided
