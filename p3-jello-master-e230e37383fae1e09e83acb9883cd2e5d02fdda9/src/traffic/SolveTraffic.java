package traffic;

import traffic.RoadSegment;
import traffic.TrafficEvaluator;
import traffic.TrafficPosition;
import cse332.chess.interfaces.Searcher;

public class SolveTraffic {
    public Searcher<RoadSegment, TrafficPosition> searcher;
    public static final String SOURCE = "900, BROADWAY, SEATTLE, WA 98122 -- 998, BROADWAY, SEATTLE, WA 98122";
    public static final String DESTINATION = "901, E PINE ST, SEATTLE, WA 98122 -- 949, E PINE ST, SEATTLE, WA 98122";
    
    private TrafficPosition position;

    public static void main(String[] args) {
        SolveTraffic game = new SolveTraffic();
        game.play();
    }

    public SolveTraffic() {
        searcher = new TrafficSearcher<RoadSegment, TrafficPosition>();
        searcher.setDepth(21);
        searcher.setCutoff(12);
        searcher.setEvaluator(new TrafficEvaluator(3));
    }

    public void play() {
        RoadSegment.initialize();

        this.position = TrafficPosition.FACTORY.create().init(SOURCE);
        this.position.setDestination(DESTINATION);

        while (!position.atDestination()) {
            position.applyMove(null);
            RoadSegment move = searcher.getBestMove(position, 1000, 1000);
            position.applyMove(move);
            System.out.println("Made a move: " + position.toString());
        }
    }

}
