Feature: Handling token limits

  As a user of the system
  I want to know the available tokens
  So that I can manage my usage effectively

  Rule: I have enough token

      Scenario: Decrease tokens on usage with big difference
        Given I have 1000 tokens available
        When I try to use 200 tokens
        Then I should have 800 tokens available

      Scenario: Decrease tokens on usage result 0
        Given I have 200 tokens available
        When I try to use 200 tokens
        Then I should have 0 tokens available

      Scenario: Decrease tokens on usage with little difference
        Given I have 200 tokens available
        When I try to use 199 tokens
        Then I should have 1 tokens available

  Rule: I don't have enough token

    Scenario: Decrease tokens on usage
      Given I have 200 tokens available
      When I try to use 300 tokens
      Then I should have 0 tokens available

    Scenario: I have no token
      Given I have 0 tokens available
      When I try to use 300 tokens
      Then I should have 0 tokens available

    Scenario: Decrease tokens on usage
      Given I have 200 tokens available
      When I try to use 201 tokens
      Then I should have 0 tokens available

    Scenario Outline: Decrease tokens
      Given I have <tokens> tokens available
      When I try to use <cost> tokens
      Then I should have <available> tokens available
      Examples:
        |tokens|cost|available|
        |1000  |200|800      |
        |200  |200|0      |



    Scenario:
      Given the user has tokens
        |name|tokens|
        |John|   200|
        |Jack|   230|
        |Jane|   240|
