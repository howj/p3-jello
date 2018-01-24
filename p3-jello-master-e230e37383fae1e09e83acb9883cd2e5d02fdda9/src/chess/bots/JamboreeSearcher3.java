package chess.bots;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class JamboreeSearcher3<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> { 
	
	private static final double PERCENTAGE_SEQUENTIAL = .395;
	private static final int divideCutoff = 5;
	private static final ForkJoinPool POOL = new ForkJoinPool(); 

	public M getBestMove(B board, int myTime, int opTime) {
    	List<M> moves = board.generateMoves();
    	return POOL.invoke(new SearchTask<M, B>(moves, 0, moves.size(), evaluator, board, ply, -evaluator.infty(), evaluator.infty(), cutoff, true)).move;
    }
	
	public M getBestMove(B board, int myTime, int opTime, int cores) {
		final ForkJoinPool POOL2 = new ForkJoinPool(cores);
    	List<M> moves = board.generateMoves();
    	return POOL2.invoke(new SearchTask<M, B>(moves, 0, moves.size(), evaluator, board, ply, -evaluator.infty(), evaluator.infty(), cutoff, true)).move;
    }
    
	@SuppressWarnings("serial")
	static class SearchTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>> {
    	List<M> moves;
    	int lo, hi, depth, cutoff;
    	B board;
    	Evaluator<B> evaluator;
    	int alpha, beta;
    	boolean sequential;
    	M bestMove;
    	    	
    	public SearchTask(List<M> moves, int lo, int hi, Evaluator<B> evaluator, B board, int depth, int alpha, int beta, int cutoff, boolean sequential) {
    		this.moves = moves;
    		this.depth = depth;
    		this.lo = lo;
    		this.hi = hi;
    		this.board = board;
    		this.evaluator = evaluator;
    		this.alpha = alpha;
    		this.beta = beta;
    		this.cutoff = cutoff;
    		this.sequential = sequential;
    		this.bestMove = null;
    	}
    	
		@SuppressWarnings("unchecked")
		public BestMove<M> compute() {
			if (!this.moves.isEmpty() && this.hi == this.lo) {
	    		this.board = this.board.copy();
	    		M move = this.moves.get(this.hi);
	    		this.board.applyMove(move);
	    		this.depth--; 
	    		this.moves = this.board.generateMoves();
	    		if (this.moves.isEmpty()) {
					return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
	    		}
	    		this.lo = 0;
	    		this.hi = this.moves.size();
	    		this.sequential = true;
	    	} 
    		if (this.depth <= this.cutoff || this.moves.isEmpty()) {
    			return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
	    	}
			if (this.sequential) {
				int limit = (int)(PERCENTAGE_SEQUENTIAL * this.hi);
				for (int i = 0; i < limit; i++) {
					M move = this.moves.get(i);
					this.board.applyMove(move);
					List<M> newMoves = this.board.generateMoves();
					if (this.moves.isEmpty()) {
						return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth - 1, this.alpha, this.beta);
		    		}
					BestMove<M> result = new SearchTask<M, B>(newMoves, 0, newMoves.size(), this.evaluator, this.board, this.depth - 1, -this.beta, -this.alpha, this.cutoff, true).compute().negate();
					this.board.undoMove();
		        	if (result.value > this.alpha) {
		        		this.alpha = result.value;
		        		this.bestMove = move;
		        	}
		        	if (this.alpha >= this.beta) {
		        		return new BestMove<M>(this.bestMove, this.alpha);
		        	}
		    	}
				this.lo = limit;
			} 
			if (this.hi - this.lo <= divideCutoff) {
				SearchTask<M, B>[] tasks = (SearchTask<M, B>[])new SearchTask[this.hi - this.lo - 1];
				for (int i = 0; i < (this.hi - this.lo) - 1; i++) {
					SearchTask<M, B> task = new SearchTask<M, B>(this.moves, i + this.lo, i + this.lo, this.evaluator, this.board, this.depth, -this.beta, -this.alpha, this.cutoff, true);
					task.fork();
					tasks[i] = task;
				}
				BestMove<M> firstResult = new SearchTask<M, B>(this.moves, this.hi - 1, this.hi - 1, this.evaluator, this.board, this.depth, -this.beta, -this.alpha, this.cutoff, true).compute().negate();
				if (firstResult.value > this.alpha) {
					this.alpha = firstResult.value;
					this.bestMove = this.moves.get(this.hi - 1);
				}
				if (this.alpha >= this.beta) {
					return new BestMove<M>(this.bestMove, this.alpha);
				}
				for (int i = 0; i < (this.hi - this.lo) - 1; i++) {
					BestMove<M> result = (tasks[i].join()).negate();
					if (result.value > this.alpha) {
						this.alpha = result.value;
						this.bestMove = this.moves.get(i + this.lo);
					}
					if (this.alpha >= this.beta) {
						return new BestMove<M>(this.bestMove, this.alpha);
					}
				}
				return new BestMove<M>(this.bestMove, this.alpha);
			} else { 
				int mid = this.lo + (this.hi - this.lo) / 2;
				SearchTask<M, B> left = new SearchTask<M, B>(this.moves, this.lo, mid, this.evaluator, this.board, this.depth, this.alpha, this.beta, this.cutoff, false);
				SearchTask<M, B> right = new SearchTask<M, B>(this.moves, mid, this.hi, this.evaluator, this.board, this.depth, this.alpha, this.beta, this.cutoff, false);
				
				right.fork();
				BestMove<M> leftResult = left.compute();
				BestMove<M> rightResult = right.join();
				
				if (leftResult.value > this.alpha) {
					this.bestMove = leftResult.move;
					this.alpha = leftResult.value;
				}
				if (rightResult.value > this.alpha) {
					this.bestMove = rightResult.move;
					this.alpha = rightResult.value;
				}
				return new BestMove<M>(this.bestMove, this.alpha);
			}	
    	}
    } 
}