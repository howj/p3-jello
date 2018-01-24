package cse332.chess.gui;

//package com.chessclub.easychess;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cse332.chess.interfaces.Move;
import cse332.chess.server.ChessServerConnection;
import cse332.chess.server.Hub;
import chess.setup.Engine;

class GameState {
    public String channel;
    public String white;
    public String black;
    public String whiteTime;
    public String blackTime;
    public String board;

    public GameState(Object value) {
        String[] pieces = ((String) value).split(":");
        String setter = pieces[1].trim().split("!")[0];
        if (!setter.equals(ChessServerConnection.CHESS_RUNNER)) {
            throw new RuntimeException();
        }
        channel = pieces[1].trim().split(" ")[2];
        white = pieces[3].trim().split(" ")[0];
        black = pieces[4].trim().split(" ")[0];
        whiteTime = pieces[5].trim().split(" ")[0];
        blackTime = pieces[6].trim().split(" ")[0];
        board = pieces[7].trim().split(" ")[0];
    }
}

public final class GamePanel extends JPanel implements Observer {
    /**
   * 
   */
    private static final long serialVersionUID = -6731471373081937898L;

    private Hub hub;

    private Board board;

    private ChessClock lowerClock, upperClock;

    private JLabel lowerName, upperName;

    private JButton buttonDraw, buttonAccept;

    private Component button1 = null, button2 = null;

    private boolean lowersTurn = true;

    private boolean amPlaying = false;

    private LinkedList<String> pendingMatches = new LinkedList<String>();

    private transient Engine m_player;

