package traffic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import cse332.chess.interfaces.Move;

public class RoadSegment implements Move<RoadSegment> {
    private static Map<String, RoadSegment> roadSegmentsByAddress = new HashMap<>();
    private final double MILES_PER_FT = 0.000189394;
    private final double HR_PER_SEC = 0.000277778;

    private final String repr;
    private final String fromAddress;
    private final String toAddress;
    private final Location fromLocation;
    private final Location toLocation;
    private final double length;
    private final Double throughput;
    private final int speedLimit;
    private final Set<RoadSegment> nextLocations;
    
    private RoadSegment(String from, String to, String fLoc, String tLoc,
            String len, String throughput, String speed) {
        this.fromAddress = from;
        this.toAddress = to;
        this.fromLocation = new Location(fLoc);
        this.toLocation = new Location(tLoc);
        this.length = Double.parseDouble(len);
        if (!throughput.equals("None")) {
            this.throughput = Double.parseDouble(throughput);
        }
        else {
            this.throughput = null;
        }
        this.speedLimit = Integer.parseInt(speed);
        
        this.repr = from + " -- " + to; 

        this.nextLocations = new HashSet<>();
    }
    
    public static void initialize() {
        try {
            Scanner scan = new Scanner(new File("downtown_seattle.graph"));
            while (scan.hasNextLine()) {
                getOrMake(scan.nextLine());
            }
            scan.close();
            scan = new Scanner(new File("downtown_seattle.graph"));
            while (scan.hasNextLine()) {
                String[] parts = scan.nextLine().split("[|]");
                RoadSegment from = lookupByAddress(parts[0]);
                RoadSegment to = lookupByAddress(parts[5]);
                from.addMove(to);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The Seattle data file is missing.");
        }
        
    }

    private static RoadSegment getOrMake(String initializationString) {
        String[] parts = initializationString.split("[|]");
        RoadSegment existing = lookupByAddress(parts[0]);
        if (existing != null) {
            return existing;
        }
        
        String[] addrs = parts[0].split("--");
        String[] locs = parts[1].split("--");
        String length = parts[2];
        String throughput = parts[3];
        String speed = parts[4];
        
        RoadSegment seg = new RoadSegment(addrs[0].trim(), addrs[1].trim(), locs[0].trim(), locs[1].trim(), length, throughput, speed);
        roadSegmentsByAddress.put(parts[0], seg);
        return seg;
    }

    public static RoadSegment lookupByAddress(String addrString) {
        if (roadSegmentsByAddress.containsKey(addrString)) {
            return roadSegmentsByAddress.get(addrString);
        }
        return null;
    }
    
    public String getStartAddress() { return this.fromAddress; }
    public String getEndAddress()   { return this.toAddress; }

    public Location getStartLocation() { return this.fromLocation; }
    public Location getEndLocation()   { return this.toLocation; }

    public double getDistance()   { 
        return this.length * MILES_PER_FT;
    }
    
    public double timeAtMax() {
        return getDistance() / getMaxSpeed();
    }
    
    public double timeAtMin() {
        return getDistance() / getMinSpeed();
    }
    
    public double getMaxSpeed()   { 
        return this.speedLimit * HR_PER_SEC;
    }
    
    public double getMinSpeed()   { 
        // throughput = largest number of cars in an hour period
        // The segment has length ft length.
        
        // throughput cars/hr * length ft/car * mi/ft = mi/hr
        
        // ex: length = 321 ft, throughput = 221
        // 221*321*0.000189394
        
        if (this.throughput == null) {
            return getMaxSpeed();
        }

        return Math.min(getMaxSpeed(), this.throughput * this.length * MILES_PER_FT * HR_PER_SEC); 
    }
    
    public void addMove(RoadSegment seg) {
        this.nextLocations.add(seg);
    }
    
    public Set<RoadSegment> getMoves() {
        return this.nextLocations;
        
    }

    public String toString() {
        return this.repr;
    }
    
    public int hashCode() {
        return this.repr.hashCode();
    }

    @Override
    public RoadSegment create() {
        return this;
    }

    @Override
    public RoadSegment copy() {
        return this;
    }

    @Override
    public boolean isCapture() {
        return false;
    }

    @Override
    public boolean isPromotion() {
        return false;
    }

    @Override
    public int srcRow() {
        return -1;
    }

    @Override
    public int srcCol() {
        return -1;
    }

    @Override
    public int destRow() {
        return -1;
    }

    @Override
    public int destCol() {
        return -1;
    }

    @Override
    public String serverString() {
        return this.toString();
    }
}
