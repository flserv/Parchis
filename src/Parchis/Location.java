package Parchis;

/**
 *
 * @author Paco
 */
public class Location {

    private int square;
    private int subSquare;

    public Location() {
        int square = 0;
        int subSquare = 1;
    }

    public Location(int aSquare, int aSubSquare) {
        this.square = aSquare;
        this.subSquare = aSubSquare;
    }

    public int getSquare() {
        return this.square;
    }

    public int getSubSquare() {
        return this.subSquare;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public void setSubSquare(int subSquare) {
        this.subSquare = subSquare;
    }

}
