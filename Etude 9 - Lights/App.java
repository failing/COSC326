
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import sun.print.CUPSPrinter;

import java.awt.*;
import java.util.Arrays;

public class App{

    public class Lights implements ViewerListener,Runnable {
        protected boolean loop = true;
        Graph graph;
        Viewer viewer;
        ViewerPipe fromViewer;
        String[] nodes;
        String[] edges;
        int edges_count;
        int node_count;
        int max_tries = 0;
        int current_tries = 0;

        public String how_to_solve = "";


        /**
         * Add's all the lights/nodes to the graph.
         * Sets up every light to On
         */
        public void run(){


            System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

            graph = new SingleGraph("Lights On ?");
            graph.addAttribute("ui.stylesheet", "node { shape: freeplane; fill-mode: dyn-plain; size: 30px; text-color: red; } edge { fill-mode: dyn-plain; }");
            viewer = graph.display();
            fromViewer = viewer.newViewerPipe();
            fromViewer.addViewerListener(this);
            fromViewer.addSink(graph);

            viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

            //Add all lights
            for(int i = 0;i<nodes.length;i++){
                graph.addNode(nodes[i]).addAttribute("ui.label", nodes[i]);
                graph.getNode(nodes[i]).addAttribute("On",true);
                graph.getNode(nodes[i]).addAttribute("ui.color", Color.GREEN);
            }

            //Add all edges (if there are any)
            if (!Arrays.equals(nodes,edges)){
                for(int i = 0;i<edges.length;i++){
                    graph.addEdge(edges[i],String.valueOf(edges[i].charAt(0)),String.valueOf(edges[i].charAt(1)),true);
                }
            }

            //just a general rule
            max_tries = nodes.length * 2;

            //to get the viewer to show from another thread
            while(loop){
                fromViewer.pump();
            }
        }

        /**
         * Sets the node/edges field
         * @param nodes
         * @param edges
         */
        public Lights(String[] nodes,String[] edges){

            this.nodes = nodes;
            this.edges = edges;
            this.edges_count = edges.length;
            this.node_count = nodes.length;

        }

        public void viewClosed(String id) {
            loop = false;
            Thread.currentThread().interrupt();
        }

        /**
         * Toggles the clicked on light.
         */
        public void buttonPushed(String id) {
            toggle(id);
        }

        public void buttonReleased(String id){
            return;
        }

        /**
         * Get's the list of instructions by completing the circuit
         */
        public void show(){

            this.how_to_solve = "";
            current_tries = 0;

            while(!all_lights_off() && current_tries < max_tries){
                solve();
                if (this.how_to_solve.charAt(0) == this.how_to_solve.charAt(this.how_to_solve.length()-1) && current_tries >= 2){
                    pretty_steps();
                    return;
                }

            }

            pretty_steps();
        }

        /**
         * Sets all the lights back to on.
         */
        public void reset(){
            current_tries = 0;

            for(Node a :graph){
                a.setAttribute("On",true);
            }
            update_colours();
        }
        /**
         * Tries to solve the circuit by, either; toggling a light that has no connections to/from it at all. If a light, like that doesn't
         * exist, it then gets the light with the most outs and toggles that light. If that light is null then we go through all nodes and if they are on we toggle each one.
         * We do this until current_tries >= max_tires. If we go over this limit we determine that it can't be fully solved.
         */
        public void solve(){

            current_tries++;

            if(current_tries >= max_tries){
                get_closest();
                return;
            }


            if(all_lights_off()){
                return;
            }

            Node b = single_node();
            Node c = one_connection();

            if(b != null) {
                toggle(b.toString());
                this.how_to_solve += b.toString();
                if(loop_instructions()){
                    current_tries = max_tries;
                }
                return;
            }else{
                if(c != null){
                    toggle(c.toString());
                    this.how_to_solve += c.toString();
                    if(loop_instructions()){
                        current_tries = max_tries;
                    }
                    return;
                }else{
                    Node to = get_most_outs();
                    if(to!=null){
                        toggle(to.toString());
                        this.how_to_solve += to.toString();
                        if(loop_instructions()){
                            current_tries = max_tries;
                        }
                        return;
                    }else{
                        for(Node n: graph){
                            if(n.getAttribute("On").equals(true)){
                                toggle(n.toString());
                                this.how_to_solve += n.toString();
                                if(loop_instructions()){
                                    current_tries = max_tries;
                                }
                                return;
                            }
                        }
                    }
                }
            }


            if(all_lights_off()){
                return;
            }
        }
        /**
         * Gets the list of nodes to click, if all lights can't be switched off.
         */
        public void get_closest(){
            this.how_to_solve = "";
            for (Node a: graph) {
                if(a.getAttribute("On").equals(false)){
                    this.how_to_solve += a.toString();
                }
            }
        }

