package cse332.chess.server;

import java.io.*;
import java.net.*;

public class ChessServerConnection extends Thread {
    private final static String SERVER = "chess.countablethoughts.com";
    private final static String SERVER_PASSWORD = "jTeaT.xdcLJG4Kk>>H2TNZ4eeb}Da7";
    public final static String CHESS_RUNNER = "chezz";
    public final static String CHAT_CHANNEL = "#main";

    private String username;
    private String nick;

    private BufferedWriter out;
    private BufferedReader in;
    
    private Hub hub;
    
    private String gameChannel;

    public ChessServerConnection(Hub hub, String username, String password)
            throws UnknownHostException, IOException {
        
        this.hub = hub;
        
        this.username = username;
        
        int num = 0;

        // Connect directly to the IRC server.
        @SuppressWarnings("resource")
        Socket socket = new Socket(SERVER, 9001);
        out = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Log on to the server.
        write("PASS", SERVER_PASSWORD);
        write("NICK", username);
        write("USER", username, "-", "-", "-");

        // Read lines from the server until it tells us we have connected.
        String line = null;
        boolean logged_in = false;
        while ((line = in.readLine()) != null) {
            int code = Integer.parseInt(line.split(" ")[1]);
            switch (code) {
            case IRCCodes.RplMyInfo:
                nick = line.split(" ")[2];
                logged_in = true;
                break;

            case IRCCodes.ErrNickNameInUse:
                write("NICK", username + (num++));
                break;
            }

            if (logged_in) {
                break;
            }
        }

        // Join the channel and authenticate.
        write("JOIN", CHAT_CHANNEL);
        m_chess("AUTH", username, password);
        this.start();
    }
    
    public String getAccountName() {
        return this.username;
    }
    
    public String getNickname() {
        return this.nick;
    }
    
    public String getGameChannel() {
        return this.gameChannel;
    }
    
    public void setGameChannel(String gameChannel) {
        this.gameChannel = gameChannel;
    }

    public void send(String cmd, String text) {
        try {
            cmd = cmd.trim();
            switch (cmd) {
                case "MAIN":
                    m_channel(CHAT_CHANNEL, text);
                break;
                case "GAME":
                    m_channel(gameChannel, text);
                break;
                case "MOVE":
                    m_channel(gameChannel, "\u0001ACTION " + text + "\u0001");
                break;
                default:
                    String[] parts = text.split(" ", 2);
                    cmd = parts[0].trim();
                    switch (cmd) {
                        // I am sending a match request.
                        case "match":
                            if (parts.length == 1) {
                                hub.addMessage("Usage: match <name>");
                            }
                            else {
                                text = parts[1];
                                m_chess("start", text);
                                this.gameChannel = "REQUESTED";
                            }
                        break;
                        case "watch":
                            if (parts.length == 1 || !parts[1].startsWith("#")) {
                                hub.addMessage("Usage: watch #<gamenumber>");
                            }
                            else {
                                write("JOIN", parts[1]);
                            }
                        break;
                        case "accept":
                            if (parts.length == 1 || !parts[1].startsWith("#")) {
                                hub.addMessage("Usage: accept #<gamenumber>");
                            }
                            else {
                                text = parts[1];
                                this.gameChannel = text;
                                write("JOIN", gameChannel);
                            }
                        break; 
                        case "who":
                            m_chess("ONLINE", "");
                            hub.addMessage("Online Players:");
                        break;
                        case "games":
                            m_chess("LIST", "all");
                            hub.addMessage("Current Games:");
                        break;
                        case "scores":
                            if (parts.length == 1) {
                                hub.addMessage("Usage: scores <botname>");
                            }
                            else {
                                text = parts[1];
                                m_chess("SCORES", text);
                            }
                        break;
                        case "help":
                            hub.addMessage("You can use the following commands:");
                            hub.addMessage("    Send A Challenge:    \"" + "match <name>\"");
                            hub.addMessage("    Accept Challenge:    \"" + "accept #<gamenumber>\"");
                            hub.addMessage("    Watch A Game:        \"" + "watch #<gamenumber>\"");
                            hub.addMessage("    List Online Players: \"" + "who\"");
                            hub.addMessage("    List Current Games:  \"" + "games\"");
                            hub.addMessage("    Show Games Results:  \"" + "scores <botname>\"");
                        break;
                    }
            }
        } catch(Exception e) {
            e.printStackTrace();
            hub.killConnection();
        }
    }

    public void write(String... args) throws IOException {
        StringBuilder str = new StringBuilder();

        int i = 0;
        while (i < args.length - 1) {
            str.append(args[i] + " ");
            i++;
        }
        if (args.length > 0) {
            str.append(args[i]);
        }
        out.write(str.toString());
        out.write("\r\n");
        out.flush();
    }

    public void m_channel(String channel, String msg) throws IOException {
        write("PRIVMSG", channel, ":" + msg);
    }

