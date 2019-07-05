import java.awt.*;

public class Layer {

    private double ratio;
    private Color colour;

    public double getRatio() {
        return ratio;
    }

    public Color getColour() {
        return colour;
    }

    public Layer(double ratio, Color colour){
        this.ratio = ratio;
        this.colour = colour;
    }

}
