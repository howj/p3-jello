package traffic;

import java.awt.Point;

public class Location {
    private final Point loc;

    public Location(double lat, double lon) {
        this.loc = new Point();
        this.loc.setLocation(lat, lon);
    }

    public Location(String fromString) {
        String[] coords = fromString.substring(1, fromString.length() - 1)
                .split(", ");
        this.loc = new Point();
        this.loc.setLocation(
            Double.parseDouble(coords[0]),
            Double.parseDouble(coords[1])
        );
    }
 
    public String toString() {
        return "(" + loc.getX() + ", " + loc.getY() + ")";
    }
   
    public int hashCode() {
        return this.loc.hashCode();
    }
}