package chess.bots;

import java.util.List;


import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

/**
 * This class should implement the minimax algorithm as described in the
 * assignment handouts.
 */
public class SimpleSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {

    public M getBestMove(B board, int myTime, int opTime) {
        /* Calculate the best move */
        BestMove<M> best = minimax(this.evaluator, board, ply);
        // reportNewBestMove(best.move);
        return best.move;
    }

    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimax(Evaluator<B> evaluator, B board, int depth) {
    	
    	// If the depth is 0, just return what we have currently
    	if (depth == 0) {
    		return new BestMove(null, evaluator.eval(board));
    	}
    	
    	List<M> moves = board.generateMoves();
    	
    	BestMove<M> best = new BestMove(null, -evaluator.infty());
    	
    	// Check for empty moves situation
    	if (moves.isEmpty()) {
    		if (board.inCheck()) {
    			return new BestMove(null, -evaluator.mate() - depth);
    		} else {
    			return new BestMove(null, -evaluator.stalemate());
    		}
    	}
    	
    	// Try all possible moves 
    	for (M move : moves) {
    		board.applyMove(move);    		
    		BestMove<M> currMove = minimax(evaluator, board, depth - 1).negate(); 
    		
        	board.undoMove();
        	if (currMove.value > best.value) {
        		best.move = move;
        		best.value = currMove.value;
        	}
    	}
    	
    	return best;
    }
}