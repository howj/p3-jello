package chess.bots;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
public class ParallelSearcher<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> {
	
	private final int cutoff = super.cutoff;
	private static final int divideCutoff = 1;
	private static final ForkJoinPool POOL = new ForkJoinPool(); 
	
    public M getBestMove(B board, int myTime, int opTime) {
    	List<M> moves = board.generateMoves();
    	return POOL.invoke(new SearchTask<M, B>(moves, 0, moves.size(), this.evaluator, board, ply, cutoff)).move;
    }
    
    @SuppressWarnings("serial")
	static class SearchTask<M extends Move<M>, B extends Board<M, B>> extends RecursiveTask<BestMove<M>> {
    	List<M> moves;
    	int lo, hi, depth, cutoff;
    	B board;
    	Evaluator<B> evaluator;
    	    	
    	public SearchTask(List<M> moves, int lo, int hi, Evaluator<B> evaluator, B board, int depth, int cutoff) {
    		this.moves = moves;
    		this.depth = depth;
    		this.lo = lo;
    		this.hi = hi;
    		this.board = board;
    		this.evaluator = evaluator;
    		this.cutoff = cutoff;
    	}
    	
		public BestMove<M> compute() {
	    	if (this.hi == this.lo) {
	    		this.board = board.copy();
	    		this.board.applyMove(this.moves.get(this.hi));
	    		if (this.depth <= this.cutoff) {
		    		return SimpleSearcher.minimax(this.evaluator, this.board, this.depth);
		    	}
	    		this.moves = board.generateMoves();
	    		if (this.moves.isEmpty()) {
	    			return SimpleSearcher.minimax(this.evaluator, this.board, this.depth);
	    		}
	    		this.hi = this.moves.size();
	    		this.lo = 0;
	    	}
	    	if (this.hi - this.lo <= divideCutoff) {
//	    		ArrayList<SearchTask<M, B>> taskList = new ArrayList<SearchTask<M, B>>();
//	    		for (int i = lo; i < hi - 1; i++) {
//	    			SearchTask<M, B> task = new SearchTask<M, B>(moves, i, i, evaluator, board, depth - 1, cutoff);
//	    			task.fork();
//	    			taskList.add(task);
//	    		}
//	    		BestMove<M> best = new BestMove(moves.get(hi - 1), new SearchTask<M, B>(moves, hi - 1, hi - 1, 
//	    				evaluator, board, depth - 1, cutoff).compute().negate().value);
//	    		for (int i = 0; i < (hi - lo) - 1; i++) {
//	    			BestMove<M> result = ((BestMove<M>) taskList.get(i).join()).negate();
//	    			if (result.value > best.value) {
//	    				result.move = moves.get(i + lo);
//	    				best = result;
//	    			}
//	    		}
	    		@SuppressWarnings("unchecked")
				SearchTask<M, B>[] tasks = new SearchTask[hi - lo - 1];
	    		for (int i = 0; i < (hi - lo) - 1; i++) {
	    			SearchTask<M, B> task = new SearchTask<M, B>(moves, i + lo, i + lo, evaluator, board, depth - 1, cutoff);
	    			task.fork(); 
	    			tasks[i] = task;
	    		}
	    		BestMove<M> best = new BestMove<M>(moves.get(hi - 1), new SearchTask<M, B>(moves, hi - 1, hi - 1, 
	    				evaluator, board, depth - 1, cutoff).compute().negate().value);
	    		for (int i = 0; i < (hi - lo) - 1; i++) {
	    			BestMove<M> result = tasks[i].join().negate();
	    			if (result.value > best.value) {
	    				result.move = moves.get(i + lo);
	    				best = result;
	    			}
	    		}
	    		return best;
	    	} else {
	    		int mid = lo + (hi - lo) / 2;
	    		SearchTask<M, B> left = new SearchTask<M, B>(moves, lo, mid, evaluator, board, depth, cutoff);
	    		SearchTask<M, B> right = new SearchTask<M, B>(moves, mid, hi, evaluator, board, depth, cutoff);
	    		
	    		right.fork();
	    		BestMove<M> leftResult = left.compute();
	    		BestMove<M> rightResult = right.join();
	    		
	    		if (leftResult.value > rightResult.value) {
	    			return leftResult;
	    		} else {
	    			return rightResult;
	    		}
	    	}
    	}
    }
}
