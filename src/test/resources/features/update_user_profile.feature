Feature: Update User Profile

  Scenario: Successfully updating user profile
    Given a logged-in user with email "user@example.com"
    When the user updates their profile name to "New Name"
    Then the user profile should be updated successfully

  Scenario: Unsuccessful profile update due to unauthorized access
    Given a user with email "user@example.com" is not logged in
    When the user attempts to update their profile name to "New Name"
    Then the profile update should fail
    And the user should receive an "unauthorized access" error message