        public boolean loop_instructions(){
            if((this.how_to_solve.charAt(0) == this.how_to_solve.charAt(this.how_to_solve.length()-1) && current_tries >= 2)){
               return true;
            }
            return false;
        }

        /**
         * To make the steps output more presentable
         */
        public void pretty_steps() {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < this.how_to_solve.length(); i++) {
                sb.append(String.format("Step %d: ", i+1));
                sb.append(this.how_to_solve.charAt(i));
                sb.append("\n");
            }

            this.how_to_solve = sb.toString();

        }

        /**
         * Toggles the light that is passed in and all lights that is has a connection to.
         * It will be update the colours accordingly.
         * @param id
         */
        public void toggle(String id){

            //Get the current Node
            Node get_current_node = graph.getNode(id);

            boolean current = get_current_node.getAttribute("On");

            get_current_node.setAttribute("On",!current);

            for(Edge e: get_current_node.getEachLeavingEdge()) {
                get_current_node = e.getNode1();
                current = get_current_node.getAttribute("On");
                get_current_node.setAttribute("On",!current);
            }


            update_colours();
        }


        public Node one_connection(){

            for(Node n:graph){
                if (n.getAttribute("On").equals(true)) {
                    int count = 0;
                    for (Edge e: n.getEachLeavingEdge()) {
                        count++;
                    }
                    if(count == 1){
                        return n;
                    }
                }

            }

            return null;
        }
        /**
         * Returns a light that is on it's own
         * @return
         */
        public Node single_node(){

            for(Node n:graph){
                if (n.getAttribute("On").equals(true)) {
                    int count = 0;
                    for (Edge e: n.getEdgeSet()) {
                        count++;
                    }
                    if(count == 0){
                        return n;
                    }
                }

            }

            return null;
        }

        /**
         * If a light has a connection back and forward between two lights
         * @param a
         * @param b
         * @return
         */
        public boolean looped(Node a, Node b){
            if (a.hasEdgeFrom(b) && b.hasEdgeFrom(a)){
                return true;
            }
            return false;
        }

        /**
         * Returns the light with the most connections from it
         * @return
         */
        public Node get_most_outs(){

            int max = 0;
            Node max_node = null;


            for (Node l: graph.getNodeSet()) {
                if (l.getAttribute("On").equals(false)) continue;
                int count = 0;
                for (Edge e: l.getEachLeavingEdge()) {
                    if(e.getNode1().getAttribute("On").equals(true)){
                        count++;
                    }
                }
                if (count>max){
                    max = count;
                    max_node = l;
                }
            }
            return max_node;
        }

        /**
         * Updates the colours of the graph after we toggle some lights
         */
        public void update_colours(){
            for (Node a: graph.getNodeSet()) {
                if(a.getAttribute("On").equals(true)){
                    a.setAttribute("ui.color",Color.GREEN);
                }else{
                    a.setAttribute("ui.color",Color.BLUE);
                }
            }
        }

        /**
         * Returns the current amount of lights that are on
         * @return
         */
        public int amount_of_lights(){

            int count = 0;
            for (Node a: graph.getNodeSet()) {
                if(a.getAttribute("On").equals(true)){
                    count++;
                }
            }
            return count;
        }

        /**
         * Returns if all the lights are off
         * @return
         */
        public boolean all_lights_off(){

            for (Node a: graph.getNodeSet()) {
                if(a.getAttribute("On").equals(true)){
                    return false;
                }
            }
            return true;
        }
    }

}
