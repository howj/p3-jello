package experiments;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import chess.bots.BestMove;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;
import experiments.AlphaBetaExperiments.countAndMove;

public class MinimaxExperiments<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	
	public static long count = 0;

	public M getBestMove(B board, int myTime, int opTime) {
		/* Calculate the best move */
		// reportNewBestMove(best.move);
		return null;
	}
	
	public long getBestMoveNodes(B board) {
		count = 0;
		BestMove<M> best = minimaxCount(this.evaluator, board, ply);
		return count;
	}
	
	public static class countAndMove<M> {
		long count;
		BestMove<M> move;
	}
	
	static <M extends Move<M>, B extends Board<M, B>> countAndMove minimax(Evaluator<B> evaluator, B board, int depth) {
		count = 0;
		countAndMove<M> ans = new countAndMove<M>();
		ans.move = minimaxCount(evaluator, board, depth);
		ans.count = count;
		return ans;
	}

	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimaxCount(Evaluator<B> evaluator, B board, int depth) {

		count++;
		// If the depth is 0, just return what we have currently
		if (depth == 0) {
			return new BestMove<M>(null, evaluator.eval(board));
		}

		List<M> moves = board.generateMoves();

		BestMove<M> best = new BestMove<M>(null, -evaluator.infty());

		// Check for empty moves situation
		if (moves.isEmpty()) {
			if (board.inCheck()) {
				return new BestMove<M>(null, -evaluator.mate() - depth);
			} else {
				return new BestMove<M>(null, -evaluator.stalemate());
			}
		}

		// Try all possible moves
		for (M move : moves) {
			board.applyMove(move);
			BestMove<M> currMove = minimaxCount(evaluator, board, depth - 1).negate();

			board.undoMove();
			if (currMove.value > best.value) {
				best.move = move;
				best.value = currMove.value;
			}
		}

		return best;
	}
	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> minimaxCount(Evaluator<B> evaluator, B board, int depth, AtomicLong count) {

		count.getAndIncrement();
		// If the depth is 0, just return what we have currently
		if (depth == 0) {
			return new BestMove<M>(null, evaluator.eval(board));
		}

		List<M> moves = board.generateMoves();

		BestMove<M> best = new BestMove<M>(null, -evaluator.infty());

		// Check for empty moves situation
		if (moves.isEmpty()) {
			if (board.inCheck()) {
				return new BestMove<M>(null, -evaluator.mate() - depth);
			} else {
				return new BestMove<M>(null, -evaluator.stalemate());
			}
		}

		// Try all possible moves
		for (M move : moves) {
			board.applyMove(move);
			BestMove<M> currMove = minimaxCount(evaluator, board, depth - 1, count).negate();

			board.undoMove();
			if (currMove.value > best.value) {
				best.move = move;
				best.value = currMove.value;
			}
		}

		return best;
	}
}
