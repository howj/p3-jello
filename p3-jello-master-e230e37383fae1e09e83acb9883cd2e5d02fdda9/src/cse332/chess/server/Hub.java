package cse332.chess.server;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cse332.chess.gui.Config;
import cse332.chess.gui.EasyMenuFrame;
import cse332.chess.gui.GamePanel;
import cse332.chess.gui.MessagePanel;
import chess.play.EasyChess;

public class Hub extends JPanel {
    public class GameState {
        public String channel;
        public boolean imWhite;
        public boolean imPlaying;
        public String white;
        public String black;
        public String whiteTime;
        public String blackTime;
        public String board;
        public String currentPlayer;
        public String lastMove;

        public GameState(Object value, String name) {
            String[] pieces = ((String) value).split(":");
            String setter = pieces[1].trim().split("!")[0];
            if (!setter.equals(ChessServerConnection.CHESS_RUNNER)) {
                throw new RuntimeException();
            }
            channel = pieces[1].trim().split(" ")[2];
            white = pieces[3].trim().split(" ")[0];
            imWhite = white.equals(name);
            black = pieces[4].trim().split(" ")[0];
            imPlaying = imWhite || black.equals(name);
            whiteTime = pieces[5].trim().split(" ")[0];
            blackTime = pieces[6].trim().split(" ")[0];
            board = pieces[7].trim().split(" ")[0];
            currentPlayer = pieces[7].trim().split(" ")[1];
            lastMove = pieces[8].trim().split(" ")[0];
        }
    }

    private static final long serialVersionUID = -47361648262042927L;

    private Frame frame;

    public EasyChess applet;

    private MessagePanel messagePanel;

    private GamePanel gamePanel;

    private transient ChessServerConnection connection;

    private char cPromote = 'Q';

    private boolean boolLoggedIn = false;

    public static final boolean boolTimestampPossible = false;

    public String myName = "";

    public GameState gameState;
    public boolean isSetup = false;

    public String whoToMatch;

    public Hub(EasyChess app, String who) {
        applet = app;
        whoToMatch = who;
        messagePanel = new MessagePanel(this);

        gamePanel = new GamePanel(this);

        this.setBackground(Config.colorBackground);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        gamePanel.setPreferredSize(new Dimension(500, 500));

        this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, gamePanel,
                messagePanel));
    }

    public synchronized void sendCommand(String cmd) {
        connection.send(cmd.trim(), "");
    }

    public synchronized void sendCmdArgs(String cmd, String args) {
        connection.send(cmd.trim(), args.trim());
    }

    public synchronized void sendCommandAndEcho(String cmd, String text) {
        messagePanel.addMessage("> " + text.trim());
        connection.send(cmd.trim(), text.trim());
    }

    public void addMessage(String s) {
        if (boolLoggedIn) {
            messagePanel.addMessage(s);
        }
    }

    public void addChat(String s) {
        messagePanel.addChat(s);
    }

    public void receive(String cmd, Object value) {
        switch (cmd) {
        case "AUTH":
            if ((boolean) value) {
                myName = connection.getNickname();
                boolLoggedIn = true;
                if (applet != null) {
                    frame = new EasyMenuFrame("EasyChess: " + myName, this);
                    frame.add(this);
                    frame.setSize(800, 600);
                    frame.setVisible(true);

                    messagePanel.giveFocus();
                }
            } else if (applet != null) {
                applet.loginFailed("Bad User Credentials.");
            }
            else {
                System.out.println("Login Failed! Bad User Credentials!");
            }

            if (applet == null) {
                connection.send("", "match " + whoToMatch);
            }
        break;
        case "MAIN":
            messagePanel.receive("MAIN", value);
            break;
        case "ILLEGAL":
            messagePanel.receive("ILLEGAL", value);
        break;
        case "GAME_REQUEST":
            messagePanel.receive("ADD_MATCH", value);
        break;
        case "GAME_SETUP":
            try {
                gameState = new GameState(value, myName);
            } catch (Exception e) {
                e.printStackTrace();
            }
           
            String move = gameState.lastMove;
            gamePanel.receive("MADE_MOVE", move);
            
            if (!gameState.imPlaying) {
                gamePanel.receive("STARTED_OBSERVING", myName);
            }
        break;
        case "MOVE":
        	/* no longer necessary as of 17wi; superceded by GAME_SETUP! */
        break;
        case "START":
            gamePanel.receive("GAME_STARTED", myName);
        break;
        case "END":
            gamePanel.receive("GAME_OVER", myName);
            connection.setGameChannel(null);
            if (applet == null) {
                System.exit(1);
            }
        break;
        }
    }

    public boolean login(String username, String password) {
        username = username.toLowerCase();
        if (applet != null && username == null || "".equals(username)) {
            applet.loginFailed("Please enter your team name.");
            return false;
        }
        try {
            connection = new ChessServerConnection(this, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            if (applet != null) {
                applet.loginFailed("Exception!  Please contact someone on course staff.");
            }
        }
        if (applet != null) {
            applet.loginSucceeded("Connected.");
            return true;
        }
        return false;
    }

    public void killConnection() {
        addMessage("Shutting down...");
        messagePanel.shutdown();
        gamePanel.shutdown();
        if (applet != null) {
            applet.loginFailed("Disconnected");
        }
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public void setPromote(char c) {
        cPromote = c;
    }

    public char getPromote() {
        return cPromote;
    }
}
