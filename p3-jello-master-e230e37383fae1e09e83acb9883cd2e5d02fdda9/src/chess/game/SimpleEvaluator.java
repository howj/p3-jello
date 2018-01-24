package chess.game;

import cse332.chess.interfaces.Evaluator;
import chess.board.ArrayBoard;

import chess.board.ArrayPiece;
import static chess.board.ArrayBoard.*;
import static chess.board.ArrayPiece.*;

public class SimpleEvaluator implements Evaluator<ArrayBoard> {

    private static final int INFINITY = 1000000;
    private static final int MATE = 300000;
    private static final int STALEMATE = 0;

    public int infty() {
        return INFINITY;
    }

    public int mate() {
        return MATE;
    }

    public int stalemate() {
        return STALEMATE;
    }

    /*
     * This is the evaluator. It simply returns a score for the board position
     * with respect to the player to move. If you want to use a
     * different evaluation function for a more advanced version of your
     * program, you may do that.
     * 
     * The evaluation function gives a score for each piece according to the
     * pieceValue array below, and an additional amount for each piece depending
     * on where it is (see comment below). A bonus of 10 points should be given
     * if the current player has castled (and -10 for the opponent castling)
     * 
     * The eval of a position is the value of the pieces of the player whose
     * turn it is, minus the value of the pieces of the other player (plus the
     * castling points thrown in).
     * 
     * If it's WHITE's turn, and white is up a queen, then the value will be
     * roughly 900. If it's BLACK's turn and white is up a queen, then the value
     * returned should be about -900.
     */

    public int eval(ArrayBoard board) {
        // Evaluate the board according to white and flip at the end if we
        // are actually evaluating for black.
        int score = 0;

        // calculate the bonus for castling.
        if (board.hasCastled[WHITE])
            score += CASTLE_BONUS;

        if (board.hasCastled[BLACK])
            score -= CASTLE_BONUS;

        // calculate the material and positional worth of each piece
        for (ArrayPiece p : board.allPieces()) {
            score += PIECE_VALUE[p.piece];
            score += POS_VALUE[p.piece][p.square];
        }

        return (board.toPlay() == WHITE ? score : -score);
    }

    /*
     * Piece value tables modify the value of each piece according to where it
     * is on the board.
     * 
     * To orient these tables, each row of 8 represents one row (rank) of the
     * chessboard.
     * 
     * !!! The first row is where white's pieces start !!!
     * 
     * So, for example having a pawn at d2 is worth -5 for white. Having it at
     * d7 is worth 20. Note that these have to be flipped over to evaluate
     * black's pawns since pawn values are not symmetric.
     */
    private static int bishoppos[][] = { { -5, -5, -5, -5, -5, -5, -5, -5 },
            { -5, 10, 5, 8, 8, 5, 10, -5 }, { -5, 5, 3, 8, 8, 3, 5, -5 },
            { -5, 3, 10, 3, 3, 10, 3, -5 }, { -5, 3, 10, 3, 3, 10, 3, -5 },
            { -5, 5, 3, 8, 8, 3, 5, -5 }, { -5, 10, 5, 8, 8, 5, 10, -5 },
            { -5, -5, -5, -5, -5, -5, -5, -5 } };
    private static int knightpos[][] = { { -10, -5, -5, -5, -5, -5, -5, -10 },
            { -8, 0, 0, 3, 3, 0, 0, -8 }, { -8, 0, 10, 8, 8, 10, 0, -8 },
            { -8, 0, 8, 10, 10, 8, 0, -8 }, { -8, 0, 8, 10, 10, 8, 0, -8 },
            { -8, 0, 10, 8, 8, 10, 0, -8 }, { -8, 0, 0, 3, 3, 0, 0, -8 },
            { -10, -5, -5, -5, -5, -5, -5, -10 } };
    private static int pawnpos[][] = { { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, -5, -5, 0, 0, 0 }, { 0, 2, 3, 4, 4, 3, 2, 0 },
            { 0, 4, 6, 10, 10, 6, 4, 0 }, { 0, 6, 9, 10, 10, 9, 6, 0 },
            { 4, 8, 12, 16, 16, 12, 8, 4 }, { 5, 10, 15, 20, 20, 15, 10, 5 },
            { 0, 0, 0, 0, 0, 0, 0, 0 } };

    /* Material value of a piece */
    private static final int kingval = 350;
    private static final int queenval = 900;
    private static final int rookval = 500;
    private static final int bishopval = 300;
    private static final int knightval = 300;
    private static final int pawnval = 100;
    // private static final int emptyval = 0;

    /* The bonus for castling */
    private static final int CASTLE_BONUS = 10;

    private static final int[] PIECE_VALUE = new int[16];
    static {
        PIECE_VALUE[WHITE_PAWN] = pawnval;
        PIECE_VALUE[WHITE_KNIGHT] = knightval;
        PIECE_VALUE[WHITE_BISHOP] = bishopval;
        PIECE_VALUE[WHITE_ROOK] = rookval;
        PIECE_VALUE[WHITE_QUEEN] = queenval;
        PIECE_VALUE[WHITE_KING] = kingval;

        PIECE_VALUE[BLACK_PAWN] = -pawnval;
        PIECE_VALUE[BLACK_KNIGHT] = -knightval;
        PIECE_VALUE[BLACK_BISHOP] = -bishopval;
        PIECE_VALUE[BLACK_ROOK] = -rookval;
        PIECE_VALUE[BLACK_QUEEN] = -queenval;
        PIECE_VALUE[BLACK_KING] = -kingval;
    }

    private static final int[][] POS_VALUE = new int[16][128];
    static {
        POS_VALUE[WHITE_PAWN] = make1D(pawnpos);
        POS_VALUE[WHITE_KNIGHT] = make1D(knightpos);
        POS_VALUE[WHITE_BISHOP] = make1D(bishoppos);

        POS_VALUE[BLACK_PAWN] = flipvert(negate(make1D(pawnpos)));
        POS_VALUE[BLACK_KNIGHT] = flipvert(negate(make1D(knightpos)));
        POS_VALUE[BLACK_BISHOP] = flipvert(negate(make1D(bishoppos)));
    }

    private static int[] make1D(int[][] arr) {
        int[] newarr = new int[128];

        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[0].length; ++j) {
                int newindex = indexOfSquare(i, j);

                newarr[newindex] = arr[i][j];
            }
        }

        return newarr;
    }

    private static int[] flipvert(int[] arr) {
        int[] newarr = new int[arr.length];

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                int newindex = indexOfSquare(i, j);
                int oldindex = indexOfSquare(7 - i, j);

                newarr[newindex] = arr[oldindex];
            }
        }

        return newarr;
    }

    private static int[] negate(int[] arr) {
        int[] newarr = new int[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            newarr[i] = -arr[i];
        }

        return newarr;
    }
}