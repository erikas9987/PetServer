Feature: Add Pet

  Scenario: Successfully adding a pet to the user
    Given a logged-in user with email "user@example.com"
    And no existing pet named "Buddy"
    When the user adds a pet named "Buddy"
    Then the pet "Buddy" should be added to the user's profile
