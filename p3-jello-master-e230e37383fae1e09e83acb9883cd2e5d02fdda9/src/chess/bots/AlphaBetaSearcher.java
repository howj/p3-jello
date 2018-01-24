package chess.bots;

import java.util.List;

import cse332.chess.interfaces.AbstractSearcher;
import cse332.chess.interfaces.Board;
import cse332.chess.interfaces.Evaluator;
import cse332.chess.interfaces.Move;

public class AlphaBetaSearcher<M extends Move<M>, B extends Board<M, B>> extends AbstractSearcher<M, B> {
	// public M getBestMove(B board, int myTime, int opTime) {
	// throw new NotYetImplementedException();
	// }
	public M getBestMove(B board, int myTime, int opTime) {
		/* Calculate the best move */
		// BestMove<M> alpha = new BestMove<M>(null, -evaluator.infty()); //
		// lower bound
		// BestMove<M> beta = new BestMove<M>(null, evaluator.infty()); // upper
		// bound

		return alphabeta(this.evaluator, board, ply, -evaluator.infty(), evaluator.infty()).move;

	}

	static <M extends Move<M>, B extends Board<M, B>> BestMove<M> alphabeta(Evaluator<B> evaluator, B board, int depth,
			int alpha, int beta) {

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