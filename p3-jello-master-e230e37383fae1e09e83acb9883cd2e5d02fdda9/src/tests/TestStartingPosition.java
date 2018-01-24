package tests;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.bots.AlphaBetaSearcher;
import chess.bots.JamboreeSearcher;
import chess.bots.JamboreeSearcher2;
import chess.bots.JamboreeSearcher3;
import chess.bots.LazySearcher;
import chess.bots.ParallelSearcher;
import chess.bots.ParallelSearcher2;
import chess.bots.SimpleSearcher;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

public class TestStartingPosition {
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String[] positions = {"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", 
    										  "r3k2r/pp5p/2n1p1p1/2pp1p2/5B2/2qP1Q2/P1P2PPP/R4RK1 w Hkq -",
			  								  "2k3r1/p6p/2n5/3pp3/1pp5/2qPP3/P1P1K2P/R1R5 w Hh -"};

    public static ArrayMove getBestMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0);
    }
    
    public static ArrayMove getBestMove(String fen, ParallelSearcher2<ArrayMove, ArrayBoard> searcher, int depth, int cutoff, int cores) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0, cores);
    }
    
    public static ArrayMove getBestMove(String fen, JamboreeSearcher3<ArrayMove, ArrayBoard> searcher, int depth, int cutoff, int cores) { 
        searcher.setDepth(depth);
        searcher.setCutoff(cutoff);
        searcher.setEvaluator(new SimpleEvaluator());

        return searcher.getBestMove(ArrayBoard.FACTORY.create().init(fen), 0, 0, cores);
    }
    
    public static void printMove(String fen, Searcher<ArrayMove, ArrayBoard> searcher, int depth, int cutoff) {
        String botName = searcher.getClass().toString().split(" ")[1].replace("chess.bots.", "");
        System.out.println(botName + " returned " + getBestMove(fen, searcher, depth, cutoff));
    }
    public static void main(String[] args) {
        ParallelSearcher2<ArrayMove, ArrayBoard> parallel = new ParallelSearcher2<>();
        JamboreeSearcher3<ArrayMove, ArrayBoard> jamboree = new JamboreeSearcher3<>();
        SimpleSearcher<ArrayMove, ArrayBoard> minimax = new SimpleSearcher<>();
        AlphaBetaSearcher<ArrayMove, ArrayBoard> alphabeta = new AlphaBetaSearcher<>();

//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j <= 5; j++) {
//				long parallelTime = 0;
//				for (int k = 0; k < 20; k++) {
//					long t1 = System.currentTimeMillis();
//					getBestMove(positions[i], parallel, 5, j);
//					long t2 = System.currentTimeMillis();
//					parallelTime += (t2 - t1);
//				}
//				System.out.println("parallel\n\tposition " + i + ", sequential cutoff " + j + ", total ms " + parallelTime + ", average " + parallelTime/20);
//			}
//		}
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j <= 5; j++) {
//				long jamboreeTime = 0;
//				for (int k = 0; k < 20; k++) {
//					long t1 = System.currentTimeMillis();
//					getBestMove(positions[i], jamboree, 5, j);
//					long t2 = System.currentTimeMillis();
//					jamboreeTime += (t2 - t1);
//				}
//				System.out.println("jamboree\n\tposition " + i + ", sequential cutoff " + j + ", total ms " + jamboreeTime + ", average " + jamboreeTime/20);
//			}
//		}
		for (int i = 0; i < 3; i++) {
			long parallelTime = 0;
			for (int k = 0; k < 20; k++) {
				long t1 = System.currentTimeMillis();
				getBestMove(positions[i], parallel, 5, 2, 16);
				long t2 = System.currentTimeMillis();
				parallelTime += (t2 - t1);
			}
			System.out.println("parallel\n\tposition " + i + ", sequential cutoff 2, cores 16, total ms " + parallelTime + ", average " + parallelTime/20);
			System.err.println("parallel\n\tposition " + i + ", sequential cutoff 2, cores 16, total ms " + parallelTime + ", average " + parallelTime/20);
		}
		for (int i = 0; i < 3; i++) {
			long jamboreeTime = 0;
			for (int k = 0; k < 20; k++) {
				long t1 = System.currentTimeMillis();
				getBestMove(positions[i], jamboree, 5, 2, 16);
				long t2 = System.currentTimeMillis();
				jamboreeTime += (t2 - t1);
			}
			System.out.println("jamboree\n\tposition " + i + ", sequential cutoff 2, cores 16, total ms " + jamboreeTime + ", average " + jamboreeTime/20);
			System.err.println("jamboree\n\tposition " + i + ", sequential cutoff 2, cores 16, total ms " + jamboreeTime + ", average " + jamboreeTime/20);
		}
		for (int i = 0; i < 3; i++) {
			long minimaxTime = 0;
			for (int k = 0; k < 20; k++) {
				long t1 = System.currentTimeMillis();
				getBestMove(positions[i], minimax, 5, 0);
				long t2 = System.currentTimeMillis();
				minimaxTime += (t2 - t1);
			}
			System.out.println("minimax\n\tposition " + i + ", total ms " + minimaxTime + ", average " + minimaxTime/20);
			System.err.println("minimax\n\tposition " + i + ", total ms " + minimaxTime + ", average " + minimaxTime/20);
		}
		for (int i = 0; i < 3; i++) {
			long alphabetaTime = 0;
			for (int k = 0; k < 20; k++) {
				long t1 = System.currentTimeMillis();
				getBestMove(positions[i], alphabeta, 5, 0);
				long t2 = System.currentTimeMillis();
				alphabetaTime += (t2 - t1);
			}
			System.out.println("alphabeta\n\tposition " + i + ", total ms " + alphabetaTime + ", average " + alphabetaTime/20);
			System.err.println("alphabeta\n\tposition " + i + ", total ms " + alphabetaTime + ", average " + alphabetaTime/20);
		}
    }
}
