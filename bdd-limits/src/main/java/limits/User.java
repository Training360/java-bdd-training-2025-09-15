package limits;

public class User {

    int availableTokens;

    public User(int availableTokens) {
        this.availableTokens = availableTokens;
    }

    public void tryToUse(int token) {
        if (availableTokens >= token) {
            availableTokens -= token;
        }
        else {
            availableTokens = 0;
        }
    }

    public int availableTokens() {
        return availableTokens;
    }
}
