package courses.restbdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import courses.Applicant;
import courses.CourseAnnouncement;
import courses.httpinterface.CourseRequestObject;
import courses.playwright.CoursePage;
import courses.playwright.CoursesPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.UUID;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("courses")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "courses.restbdd")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoursesIT {

    @LocalServerPort
    private int port;

    @Autowired
    RestClient.Builder restClientBuilder;

    CourseRequestObject client;

    String code;

    String result;

    @Before
    public void setupClient() {
        var restClient = restClientBuilder
                .baseUrl("http://localhost:%d".formatted(port))
                .build();
        var factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();
        client = factory.createClient(CourseRequestObject.class);
    }

    @Given("a course with {int} limit")
    public void a_course_with_limit(Integer limit) {
        code = "course-" + UUID.randomUUID();
        log.info("Code: {}", code);
        client.announceCourse(new CourseAnnouncement(code, "Java", limit));
    }
    @Given("{int} students already enrolled")
    public void students_already_enrolled(Integer number) {
        for (int i = 0; i < number; i++) {
            client.enroll(code, new Applicant("John Doe %d".formatted(i)));
        }
    }
    @When("a new student tries to enroll")
    @SneakyThrows
    public void a_new_student_tries_to_enroll() {
        try {
            client.enroll(code, new Applicant("John Doe Overflow"));
        }catch (HttpClientErrorException e) {
            ObjectMapper mapper = new ObjectMapper();
            ProblemDetail problem = mapper.readValue(e.getResponseBodyAsString(), ProblemDetail.class);
            result = problem.getDetail();
        }


    }
    @Then("the enrollment should be denied, and the message is {string}")
    public void the_enrollment_should_be_denied(String message) {
        assertEquals(message, result);
    }
}
