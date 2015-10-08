import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TweetTest {
    PreparedStatement preparedStatement = null;
    DbConnection connection = new DbConnection();
    User user;

    @Before
    public void beforeEach(){
        user = generateUser("foo_example", "123456789", connection);
    }

    @After
    public void afterEach(){
        deleteAllTweets();
        deleteAllUsers();
    }

    @Test
    public void newCreatesATweetButDoesNotSaveInDatabase(){
        int countBefore = getTweetsCount();
        new Tweet("hello", user.getId(), this.connection);
        int countAfter  = getTweetsCount();
        assertEquals(countBefore, countAfter);
    }

    @Test
    public void newCreatesATweetButSetsIdNull(){
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        assertEquals(tweet.getId(), null);
    }

    @Test
    public void saveOnValidTweetIncreasesTweetCountBy1(){
        int countBefore = getTweetsCount();
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        tweet.save();
        int countAfter  = getTweetsCount();
        assertNotEquals(countBefore, countAfter);
    }

    @Test
    public void saveOnInvalidTweetKeepsUserCountSame(){
        int countBefore = getTweetsCount();
        String tweetBody = getInvalidTweetBody();
        Tweet tweet = new Tweet(tweetBody, user.getId(), this.connection);
        tweet.save();
        int countAfter  = getTweetsCount();
        assertEquals(countBefore, countAfter);
    }

    @Test
    public void saveOnValidTweetReturnsTweet(){
        Tweet tweet = new Tweet("hello", user.getId(), this.connection);
        Tweet savedTweet = tweet.save();
        assertEquals(savedTweet.getClass().getName(), "Tweet");
        assertEquals(savedTweet.getId().getClass()
                .getSimpleName(), "Integer");
        assertEquals(savedTweet.getBody(), "hello");
        assertEquals(savedTweet.getUserId(), user.getId());
    }

    @Test
    public void saveOnInvalidTweetReturnsNull(){
        String tweetBody = getInvalidTweetBody();
        Tweet tweet = new Tweet(tweetBody, user.getId(), this.connection);
        assertEquals(tweet.save(), null);
    }

    @Test
    public void saveOnTweetWithInvalidUserIdReturnsNull(){
        Tweet tweet = new Tweet("hello", 99911223, this.connection);
        assertEquals(tweet.save(), null);
    }

    @Test
    public void saveOnTweetWithInvalidBodyReturnsNull(){
        String invalidBody = getInvalidTweetBody();
        Tweet tweet = new Tweet(invalidBody, 99911223, this.connection);
        assertEquals(tweet.save(), null);
    }

    private User generateUser(String username, String password,
                              DbConnection connection){
        return (new User(username, password, connection)).save();
    }

    private void deleteAllUsers(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM users");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllTweets(){
        try {
            preparedStatement = this.connection
                    .prepareStatement("DELETE FROM tweets");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getTweetsCount(){
        int count = 0;
        try {
            Statement countStatement = this.connection.createStatement();
            ResultSet res = countStatement.executeQuery("SELECT COUNT(*) AS total FROM tweets");
            if( res.next() ) count = res.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private String getInvalidTweetBody(){
        String tweetBody = "Lorem ipsum dolor sit amet, consectetur adipisicing elit." +
                "Rem, incidunt eos delectus veniam cupiditate possimus in velit, quia"     +
                " sed perspiciatis similique suscipit tempora laborum reprehenderit "      +
                "maxime nulla. Maiores, id, error.\n"                                      +
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Recusandae "    +
                "beatae nostrum maiores voluptatum atque repellat necessitatibus ullam "   +
                "molestias, mollitia neque quidem molestiae totam commodi ut sed dolorum." +
                " Adipisci amet, molestias.\n"                                             +
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum "       +
                "incidunt tenetur error in veniam, vitae aut aliquid repellat dolores "    +
                "alias necessitatibus nobis quidem unde ducimus. Repudiandae mollitia "    +
                "nostrum, possimus velit.";
        return tweetBody;
    }

}