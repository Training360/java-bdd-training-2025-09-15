Feature: Limit the enrollments of the course

  Scenario: Limit the number of enrollments
    Given a course with 5 limit
    And 5 students already enrolled
    When a new student tries to enroll
    Then the enrollment should be denied, and the message is "Course is full"

