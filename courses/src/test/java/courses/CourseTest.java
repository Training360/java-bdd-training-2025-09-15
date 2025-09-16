package courses;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseTest {

    @Test
    void courseIsFull() {
        var course = new Course("java-bgn", "Java programoming", 5);
        for (int i = 0; i < 5; i++) {
            course.enroll("John Doe " + UUID.randomUUID());
        }
        assertThrows(IllegalStateException.class, () -> course.enroll("Jane Doe"));

    }
}
