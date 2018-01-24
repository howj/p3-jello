package traffic;

import cse332.chess.interfaces.Evaluator;

public class TrafficEvaluator implements Evaluator<TrafficPosition> {
    private static final int SEC_PER_MIN = 60;
    private static final int INFINITY = 1000000;
    
    private int maxMinutes;
    
    public TrafficEvaluator(int min) {
        this.maxMinutes = min;
        
    }

    @Override
    public int infty() {
        return INFINITY;
    }

    /* In the context of the traffic "game", "stalemate" is meaningless */
    public int stalemate() { 
        throw new RuntimeException("There is no such thing as stalemate here...");
    }
    
    /* In the context of the traffic "game", "mate" is meaningless */
    public int mate() { 
        throw new RuntimeException("There is no such thing as mate here...");
    }

    @Override
    public int eval(TrafficPosition pos) {
        if (pos.getTrafficSeconds() > maxMinutes * SEC_PER_MIN || !pos.atDestination()) {
            return (pos.isMyTurn() ? 1 : -1) * -INFINITY;
        }
        else {
            return (pos.isMyTurn() ? 1 : -1) * (int) Math.round(INFINITY * (1 - (pos.getTrafficSeconds()/pos.getSeconds())));
        }
    }
}
