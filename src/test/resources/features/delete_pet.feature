Feature: Delete Pet

  Scenario: Successfully deleting a pet
    Given a logged-in user with email "user@example.com" and a pet named "Buddy"
    When the user deletes the pet named "Buddy"
    Then the pet "Buddy" should be removed from the user's profile
