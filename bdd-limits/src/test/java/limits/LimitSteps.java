package limits;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LimitSteps {

    User user;

    @Given("I have {int} tokens available")
    public void iHaveTokensAvailable(int tokens) {
        user = new User(tokens);
    }

    @When("I try to use {int} tokens")
    public void iTryUseTokens(int tokens) {
         user.tryToUse(tokens);
    }

    @Then("I should have {int} tokens available")
    public void iShouldHaveTokensAvailable(int expectedTokens) {
        assertTrue(expectedTokens == user.availableTokens());
    }

    @DataTableType
    public UserWithName userWithName(Map<String, String> map) {
        return new UserWithName(map.get("name"), Integer.parseInt(map.get("tokens")));
    }

    @Given("the user has tokens")
    public void theUserHasTokens(DataTable dataTable) {
        //var map = dataTable.asMap();
        //System.out.println(map);

        var list = dataTable.asList(UserWithName.class);
        System.out.println(list);
    }
}
