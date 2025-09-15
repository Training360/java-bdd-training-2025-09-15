package limits;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LimitsTest {

    @Test
    void hasAvailableTokens() {
        // Given
        User user = new User(1000);
        // When
        user.tryToUse(200);
        // Then
        assertEquals(800, user.availableTokens());
    }

    @Test
    void hasExactlyAvailable() {
        var user = new User(200);
        user.tryToUse(200);
        assertEquals(0, user.availableTokens());
    }

    @Test
    void hasAvailableTokensCloseTo() {
        var user = new User(200);
        user.tryToUse(199);
        assertEquals(1, user.availableTokens());
    }

    @Test
    void dontHaveEnoughAvailableTokens() {
        var user = new User(200);
        user.tryToUse(300);
        assertEquals(0, user.availableTokens());
    }
}
