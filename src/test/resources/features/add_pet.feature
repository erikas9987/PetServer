Feature: Add Pet

  Scenario: Successfully adding a pet to the user
    Given a logged-in user with email "user@example.com"
    And no existing pet named "Buddy"
    When the user adds a pet named "Buddy"
    Then the pet "Buddy" should be added to the user's profile

  Scenario: Unsuccessfully adding a pet due to existing pet name
    Given a logged-in user with email "user@example.com"
    And an existing pet named "Buddy"
    When the user tries to add another pet named "Buddy"
    Then the pet addition should fail
    And the user should receive a "pet name already exists" error message
