/**
 *
 *  COSC326 2019 Cordless Phones
 *  Ben Highsted
 *  Bradley Carter
 *  Matthew Neil
 *
 **/

import edu.wlu.cs.levy.CG.KDTree;
import java.util.*;
import java.util.stream.Collectors;


public class Cordless {

    private static ArrayList<Double> inputs1 = new ArrayList <>();
    private static ArrayList <Double> inputs2 = new ArrayList <>();

    private static double[][] points;

    public static void main(String [] args){

        get_input();

        gen_points();

        get_range();


    }

    public static void gen_points(){

        if(inputs1.size() != inputs2.size()){
            return;
        }

        //2d array of points size of input1
        //this assume equal amount of x y points of input1 and input2
        points = new double[inputs1.size()][2];
        // Add all x,y to a 2d array
        for(int i =0;i<points.length;i++){
            points[i][0] = inputs1.get(i);
            points[i][1] = inputs2.get(i);
        }

    }
    public static void get_input(){
        Scanner scan = new Scanner(System.in);

        String tele = scan.nextLine();
        if(!tele.toLowerCase().equals("telephone sites")){
            System.out.println("Invalid Input");
            return;
        }

        try{
            while(scan.hasNextLine()){
                String l = scan.nextLine();
                Scanner a = new Scanner(l);
                double t = Double.parseDouble(a.next());
                double tt = Double.parseDouble(a.next());
                inputs1.add(t);
                inputs2.add(tt);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        scan.close();


    }

    public static void get_range(){


        /*
        if(points.length <= 11){
            System.out.println("Less than 11 points! Radius is infinity!");
            return;
        }
        */
        

        KDTree<Integer> kd = new KDTree<>(2);

        try {
            //insert each point into tree
            for (int i = 0; i < points.length; ++i) {
                kd.insert(points[i], i);
            }

            ArrayList<Circle> circles = new ArrayList<>();

            for (int i = 0; i < points.length; i++) {
                List<Integer> nbrs = kd.nearest(points[i], 12);
                List<Point> p = new ArrayList<>();
                for (int j : nbrs) {
                    p.add(new Point(points[j][0], points[j][1]));
                }

                Circle A = SmallestEnclosingCircle.makecircle(p);
                A.points_list = p;

                circles.add(A);

                 for(Circle c: circles){
                    if(c.contains(p)){
                        System.out.println("True");
                    }
            }


            }

           
            //case
            Set<String> test = circles.stream()
                    .map(Circle::get_rad)
                    .collect(Collectors.toSet());

            ArrayList<String> a = new ArrayList<>();

            a.addAll(test);

            if (test.size() < 2){
                System.out.println("Max Radius: " + a.get(0));
                return;
            }

            ArrayList<Circle> important_circles = new ArrayList<>();
            for (Circle C : circles) {
                int count = 0;
                for (int i = 0; i < points.length; i++) {
                    if (C.contains(new Point(points[i][0], points[i][1]))) {
                        count++;
                    }
                }
                if (count == 12) {
                    important_circles.add(C);
                }

            }


            important_circles.sort(Comparator.comparing(x -> x.r));
            Collections.reverse(important_circles);

            //all points made up from our imortant circles
            ArrayList<Point> all_points = new ArrayList<>();

            for(Circle c:important_circles){
                all_points.addAll(c.points_list);
            }


            ArrayList<Double> duplicates = new ArrayList<>();
            ArrayList<String> pointDuplicates = new ArrayList<>();

            ArrayList<Circle> three_point_circles = new ArrayList<>();
            System.out.println("Calculating Circles...");
            for(int i = 0;i<all_points.size();i++){
                for(int j = i;j<all_points.size();j++){
                    if(j!=i){
                        for(int k = j;k<all_points.size();k++){
                            if(k!=j){
                                Point aa = all_points.get(i);
                                Point bb = all_points.get(j);
                                Point cc = all_points.get(k);

                                List<Point> al = new ArrayList<>();
                                al.add(aa);
                                al.add(bb);
                                al.add(cc);

                                Circle A = SmallestEnclosingCircle.makecircle(al);

                                if(!duplicates.contains(A.r)){
                                    three_point_circles.add(A);
                                    duplicates.add(A.r);
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Done.");

            three_point_circles.sort(Comparator.comparing(x -> x.r));

            List<Circle> filter_circles = three_point_circles.stream().distinct().collect(Collectors.toList());

            ArrayList<Double> rads = new ArrayList<>();
            ArrayList<Circle> new_circles = new ArrayList<>();

            for (Circle aa: filter_circles) {
                if(rads.contains(aa.r) == false){
                    new_circles.add(aa);
                    rads.add(aa.r);
                }
            }

            new_circles.sort(Comparator.comparing(x->x.r));

            Circle new_biggest = new_circles.remove(0);

            int index = 0;
            for(int i = 0;i<new_circles.size();i++){
                int count = 0;
                for(int j = 0;j<points.length;j++){
                    if(new_biggest.contains(new Point(points[j][0],points[j][1]))){
                        System.out.println("true");
                        count++;
                    }
                }

                if (count >= 12){
                    new_biggest = new_circles.get(--index);
                    break;
                }else{
                    new_biggest =  new_circles.get(index++);
                }
            }

            System.out.println("Max Radius: " + new_biggest.r);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}