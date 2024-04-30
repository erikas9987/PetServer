Feature: User Registration

  Scenario: Successful user registration with token issuance
    Given there is no existing user with email "user@example.com"
    When a new user registers with email "user@example.com" and password "securePassword123"
    Then the user should be successfully registered and receive a token

  Scenario: Unsuccessful registration due to missing password
    Given there is no existing user with email "user@example.com"
    When a new user attempts to register with email "user@example.com" and no password
    Then the registration should fail
    And the user should receive a "password is required" error message