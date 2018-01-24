package tests.gitlab;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

import chess.bots.SimpleSearcher;

import tests.TestsUtility;
import tests.gitlab.TestingInputs;

public class MinimaxTests extends SearcherTests {

	public static void main(String[] args) { new MinimaxTests().run(); }
    public static void init() { STUDENT = new SimpleSearcher<ArrayMove, ArrayBoard>(); }
	
	@Override
	protected void run() {
        SHOW_TESTS = true;
        PRINT_TESTERR = true;

        ALLOWED_TIME = 30000;
	    
        long t1 = System.currentTimeMillis();
        test("depth2", TestingInputs.FENS_TO_TEST.length);
        long t2 = System.currentTimeMillis();
        System.err.println("Depth 2 time: " + (t2-t1));
        test("depth3", TestingInputs.FENS_TO_TEST.length);
        long t3 = System.currentTimeMillis();
        System.err.println("Depth 3 time: " + (t3-t2));
        test("depth4", TestingInputs.FENS_TO_TEST.length);
        long t4 = System.currentTimeMillis();
        System.err.println("Depth 4 time: " + (t4-t3));
		
		finish();
	} 
}
