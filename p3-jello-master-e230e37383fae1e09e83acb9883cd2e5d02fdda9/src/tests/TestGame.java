package tests;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.AlphaBetaSearcher;
import chess.bots.LazySearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

public class TestGame {
    public Searcher<ArrayMove, ArrayBoard> whitePlayer;
    public Searcher<ArrayMove, ArrayBoard> blackPlayer;
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String[] positions = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", 
    										  "r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -",
    										  "2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -"};
    private ArrayBoard board;
    
    public static void main(String[] args) {
        TestGame game = new TestGame();
        game.play();
    }

    public TestGame() {
        setupWhitePlayer(new AlphaBetaSearcher<ArrayMove, ArrayBoard>(), 3, 3);
        setupBlackPlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 4, 4);
    }
    
    public void play() {
       this.board = ArrayBoard.FACTORY.create().init(STARTING_POSITION);
       Searcher<ArrayMove, ArrayBoard> currentPlayer = this.blackPlayer;
       
       int turn = 0;
       
       /* Note that this code does NOT check for stalemate... */
       while (!board.inCheck() || board.generateMoves().size() > 0) {
           currentPlayer = currentPlayer.equals(this.whitePlayer) ? this.blackPlayer : this.whitePlayer;
           System.out.printf("%3d: " + board.fen() + "\n", turn);
           this.board.applyMove(currentPlayer.getBestMove(board, 1000, 1000));
           turn++;
       }
    }
    
    public Searcher<ArrayMove, ArrayBoard> setupPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());
        return searcher; 
    }
    public void setupWhitePlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.whitePlayer = setupPlayer(searcher, depth, cutoff);
    }
    public void setupBlackPlayer(Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        this.blackPlayer = setupPlayer(searcher, depth, cutoff);
    }
}
