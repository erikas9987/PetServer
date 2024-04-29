Feature: User Registration

  Scenario: Successful user registration with token issuance
    Given there is no existing user with email "user@example.com"
    When a new user registers with email "user@example.com" and password "securePassword123"
    Then the user should be successfully registered and receive a token