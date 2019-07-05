import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

public class GUI {
    //setup all the widgets
    JFrame frame;
    JPanel button_panel, fields_panel, info_panel;
    JLabel lights, switches_label,in;
    JTextField lights_field, switches_field;
    JTextArea instructions;
    JButton ok, solve, reset,show_instructions,show_help;
    App.Lights lights_app;
    Thread thread;

    /**
     * Sets up all the gui frame
     */
    public GUI() {

        frame = new JFrame("Lights With GUI: Load Value");

        button_panel = new JPanel();
        fields_panel = new JPanel();
        info_panel = new JPanel();

        lights = new JLabel("Switches: ");
        switches_label = new JLabel("Connecting Switches: ");
        in = new JLabel("Instructions: ");
        show_help = new JButton("Show Help");
        lights_field = new JTextField("");
        switches_field = new JTextField("");
        instructions = new JTextArea(20,20);
        instructions.setEditable(false);



        ok = new JButton("Load");
        ok.addActionListener(this::Load);
        reset = new JButton("Reset");
        reset.addActionListener(this::reset);
        solve = new JButton("Solve Step By Step");
        solve.addActionListener(this::solve);
        show_help.addActionListener(this::get_help);
        show_instructions = new JButton("Show Instructions");
        show_instructions.addActionListener(this::show);

        fields_panel.setLayout(new BoxLayout(fields_panel, BoxLayout.PAGE_AXIS));
        info_panel.setLayout(new BoxLayout(info_panel, BoxLayout.LINE_AXIS));
        button_panel.setLayout(new FlowLayout());

        fields_panel.add(lights);
        fields_panel.add(lights_field);
        fields_panel.add(switches_label);
        fields_panel.add(switches_field);
        fields_panel.add(in);
        fields_panel.add(instructions);

        button_panel.add(ok);
        button_panel.add(solve);
        button_panel.add(reset);
        button_panel.add(show_instructions);
        button_panel.add(show_help);

        frame.add(fields_panel, BorderLayout.PAGE_START);
        frame.add(button_panel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

    }

    /**
     * Opens the Help App
     * @param e
     */
    public void get_help(ActionEvent e){
        new Help();
    }

    /**
     * Runs through the steps (which the user can't see) to get the list of instructions
     * @param e
     */
    public void show(ActionEvent e){

        if (lights_field.getText().length() == 0){
            return;
        }


        lights_app.reset();
        lights_app.show();
        instructions.setText(lights_app.how_to_solve);
        lights_app.reset();

    }

    /**
     * Reset the lights
     * @param e
     */
    public void reset(ActionEvent e){
        if (lights_field.getText().length() == 0){
            return;
        }
        lights_app.reset();
    }

    /**
     * Passes all the switches and nodes to the viewer app.
     *
     * @param e
     */
    public void Load(ActionEvent e){

        //reset the instruction text box
        instructions.setText("");
        if (lights_field.getText().length() == 0){
            JOptionPane.showMessageDialog(null, "Please enter values for at least the lights textbox!");
            return;
        }

        String[] nodes = lights_field.getText().split(" ");

        //Just to make sure all the lights are upper case
        for(int i = 0;i<nodes.length;i++){
            nodes[i] = nodes[i].toUpperCase();
        }

        String[] connecting_switches = null;

        //If switches is blank then just set switches to nodes
        if(switches_field.getText().equals("")){
            connecting_switches = nodes;
        }else{
            connecting_switches = switches_field.getText().split(" ");
        }

        //If switches and nodes aren't the same, validate the switches & nodes
        if (!Arrays.equals(connecting_switches,nodes)){

            if(!validate_switches(connecting_switches,nodes)){
                JOptionPane.showMessageDialog(null, "Please check switches!");
                return;
            }
        }

        if(!validate_lights(nodes)){
            JOptionPane.showMessageDialog(null, "Please check lights!");
            return;
        }

        App threadControl = new App();
        lights_app = threadControl.new Lights(nodes,connecting_switches);
        thread = new Thread(lights_app);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Solves the lights step by step
     * @param e
     */
    public void solve(ActionEvent e){


        if (lights_field.getText().length() == 0){
            return;
        }
        lights_app.solve();
    }


    /**
     * Validates all the switches
     * @param switches
     * @param n
     * @return
     */
    public boolean validate_switches(String[] switches,String[] n){

        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, n);

        for(String edge:switches){
            if(edge.length() != 2){
                return false;
            }
            if(!list.contains(String.valueOf(edge.charAt(0)))){
                return false;
            }

            if(!list.contains(String.valueOf(edge.charAt(1)))){
                return false;
            }
        }

        return true;
    }

    /**
     * Validates all the lights
     * @param n
     * @return
     */
    public boolean validate_lights(String[] n){

        /* Lights must over be one character long */
        for (String a: n) {
            if(a.length() > 1){
                System.out.println(a);
                return false;
            }
        }

        /* Duplicates for lights */
        for (int i =0;i<n.length;i++){
            for (int j=i+1;j<n.length;j++){
                if (i!=j && n[i].equals(n[j]))
                {
                    return false;
                }
            }
        }

        return true;

    }

    public static void main(String args[]) {
        new GUI();
    }
}
