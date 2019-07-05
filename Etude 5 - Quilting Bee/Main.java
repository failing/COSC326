import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Frame {
    /* The size of the window*/
    private static int x = 512;
    private static int y = 512;
    private static int length = 0;
    private static ArrayList<Layer> layers = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        double max = 0.0;

        System.out.println("scale r g b");

        /* while we have more input, ask for a new layer of a ratio and rgb values*/
        while(scan.hasNext()){
                double r = scan.nextDouble();
                max += r/2;
                /* add our layer to a list */
                Layer l = new Layer(r,new Color(scan.nextInt(),scan.nextInt(),scan.nextInt()));
                layers.add(l);
        }

        /* Calculates the initial length, from the max distance from centre */
        double initial_length = (x/2)/max;

        length = ((int)initial_length);
        scan.close();

	    new Main().run();

    }

    private void run(){
        setUndecorated(true);
        pack();
        setSize(x,y);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        /* draws the first square in the center of the window*/
        squares(g,x/2,y/2,layers.size());
    }

    private void squares(Graphics g, int x, int y, int n) {

        /* base case to stop the recursion */
        if (n == 0) return;

        /* Get the layer based on the depth/n */
        Layer temp = layers.get(layers.size()-n);
        /* draw the square, get the size based on the current depth/layer */
        int size = (int) (length*temp.getRatio());
        /* set the colour of the layer*/
        g.setColor(temp.getColour());
        
        int half = size/2;
        /* draw the square */
        g.fillRect((x-size/2), (y-size/2), size, size);

        squares(g,x-half,y+half,n-1); /* Bottom left */
        squares(g,x+half,y+half,n-1); /* Bottom right */
        squares(g,x-half,y-half,n-1); /* Top left */
        squares(g,x+half,y-half,n-1); /* Top right */

    }

}
