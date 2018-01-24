package experiments;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import chess.bots.BestMove;
import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class AlphaBetaExperiments<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	// public M getBestMove(B board, int myTime, int opTime) {
	// throw new NotYetImplementedException();
	// }
	public static long count = 0;

	public M getBestMove(B board, int myTime, int opTime) {
		/* Calculate the best move */
		// BestMove<M> alpha = new BestMove<M>(null, -evaluator.infty()); //
		// lower bound
		// BestMove<M> beta = new BestMove<M>(null, evaluator.infty()); // upper
		// bound

		return null;
	}
	
	public static class countAndMove<M> {
		long count;
		BestMove<M> move;
	}

	public long getBestMoveNodes(B board) {
		count = 0;
		BestMove<M> best = alphabetaCount(this.evaluator, board, ply, -evaluator.infty(), evaluator.infty());
		return count;
	}
	
	static <M extends Move<M>, B extends Board<M, B>> countAndMove alphabeta(Evaluator<B> evaluator, B board,
			int depth, int alpha, int beta) {
		count = 0;
		countAndMove<M> ans = new countAndMove<M>();
		ans.move = alphabetaCount(evaluator, board, depth, -evaluator.infty(), evaluator.infty());
		ans.count = count;
		return ans;
	}

	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> alphabetaCount(Evaluator<B> evaluator, B board,
			int depth, int alpha, int beta) {
		count++;
		// If the depth is 0, just return what we have currently
		if (depth == 0) {
			return new BestMove(null, evaluator.eval(board));
		}

		List<M> moves = board.generateMoves();

		// Check for empty moves situation
		if (moves.isEmpty()) {
			if (board.inCheck()) {
				return new BestMove(null, -evaluator.mate() - depth);
			} else {
				return new BestMove(null, -evaluator.stalemate());
			}
		}
		M bestMove = null;

		// Try all possible moves
		for (M move : moves) {
			board.applyMove(move);

			BestMove<M> currMove = alphabetaCount(evaluator, board, depth - 1, -beta, -alpha).negate();

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
	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> alphabetaCount(Evaluator<B> evaluator, B board,
			int depth, int alpha, int beta, AtomicLong count) {
		count.getAndIncrement();
		// If the depth is 0, just return what we have currently
		if (depth == 0) {
			return new BestMove(null, evaluator.eval(board));
		}

		List<M> moves = board.generateMoves();

		// Check for empty moves situation
		if (moves.isEmpty()) {
			if (board.inCheck()) {
				return new BestMove(null, -evaluator.mate() - depth);
			} else {
				return new BestMove(null, -evaluator.stalemate());
			}
		}
		M bestMove = null;

		// Try all possible moves
		for (M move : moves) {
			board.applyMove(move);

			BestMove<M> currMove = alphabetaCount(evaluator, board, depth - 1, -beta, -alpha, count).negate();

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