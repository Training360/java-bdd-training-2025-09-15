package courses.playwrightbdd;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import courses.playwright.CoursePage;
import courses.playwright.CoursesPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("courses")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "courses.playwrightbdd")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CoursesIT {

    @LocalServerPort
    int port;
    static Playwright playwright;
    static Browser browser;
    Page page;
    CoursePage coursePage;

    @Before
    public void init() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        page = browser.newPage();
    }

    @After
    public void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }

    String code;

    @Given("a course with {int} limit")
    public void a_course_with_limit(Integer limit) {
        CoursesPage coursesPage = new CoursesPage(page);
        coursesPage.navigate(port);
        code = "course-" + UUID.randomUUID();
        coursesPage.announce(code, "Java", limit);
    }
    @Given("{int} students already enrolled")
    public void students_already_enrolled(Integer number) {
        coursePage = new CoursePage(page);
        coursePage.navigate(port, code);
        for (int i = 0; i < number; i++) {
            coursePage.enroll("Student " + i);
        }
    }
    @When("a new student tries to enroll")
    public void a_new_student_tries_to_enroll() {
        coursePage.enroll("John Doe Overflow");
    }
    @Then("the enrollment should be denied, and the message is {string}")
    public void the_enrollment_should_be_denied(String message) {
        coursePage.gotAlert(message);
    }
}