    public void m_chess(String... args) throws IOException {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            msg.append(args[i] + " ");
        }
        if (args.length > 0) {
            msg.append(args[args.length - 1]);
        }
        write("PRIVMSG", CHESS_RUNNER, ":" + msg.toString());
    }

    public void run() {
        try {
        String line = null;
        // Keep reading lines from the server.
        while ((line = in.readLine()) != null) {
            if (line.startsWith("PING")) {
                write("PONG", line.substring(5));
            } 
            else if (line.startsWith(":")) {
                String[] parts = line.split(" ");
                String command = parts[1];
                String who = null;
                String channel = null;

                switch (command) {
                case "PRIVMSG":
                    who = parts[0].split("!")[0].substring(1);

                    channel = parts[2];
                    String contents = line.split(":")[2];
                    String cmd = contents.split(" ")[0];
                    if (who.equals(CHESS_RUNNER)) {
                        switch (cmd) {
                        case "AUTHSUCCESS":
                            hub.receive("AUTH", true);
                        break;
                        case "AUTHFAIL":
                            hub.receive("AUTH", false);
                        break;
                        case "START":
                            hub.receive("START", null);
                        break;
                        case "OPPBUSY":
                            hub.addMessage("That player is currently in a game.  Try again later.");
                        break;
                        case "PLAYSELF":
                            hub.addMessage("You can't match yourself!");
                        break;
                        case "NEEDAUTH":
                            hub.addMessage("That isn't a valid player.  Try someone else.");
                        break;
                        case "GAMELIST":
                            hub.addMessage("    " + line.split(":GAMELIST ")[1]);
                        break;
                        case "SCORES":
                            String[] scores = line.split(":SCORES ")[1].split(" ", 2);
                            if (scores.length > 1) {
                        	    hub.addMessage("    " + scores[1]);
                            }
                            else {
                        	    hub.addMessage("    <no games played>");
                            }
                        break;
                        case "PLAY_WITH":
                            String challenger = contents.split(" ")[1];
                            String chan = contents.split(" ")[2];
                            if (gameChannel == null) { 
                                hub.receive("GAME_REQUEST", challenger + " has challenged you to a game: " + chan);
                            }
                        break;
                        case "NICK":
                            hub.addMessage("    " + contents.split(" ", 2)[1]);
                        break;
                        case "DRAW":
                            hub.receive("END", null);
                            hub.addMessage("The game is a draw!");
                        break;
                        case "WIN":
                            hub.receive("END", null);
                            String[] parts2 = contents.split(" ");
                            String winner = parts2[1];
                            String message = parts2[2];
                            boolean iWon = winner.equals(nick);
                            
                            switch (message) {
                                case "PART":
                                case "QUIT":
                                case "NICKCHANGE":
                                    if (!iWon && !nick.equals(winner)) {
                                        hub.addMessage(winner + " won!"); 
                                    }
                                    else if (iWon) {
                                        hub.addMessage("You win! " + parts2[3] + " left the game.");
                                    }
                                    else {
                                        hub.addMessage("Oh no! You quit the game. " + winner + " wins!");
                                    }
                                break;
                                case "ILLEGAL":
                                    if (!iWon && !hub.gameState.imPlaying) {
                                        hub.addMessage(winner + " won!"); 
                                    }
                                    else if (iWon) {
                                        hub.addMessage("You win! " + parts2[3] + " made an illegal move.");
                                    }
                                    else {
                                        hub.addMessage("Oh no! You made an illegal move. " + winner + " wins!");
                                    }
                                break;
                                case "NOTIME":
                                    if (!iWon && !hub.gameState.imPlaying) {
                                        hub.addMessage(winner + " won!"); 
                                    }
                                    else if (iWon) {
                                        hub.addMessage("You win! " + parts2[3] + " ran out of time.");
                                    }
                                    else {
                                        hub.addMessage("Oh no! You ran out of time. " + winner + " wins!");
                                    }

                                break;
                                case "CHECKMATE":
                                    if (!iWon && !hub.gameState.imPlaying) {
                                        hub.addMessage(winner + " won!"); 
                                    }
                                    else if (iWon) {
                                        hub.addMessage("You win! Checkmate!");
                                    }
                                    else {
                                        hub.addMessage("Oh no! You lose.");
                                    }
                                break;
                            }
                        break;
                        case "YOUBUSY":
                            hub.addMessage("Your account is logged on elsewhere and in a match!");
                        break;
                        }
                    } 
                    else if (channel.equals(CHAT_CHANNEL)){
                        hub.receive("MAIN", "(@everyone) " + who + ": " + contents);
                    }
                    else if (channel.equals(gameChannel)){
                        if (contents.startsWith("\u0001ACTION ") &&
                           contents.endsWith("\u0001")) {
                           hub.receive("MOVE", contents.substring(8, contents.length() - 1));
                        }
                        else {
                            hub.receive("MAIN", who + ": " + contents);
                        }
                    }
                break;
                case "INVITE":
                    who = parts[0].split("!")[0].substring(1);
                    channel = parts[parts.length - 1]; 
                    if (who.equals(CHESS_RUNNER)) {
                        // If I REQUESTED, just join the channel
                        if (this.gameChannel != null) {
                            this.gameChannel = channel;
                            write("JOIN", gameChannel);
                        }
                    }
                break;
                case "TOPIC":
                    hub.receive("GAME_SETUP", line);
                break;
                }
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            hub.killConnection();
        }
    }
}