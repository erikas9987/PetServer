Feature: User Login

  Scenario: Successfully logging in
    Given an existing user with email "user@example.com" and password "password123"
    When the user logs in with email "user@example.com" and password "password123"
    Then the user should be logged in successfully

  Scenario: Unsuccessfully logging in due to incorrect password
    Given an existing user with email "user@example.com" and password "password123"
    When the user tries to log in with email "user@example.com" and password "wrongPassword"
    Then the login should fail
    And the user should receive a "wrong password" error message
