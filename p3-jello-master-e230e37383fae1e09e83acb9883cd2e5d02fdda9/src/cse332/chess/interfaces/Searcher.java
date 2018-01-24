package cse332.chess.interfaces;

import java.util.Observer;

public interface Searcher<M extends Move<M>, B extends Board<M, B>> {
    /**
     * Searches the game tree and selects the best move based on some heuristics
     * and algorithms.
     * 
     * @param board the current board position.
     * @param myTime the remaining time on your clock.
     * @param opTime the remaining time on your opponent's clock.
     * @return the best move from this current position.
     */
    public M getBestMove(B board, int myTime, int opTime);

    /**
     * Sets the number of ply to look ahead.
     * 
     * @param depth  the number of ply to look ahead
     *            
     */
    public void setDepth(int depth);

    
    /**
     * Sets the cut-off to switch from parallel to sequential for a searcher
     * that uses parallelism.
     * 
     * @param the cut-off
     */
    public void setCutoff(int cutoff);

    /**
     * Tells this Searcher to use a new timer.
     * 
     * @param t the new timer.
     */
    public void setTimer(Timer t);

    /**
     * Tells this Searcher to use a new evaluator.
     * 
     * @param e the new evaluator.
     */
    public void setEvaluator(Evaluator<B> e);

    /**
     * Add a new Observer to the list of Observers to notify when the Searcher's
     * state changes (such as when a new best move is found during the search).
     * This is typically done by extending the Observable class and using its
     * addObserver, setChanged, and notifyObservers methods.
     * 
     * @param o the new Observer
     */
    public void addBestMoveObserver(Observer o);
}