    public GamePanel(Hub h) {
        this.setBackground(Config.colorGame);
        hub = h;

        board = new Board(h);
        lowerClock = new ChessClock();
        upperClock = new ChessClock();
        lowerName = new JLabel(" \n ");
        upperName = new JLabel(" \n ");

        Font f = new Font("Helvetica", Font.PLAIN, 14);
        lowerName.setFont(f);
        upperName.setFont(f);
        f = new Font("Helvetica", Font.BOLD, 18);
        lowerClock.setFont(f);
        upperClock.setFont(f);

        this.setLayout(new GamePanelLayout(getSize().width, getSize().height));

        this.add("Board", board);
        this.add("UpperClock", upperClock);
        this.add("UpperName", upperName);
        this.add("LowerName", lowerName);
        this.add("LowerClock", lowerClock);

        buttonDraw = new JButton("Draw?");
        buttonDraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hub.sendCommand("draw");
            }
        });
        buttonAccept = new JButton("Accept");
        buttonAccept.setBackground(Color.yellow);
    }

    private void rescindDraw() {
        if (amPlaying) {
            buttonDraw.setText("Draw?");
            buttonDraw.setBackground(Color.cyan);
        }
    }

    private void updateAcceptButton() {
        if (pendingMatches.isEmpty()) {
            if (button1 == buttonAccept) {
                this.remove(buttonAccept);
                button1 = null;
                doLayout();
            }
        } else {
            String s = pendingMatches.getLast();
            buttonAccept.setText("Accept " + s);
            if (button1 != buttonAccept) {
                hub.sendCommand("accept " + s);
            }
        }
    }

    private void addMatch(String who) {
        if (who.equals(hub.myName))
            return;

        pendingMatches.addLast(who);
        updateAcceptButton();
    }

    public void receive(String cmd, Object value) {
        if (!"FLIP ILLEGAL_MOVE SET_CLOCK".contains(cmd)) {
            rescindDraw();
        }

        switch (cmd) {
        case "GAME_STARTED":
            String nick = (String) value;
            this.amPlaying = hub.gameState.white.equals(nick)
                    || hub.gameState.black.equals(nick);
        case "STARTED_OBSERVING":
            if (button2 != null) {
                this.remove(button2);
                button2 = null;
            }
            this.doLayout();

            board.startGame();

            if (amPlaying) {
                m_player = new Engine(hub, 60000, 1000);
                m_player.addBestMoveObserver(this);
                board.newGame();
            } 
            else {
                board.newGame(hub.gameState.board);
                lowerClock.setClock(Long.parseLong(hub.gameState.whiteTime));
                upperClock.setClock(Long.parseLong(hub.gameState.blackTime));
                if (hub.gameState.currentPlayer.equals("w")) {
                    lowerClock.startClock();
                    upperClock.stopClock();
                } 
                else {
                    upperClock.startClock();
                    lowerClock.stopClock();
                }
            }


            lowersTurn = true;

            lowerName.setText(hub.gameState.white);
            upperName.setText(hub.gameState.black);

            if (amPlaying) {
                lowerClock.startClock();
            }

            upperClock.stopClock();
            board.repaint();

            String nick1 = (String) value;
            if (hub.gameState.black.equals(nick1)) {
                board.setFlipped(true);
                ChessClock.swap(lowerClock, upperClock);
                lowerClock.repaint();
                upperClock.repaint();
                String hold = upperName.getText();
                upperName.setText(lowerName.getText());
                lowerName.setText(hold);

                lowersTurn = !lowersTurn;
            } else {
                board.setFlipped(false);
                if (amPlaying) {
                    doRun();
                }
            }
            repaint();

            break;

        case "GAME_OVER":
            board.endGame();
            shutdown();
            board.clearPending();
            board.repaint();
            break;

        case "MADE_MOVE":
            String smith = ((String) value);
            // You are always the lower player
            if (lowersTurn) {
                boolean actLikeWhite = hub.gameState.imWhite;
                int timeonclock = Integer
                        .parseInt(actLikeWhite ? hub.gameState.whiteTime
                                : hub.gameState.blackTime);

                int otherclock = Integer
                        .parseInt(actLikeWhite ? hub.gameState.blackTime
                                : hub.gameState.whiteTime);

                // You made the move
                lowerClock.stopClock();
                lowerClock.setClock(timeonclock);

                upperClock.startClock();
                lowersTurn = false;

                board.clearPending();
                System.out.println("doing move...");
                board.doSmithMove(smith);
                board.repaint();

                if (amPlaying) {
                    m_player.applyMove(smith);
                }
            } else {
                boolean actLikeWhite = hub.gameState.imWhite;
                int timeonclock = Integer
                        .parseInt(actLikeWhite ? hub.gameState.blackTime
                                : hub.gameState.whiteTime);

                int otherclock = Integer
                        .parseInt(actLikeWhite ? hub.gameState.whiteTime
                                : hub.gameState.blackTime);

                upperClock.stopClock();
                upperClock.setClock(timeonclock);

                lowerClock.startClock();
                lowersTurn = true;
                
                if (smith.equals("(none)")) {
                    return;
                }

                board.clearPending();
                try {
                    board.doSmithMove(smith);
                    m_player.applyMove(smith);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                board.repaint();

                if (amPlaying) {
                    doRun();
                }
            }

            break;

        case "ILLEGAL":
            board.clearPending();
            board.repaint();
            hub.addMessage("Illegal Move: " + (String) value);
            break;

        case "ADD_MATCH":
            addMatch((String) value);
            break;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    public void shutdown() {
        lowerClock.stopClock();
        upperClock.stopClock();
        lowerClock.setClock(3000 * 60);
        upperClock.setClock(3000 * 60);
        lowerName.setText(" \n ");
        upperName.setText(" \n ");

        if (!Config.boolGuest && amPlaying) {
            m_player = new Engine(hub, 180000, 2000); // added by Pravir
            m_player.addBestMoveObserver(this);
        }

        amPlaying = false;
        lowersTurn = true;

        if (button1 != null)
            this.remove(button1);
        if (button2 != null)
            this.remove(button2);

        board.newGame();
        pendingMatches.clear();
    }

    private void doRun() {
        Thread t = new Thread(new MakeMoveRunner());
        t.setDaemon(true);
        t.start();
    }

    public void update(Observable o, Object arg) {
        // Presently, the only updates that are expected are notifications
        // of the new best move found during a search, but others
        // may be added later
        if (arg instanceof Move) {
            board.setBestMove(((Move<?>) arg).serverString());
            board.repaint();
        }
    }

    // thread to call compute move.
    private class MakeMoveRunner implements Runnable {
        public void run() {
            String ur_move = m_player.computeMove((int) lowerClock.msecleft,
                    (int) upperClock.msecleft);
            if (ur_move != null && amPlaying) {
                hub.sendCmdArgs("MOVE", ur_move);
            }
        }

    }
}
