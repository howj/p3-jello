package experiments;

import java.util.ArrayList;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.AlphaBetaSearcher;
import chess.bots.LazySearcher;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Searcher;

public class NodesVisitedExperiment {
	public static void main(String[] args) {
        TestGame game = new TestGame();
        ArrayList<String> fens = game.play();
        MinimaxExperiments<ArrayMove, ArrayBoard> minimax = new MinimaxExperiments<>();
        minimax.setEvaluator(new SimpleEvaluator());
        AlphaBetaExperiments<ArrayMove, ArrayBoard> alphabeta = new AlphaBetaExperiments<>();
        alphabeta.setEvaluator(new SimpleEvaluator());
        ParallelExperiments<ArrayMove, ArrayBoard> parallel = new ParallelExperiments<>();
        parallel.setEvaluator(new SimpleEvaluator());
        JamboreeExperiments<ArrayMove, ArrayBoard> jamboree = new JamboreeExperiments<>();
        jamboree.setEvaluator(new SimpleEvaluator());
        
        for (int i = 1; i <= 5; i++) {
        	minimax.setDepth(i);
        	long count = 0;
			for (int j = 0; j < fens.size(); j++) {
				count += minimax.getBestMoveNodes(ArrayBoard.FACTORY.create().init(fens.get(i)));
			}
			System.out.println("Minimax\n" +
								"\tdepth: " + i + ", count: " + count);
        }
        for (int i = 1; i <= 5; i++) {
        	alphabeta.setDepth(i);
        	long count = 0;
			for (int j = 0; j < fens.size(); j++) {
				count += alphabeta.getBestMoveNodes(ArrayBoard.FACTORY.create().init(fens.get(i)));
			}
			System.out.println("Alphabeta\n" +
								"\tdepth: " + i + ", count: " + count);
        }
		for (int i = 1; i <= 5; i++) {
        	parallel.setDepth(i);
        	long count = 0; 
			for (int j = 0; j < fens.size(); j++) {
				count += parallel.getBestMoveNodes(ArrayBoard.FACTORY.create().init(fens.get(i)));
			}
			System.out.println("Parallel minimax\n" +
								"\tdepth: " + i + ", count: " + count);
        }
        for (int i = 1; i <= 5; i++) {
        	jamboree.setDepth(i);
        	long count = 0;
			for (int j = 0; j < fens.size(); j++) {
				count += jamboree.getBestMoveNodes(ArrayBoard.FACTORY.create().init(fens.get(i)));
			}
			System.out.println("Jamboree\n" +
								"\tdepth: " + i + ", count: " + count);
        }
        
	}

	public class TestStartingPosition {
	    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	    public ArrayMove getBestMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) { 
	        searcher.setDepth(depth);
	        searcher.setCutoff(cutoff);
	        searcher.setEvaluator(new SimpleEvaluator());

	        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0);
	    }
	    
	    public void printMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
	        String botName = searcher.getClass().toString().split(" ")[1].replace("chess.bots.", "");
	        System.out.println(botName + " returned " + getBestMove(fen, searcher, depth, cutoff));
	    }
	    public void main(String[] args) {
	        Searcher<ArrayMove, ArrayBoard> dumb = new LazySearcher<>();
	        printMove(STARTING_POSITION, dumb, 3, 0);
	    }
	}
	
	public static class TestGame {
	    public Searcher<ArrayMove, ArrayBoard> whitePlayer;
	    public Searcher<ArrayMove, ArrayBoard> blackPlayer;
	    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	    
	    private ArrayBoard board;


	    public TestGame() {
	        setupWhitePlayer(new AlphaBetaSearcher<ArrayMove, ArrayBoard>(), 3, 3);
	        setupBlackPlayer(new SimpleSearcher<ArrayMove, ArrayBoard>(), 4, 4);
	    }
	    
	    public ArrayList<String> play() {
	       this.board = ArrayBoard.FACTORY.create().init(STARTING_POSITION);
	       Searcher<ArrayMove, ArrayBoard> currentPlayer = this.blackPlayer;
	       ArrayList<String> fens = new ArrayList<String>();
	       
	       int turn = 0;
	       
	       /* Note that this code does NOT check for stalemate... */
	       while (!board.inCheck() || board.generateMoves().size() > 0) {
	           currentPlayer = currentPlayer.equals(this.whitePlayer) ? this.blackPlayer : this.whitePlayer;
	           System.out.printf("%3d: " + board.fen() + "\n", turn);
	           fens.add(board.fen());
	           this.board.applyMove(currentPlayer.getBestMove(board, 1000, 1000));
	           turn++;
	       }
	       return fens;
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
}
