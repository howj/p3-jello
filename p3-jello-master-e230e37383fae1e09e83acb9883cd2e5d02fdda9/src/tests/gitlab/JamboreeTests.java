package tests.gitlab;

import chess.board.ArrayBoard;
import chess.board.ArrayMove;
import chess.game.SimpleEvaluator;
import cse332.chess.interfaces.Move;
import cse332.chess.interfaces.Searcher;

import chess.bots.JamboreeSearcher;

import tests.TestsUtility;
import tests.gitlab.TestingInputs;

public class JamboreeTests extends SearcherTests {

    public static void main(String[] args) { new JamboreeTests().run(); }
    public static void init() { STUDENT = new JamboreeSearcher<ArrayMove, ArrayBoard>(); }

	
	@Override
	protected void run() {
        SHOW_TESTS = true;
        PRINT_TESTERR = true;

        ALLOWED_TIME = 1500;
	    
        long t1 = System.currentTimeMillis();
        test("depth2", TestingInputs.FENS_TO_TEST.length);
        long t2 = System.currentTimeMillis();
        System.err.println("Depth 2 time: " + (t2-t1));
        test("depth3", TestingInputs.FENS_TO_TEST.length);
        long t3 = System.currentTimeMillis();
        System.err.println("Depth 3 time: " + (t3-t2));
        
        ALLOWED_TIME = 5000;
        
        test("depth4", TestingInputs.FENS_TO_TEST.length);
        long t4 = System.currentTimeMillis();
        System.err.println("Depth 4 time: " + (t4-t3));

        ALLOWED_TIME = 60000;
        test("depth5", TestingInputs.FENS_TO_TEST.length);
        long t5 = System.currentTimeMillis();
        System.err.println("Depth 5 time: " + (t5-t4));
//        ALLOWED_TIME = 180000;
//        test("depth6", TestingInputs.FENS_TO_TEST.length);
//        long t6 = System.currentTimeMillis();
//        System.err.println("Depth 6 time: " + (t6-t5));
        
		finish();
	} 
}
