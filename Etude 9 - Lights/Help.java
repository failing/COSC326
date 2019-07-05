import java.awt.*;

import javax.swing.*;

public class Help {
    JFrame frame;
    JPanel main_panel;
    JLabel info;

    Help() {
        frame = new JFrame("Help!");

        main_panel = new JPanel();

        //should probably change, looks awful
        info = new JLabel("<html><center>Welcome!</center><br>Put your lights in as single letters only in the top text box.<br> If no connecting switches are present when loading then it is assumed" +
                " there are no edges between the lights. <br>A light must exist if you wish to create edges for it." +
                "<br>You may click on a light to toggle it off/on.<br>When loading a consecutive set of lights please close the viewing window each time before pressing load." +
                "<br>Clicking 'Show Instructions' will show the buttons to click step by step to solve the lights. <br>" +
                "Lights are on when they are green, and lights are off when they are blue.<br>A circuit is considered complete when all lights are blue.</html>");

        main_panel.add(info);

        main_panel.setLayout(new FlowLayout());

        frame.add(main_panel);



        frame.pack();


        frame.setResizable(false);
        frame.setVisible(true);

    }


    public static void main(String args[]) {
        new Help();
    }
}
