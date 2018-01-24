package chess.bots;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import chess.board.*;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class JamboreeSearcher2<M extends Move<M>, B extends Board<M, B>> extends
        AbstractSearcher<M, B> { 
	
	private static final double PERCENTAGE_SEQUENTIAL = .5;
	private static final int divideCutoff = 5;
	private static final ForkJoinPool POOL = new ForkJoinPool();
	private ConcurrentHashMap<Long, TableEntry<M>> table = new ConcurrentHashMap<Long, TableEntry<M>>();
	
	private static class TableEntry<M> {
		public BestMove<M> bestMove;
		public int depth;
		
		public TableEntry(BestMove<M> bestMove, int depth) {
			this.bestMove = bestMove;
			this.depth = depth;
		}
	}

	@SuppressWarnings("unchecked")
	public M getBestMove(B board, int myTime, int opTime) {
		if (myTime > 15000) {
			ply = 6;
		} else if (myTime > 10000) {
			ply = 5;
		} else {
			ply = 4;
		}
    	List<M> moves = board.generateMoves();
    	MVVLVASort((List<ArrayMove>)moves);
    	for (int i = 1; i < ply; i++) {
    		M depthMove = POOL.invoke(new SearchTask<M, B>(moves, 0, moves.size(), evaluator, board, i, -evaluator.infty(), evaluator.infty(), cutoff, true, table)).move; 
    		moves.remove(depthMove);
    		moves.add(0, depthMove);
    	}
    	return POOL.invoke(new SearchTask<M, B>(moves, 0, moves.size(), evaluator, board, ply, -evaluator.infty(), evaluator.infty(), cutoff, true, table)).move;
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
    	ConcurrentHashMap<Long, TableEntry<M>> table;
    	    	
    	public SearchTask(List<M> moves, int lo, int hi, Evaluator<B> evaluator, B board, int depth, int alpha, int beta, int cutoff, boolean sequential, ConcurrentHashMap<Long, TableEntry<M>> table) {
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
    		this.table = table;
    	}
    	
		@SuppressWarnings("unchecked")
		public BestMove<M> compute() {
			if (!this.moves.isEmpty() && this.hi == this.lo) {
	    		this.board = this.board.copy();
	    		M move = this.moves.get(this.hi);
	    		this.board.applyMove(move);
	    		this.depth--;
//	    		TableEntry<M> tableMove = this.table.get(this.board.signature());
//	    		if (tableMove != null && tableMove.depth == this.depth) {
//	    			return tableMove.bestMove;
//	    		}
	    		this.moves = this.board.generateMoves();
	    		if (this.moves.isEmpty()) {
//	    			BestMove<M> ans = AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
//	    			table.put(this.board.signature(), new TableEntry<M>(ans, this.depth));
//	    			return ans;
					return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
	    		}
	    		MVVLVASort((List<ArrayMove>)this.moves);
	    		this.lo = 0;
	    		this.hi = this.moves.size();
	    		this.sequential = true;
	    	} 
    		if (this.depth <= this.cutoff || this.moves.isEmpty()) {
//    			BestMove<M> ans = AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
//    			table.put(this.board.signature(), new TableEntry<M>(ans, this.depth));
//    			return ans;
    			return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth, this.alpha, this.beta);
	    	}
			if (this.sequential) {
				int limit = (int)(PERCENTAGE_SEQUENTIAL * this.hi);
				for (int i = 0; i < limit; i++) {
					M move = this.moves.get(i);
					this.board.applyMove(move);
//					TableEntry<M> tableMove = this.table.get(this.board.signature());
//		    		if (tableMove != null && tableMove.depth == this.depth - 1) {
//		    			return tableMove.bestMove;
//		    		}
					List<M> newMoves = this.board.generateMoves();
					if (this.moves.isEmpty()) {
//		    			BestMove<M> ans = AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth - 1, this.alpha, this.beta);
//		    			table.put(this.board.signature(), new TableEntry<M>(ans, this.depth - 1));
//		    			return ans;
						return AlphaBetaSearcher.alphabeta(this.evaluator, this.board, this.depth - 1, this.alpha, this.beta);
		    		}
					MVVLVASort((List<ArrayMove>)newMoves);
					BestMove<M> result = new SearchTask<M, B>(newMoves, 0, newMoves.size(), this.evaluator, this.board, this.depth - 1, -this.beta, -this.alpha, this.cutoff, true, this.table).compute().negate();
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
					SearchTask<M, B> task = new SearchTask<M, B>(this.moves, i + this.lo, i + this.lo, this.evaluator, this.board, this.depth, -this.beta, -this.alpha, this.cutoff, true, this.table);
					task.fork();
					tasks[i] = task;
				}
				BestMove<M> firstResult = new SearchTask<M, B>(this.moves, this.hi - 1, this.hi - 1, this.evaluator, this.board, this.depth, -this.beta, -this.alpha, this.cutoff, true, this.table).compute().negate();
				if (firstResult.value > this.alpha) {
					this.alpha = firstResult.value;
					this.bestMove = this.moves.get(this.hi - 1);
				}
				if (this.alpha >= this.beta) {
					return new BestMove<M>(this.bestMove, this.alpha);
				}
				for (int i = 0; i < (this.hi - this.lo) - 1; i++) {
					BestMove<M> result = ((BestMove<M>)tasks[i].join()).negate();
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
				SearchTask<M, B> left = new SearchTask<M, B>(this.moves, this.lo, mid, this.evaluator, this.board, this.depth, this.alpha, this.beta, this.cutoff, false, this.table);
				SearchTask<M, B> right = new SearchTask<M, B>(this.moves, mid, this.hi, this.evaluator, this.board, this.depth, this.alpha, this.beta, this.cutoff, false, this.table);
				
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
	
	public static void MVVLVASort(List<ArrayMove> list) {
		list.sort((x, y) -> x.isCapture() == y.isCapture() ? (x.dest.piece == y.dest.piece ? (x.source.piece == ArrayPiece.KING ? 100 : x.source.piece) - (y.source.piece == ArrayPiece.KING ? 100 : y.source.piece) : y.dest.piece - x.dest.piece) : (x.isCapture() ? -1 : 1));
	}
}