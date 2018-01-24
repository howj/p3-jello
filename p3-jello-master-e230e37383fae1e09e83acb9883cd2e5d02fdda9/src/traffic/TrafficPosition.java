package traffic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import cse332.chess.interfaces.Board;

public class TrafficPosition implements Board<RoadSegment,TrafficPosition> {
    public static final TrafficPosition FACTORY = new TrafficPosition();
    private Stack<RoadSegment> locations;
    private double secondsDriving;
    private double secondsLostToTraffic; 
    private RoadSegment destination;
    private boolean myMove = true;
    
    private TrafficPosition() {}

    public void setDestination(String dest) {
        this.destination = RoadSegment.lookupByAddress(dest);
    }
    
    public boolean atDestination() {
        return this.locations.peek() == destination;
    }
    
        
    public double getSeconds() {
        return secondsDriving;
    }
    
    public double getTrafficSeconds() {
        return secondsLostToTraffic;
    }
    
    public List<RoadSegment> getPath() {
        List<RoadSegment> path = new ArrayList<>(this.locations);
        Collections.reverse(path);
        return path;
    }
    
    public boolean isMyTurn() {
        return this.myMove;
    }
    
    public void applyMove(RoadSegment location) {
        if (myMove) {
            this.locations.push(location);
            this.secondsDriving += location.timeAtMin();
        }
        else {
            RoadSegment loc = this.locations.peek();
            this.secondsLostToTraffic += loc.timeAtMin() - loc.timeAtMax();
        }
        myMove = !myMove;
    }
    
    public void undoMove() {
        if (myMove) {
            RoadSegment loc = this.locations.peek();
            this.secondsLostToTraffic -= loc.timeAtMin() - loc.timeAtMax();
        }
        else {
            RoadSegment location = this.locations.pop();
            this.secondsDriving -= location.getDistance() / location.getMinSpeed();
        }
        myMove = !myMove;
    }
    
    private List<RoadSegment> getMoves() {
        if (this.atDestination() && myMove) {
            return new ArrayList<RoadSegment>();
        }
        List<RoadSegment> moves = new ArrayList<RoadSegment>();
        if (myMove) {
            for (RoadSegment seg : this.locations.peek().getMoves()) {
                if (!this.locations.contains(seg)) {
                    moves.add(seg);
                }
            }
        }
        else {
            moves.add(this.locations.peek());
        }
        return moves;
    }

    @Override
    public TrafficPosition create() {
        return new TrafficPosition();
    }

    @Override
    public TrafficPosition copy() {
        TrafficPosition copy = create();
        copy.locations = (Stack<RoadSegment>) new Vector<RoadSegment>(this.locations);
        copy.secondsDriving = this.secondsDriving;
        copy.secondsLostToTraffic = this.secondsLostToTraffic;
        copy.myMove = this.myMove;
        copy.destination = this.destination;
        return copy;
    }
    
    @Override
    public TrafficPosition init(String start) {
        this.locations = new Stack<>();
        this.applyMove(RoadSegment.lookupByAddress(start));
        return this;
    }

    @Override
    public List<RoadSegment> generateMoves() {
        return getMoves();
    }

    @Override
    public RoadSegment createMoveFromString(String move) {
        return RoadSegment.lookupByAddress(move);
    }

    @Override
    public List<RoadSegment> generatePseudoMoves() {
        return getMoves();
    }
    
    public String toString() {
        String repr = "Seconds: " + Math.round(this.secondsDriving) + ", Traffic: " + Math.round(this.secondsLostToTraffic) + ", [";
        for (RoadSegment seg : locations) {
            repr += seg.toString().split(" -- ")[0] + " | ";
        }
        if (repr.length() > 1) {
            repr = repr.substring(0, repr.length() - 3);
        }
        return repr + "]";
    }

    @Override
    public boolean isLegalPseudoMove(RoadSegment move) { return isLegalMove(move); }

    @Override
    public boolean isLegalMove(RoadSegment move) { 
        return locations.peek().getMoves().contains(move);
    }

    @Override
    public boolean inCheck() { return false; }

    @Override
    public int toPlay() { return -1; }

    @Override
    public int plyCount() {
        return -1;
    }

    @Override
    public long signature() {
        return -1;
    }

    @Override
    public String fen() {
        return null;
    }
}
