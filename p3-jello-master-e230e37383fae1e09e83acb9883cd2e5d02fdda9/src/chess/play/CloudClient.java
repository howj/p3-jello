package chess.play;

import cse332.chess.server.Hub;

/**
 * You can use this client to have your bot automatically
 * start a match when run.  This will be particularly useful
 * when you want to have your bot play on a cloud instance. To
 * see how the game is going, you should observe the game using
 * the normal EasyChess interface.
 */
public class CloudClient {
    public static void main(String[] args) {
        String username = "jello";
        String password = "rWc7SQGjti";
        String botToPlay = "clamps";

        System.out.println("Starting a match against " + botToPlay);
        Hub hub = new Hub(null, botToPlay);
        hub.login(username, password);
    }
}
