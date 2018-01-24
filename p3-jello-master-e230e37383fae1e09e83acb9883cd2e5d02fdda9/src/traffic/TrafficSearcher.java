package traffic;

import java.util.List;

import chess.bots.BestMove;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
import cse332.exceptions.NotYetImplementedException;

public class TrafficSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {

    public M getBestMove(B board, int myTime, int opTime) {
    	return alphabeta(this.evaluator, board, ply, -evaluator.infty(), evaluator.infty()).move;
    }
    
    static <M extends Move<M>, B extends Board<M, B>> BestMove<M> alphabeta(Evaluator<B> evaluator, B board, int depth, int alpha, int beta) {
    	
    	// If the depth is 0, just return what we have currently
    	if (depth == 0) {
    		return new BestMove(null, evaluator.eval(board));
    	}
    	
    	List<M> moves = board.generateMoves();
    	
    	// Check for empty moves situation
    	if (moves.isEmpty()) {
    		//return new BestMove(null, -evaluator.infty());
    		return new BestMove(null, evaluator.eval(board));
    	}
    	M bestMove = null;
    	
    	// Try all possible moves 
    	for (M move : moves) {
    		board.applyMove(move);   
    		
    		BestMove<M> currMove = alphabeta(evaluator, board, depth - 1, -beta, -alpha).negate(); 
    		
        	board.undoMove();
        	if (currMove.value > alpha) {
        		alpha = currMove.value;
        		bestMove = move;
        	}
        	
        	if (alpha >= beta) {
        		return new BestMove(bestMove, alpha);
        	}
    	}
    	
    	return new BestMove(bestMove, alpha);
    }    
}