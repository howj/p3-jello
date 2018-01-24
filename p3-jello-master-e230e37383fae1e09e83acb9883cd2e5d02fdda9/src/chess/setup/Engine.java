package chess.setup;

import java.util.Observer;

import cse332.chess.interfaces.Searcher;
import cse332.chess.server.Hub;
import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.AlphaBetaSearcher;
import chess.bots.JamboreeSearcher;
import chess.bots.JamboreeSearcher2;
import chess.bots.LazySearcher;
import chess.game.SimpleEvaluator;
import chess.game.SimpleTimer;

/**
 *  When you play on the chess server, this class is used to set up
 *  your bot.  Make sure you specify the searcher, evaluator,  
 */
public class Engine {
	/*
	 * You will want to change these to whatever classes you want your bot
	 * to use when it plays a game of chess.  
	 */
	
	private Searcher <ArrayMove, ArrayBoard>
	    searcher = new JamboreeSearcher2<ArrayMove,ArrayBoard>();
	
	public Engine(int time, int inc) {
        searcher.setDepth(6);
        searcher.setCutoff(4);//

        searcher.setEvaluator(new SimpleEvaluator());
        searcher.setTimer(new SimpleTimer(time, inc));
    }
	
	public String getName() {
		return "Name of Bot";
	}

	
	
	
	

	/****************************************************/
    /****************************************************/
    /****************************************************/
    /**** You do not need to look below this comment ****/
    /****************************************************/
    /****************************************************/
    /****************************************************/

	private ArrayBoard board = ArrayBoard.FACTORY.create().init(ArrayBoard.STARTING_POSITION);

	private int plyCount = 0;

	/**
	 * Converts the string representation of a move into a move
	 * and then applies it to the current board.
	 * 
	 * @param m the move string.
	 */
	public void applyMove(String m)
	{
	  if( board.plyCount() != plyCount++ )
	  {
	    throw new IllegalStateException(
	      "Did you forget to call undoMove() somewhere?"
	    );
	  }
	  
		board.applyMove(board.createMoveFromString(m));
	}


	/**
	 * Compute and return a move in the current position.
	 * 
	 * The returned move must be in the String format accepted
	 * by the server.
	 * 
	 * @param myTime number of seconds left on the player's clock
	 * @param opTime number of seconds left on the opponent's clock
	 */
	public String computeMove(int myTime, int opTime) {
	  //assert(false) : "Assertions should be disabled when playing competitively.";
	  
        
        ArrayMove move = searcher.getBestMove(getBoard(), myTime, opTime);
        if (move == null) {
            return null;
        }
		return board.moveToSmithString(move);
	}	

	/**
	 * Return the player's board state
	 */
	public ArrayBoard getBoard() {
		return board;
	}

	public Hub theHub;

	public Engine(Hub h, int time, int inc) {
		this(time, inc);
		theHub = h;
	}
	
	// This can be expanded so that the Observer is notified of other
	// events as well.
	/**
	 * Adds an Observer to the Searcher so that when a new best move
	 * is found, the Observer will be notified. 
	 * @param o the new Observer
	 */
	public void addBestMoveObserver(Observer o) {
		searcher.addBestMoveObserver(o);
	}
}
