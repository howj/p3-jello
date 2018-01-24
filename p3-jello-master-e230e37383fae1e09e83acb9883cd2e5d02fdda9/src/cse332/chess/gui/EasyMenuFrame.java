package cse332.chess.gui;

import java.awt.CheckboxMenuItem;
import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import javax.swing.JFrame;

import cse332.chess.server.Hub;

public class EasyMenuFrame extends JFrame {
    private static final long serialVersionUID = 93543371734115264L;
    private Hub hub;
    private MenuBar mb;
    private MenuItem fileExit;
    private MenuItem gameDraw, gameAbort, gameResign;
    private CheckboxMenuItem promoteQueen, promoteRook, promoteKnight,
            promoteBishop;
    private MenuItem optionsRefresh;

    public EasyMenuFrame(String title, Hub hub) {
        super(title);
        this.hub = hub;
        mb = new MenuBar();
        initFile();
        initGame();
        initOptions();
        this.setMenuBar(mb);
    }

    private void initFile() {
        Menu m = new Menu("File");
        fileExit = new MenuItem("Exit");
        m.add(fileExit);
        mb.add(m);
    }

    private void initGame() {
        Menu m = new Menu("Game");
        gameDraw = new MenuItem("Suggest a draw");
        m.add(gameDraw);
        gameAbort = new MenuItem("Suggest an abort");
        m.add(gameAbort);
        gameResign = new MenuItem("Resign");
        m.add(gameResign);

        m.addSeparator();
        promoteQueen = new CheckboxMenuItem("Promote to queen");
        promoteQueen.setState(true);
        m.add(promoteQueen);
        promoteRook = new CheckboxMenuItem("Promote to rook");
        m.add(promoteRook);
        promoteKnight = new CheckboxMenuItem("Promote to knight");
        m.add(promoteKnight);
        promoteBishop = new CheckboxMenuItem("Promote to bishop");
        m.add(promoteBishop);

        m.addSeparator();
        mb.add(m);
    }

    private void initOptions() {
        Menu m = new Menu("Options");
        optionsRefresh = new MenuItem("Repaint window");
        m.add(optionsRefresh);

        mb.add(m);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target == promoteQueen) {
            hub.setPromote('q');
            promoteQueen.setState(true);
            promoteRook.setState(false);
            promoteBishop.setState(false);
            promoteKnight.setState(false);
        } 
        else if (evt.target == promoteRook) {
            hub.setPromote('r');
            promoteQueen.setState(false);
            promoteRook.setState(true);
            promoteBishop.setState(false);
            promoteKnight.setState(false);
        } 
        else if (evt.target == promoteBishop) {
            hub.setPromote('b');
            promoteQueen.setState(false);
            promoteRook.setState(false);
            promoteBishop.setState(true);
            promoteKnight.setState(false);
        } 
        else if (evt.target == promoteKnight) {
            hub.setPromote('n');
            promoteQueen.setState(false);
            promoteRook.setState(false);
            promoteBishop.setState(false);
            promoteKnight.setState(true);
        } 
        else if (evt.target == gameDraw) {
            hub.sendCommand("draw");
        } 
        else if (evt.target == gameAbort) {
            hub.sendCommand("abort");
        } 
        else if (evt.target == gameResign) {
            hub.sendCommand("resign");
        } 
        else if (evt.target == optionsRefresh) {
            hub.repaint();
        } 
        else if (evt.target == fileExit) {
            hub.sendCommand("exit");
        } 
        else {
            return false;
        }
        return true;
    }
}