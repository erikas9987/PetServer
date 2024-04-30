Feature: Illness Inquiry

  Scenario: Successfully fetching illness details
    Given the illness "Canine Parvovirusr" is recorded in the system
    When a user requests details for "Canine Distemper"
    Then the details of "Canine Parvovirus" should be provided
