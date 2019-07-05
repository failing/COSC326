/**
 *
 * Etude 8 Hole Patches COSC326 2019
 * Brad Carter, Ben Highsted, Matthew Neil
 *
 * Takes an input of a sheet with holes and a patch size, and using that input calculates where patches need to
 * be placed to fix the sheet.
 *
 */

import java.util.*;
//import edu.wlu.cs.levy.CG.KDTree;

public class HolePatches {
  public static ArrayList<Double> holeX =  new ArrayList<>();
  public static ArrayList<Double> patchX = new ArrayList<>();
  public static ArrayList<Double> patchY = new ArrayList<>();
  public static ArrayList<Double> holeY  = new ArrayList<>();
  public static ArrayList<Double> holeSize =  new ArrayList<>();
  public static double[][] holeXY;
  public static double patchSize;
  public static double sheetX;
  public static double sheetY;
  
  
  public static void main(String[] args){
    
    
    Scanner scan = new Scanner(System.in);
    String holes;
    
    while(scan.hasNextLine()){
      boolean invalid = false;
      //Sheet Size
      String sheet = scan.nextLine();
      String[] sheetArray = sheet.split(" ");
      
      if(!(sheetArray[0].equals("Sheet") && sheetArray[1].equals("Size"))){
        invalid = true;
        //System.out.println("G");
      }
      
      sheetX = Double.parseDouble(sheetArray[2]);
      sheetY = Double.parseDouble(sheetArray[3]);
      
      //Patch Size
      String patch = scan.nextLine();
      String[] patchArray = patch.split(" ");
      
      if(!(patchArray[0].equals("Patch") && patchArray[1].equals("Size"))){
        invalid = true;
      }
      
      patchSize = Double.parseDouble(patchArray[2]);
      
      //Positions of holes
      while(true){
        String position = scan.nextLine();
        if(!(position.equals("Positions of holes"))){
          invalid = true;
        }
        
        //Holes
        holeX = new ArrayList<Double>();
        holeY = new ArrayList<Double>();
        holeSize = new ArrayList<Double>();
        //holeSize.add(0.0);
        String[] holesArray;
        boolean fixable;
        while(true){
          holes = scan.nextLine();
          if(holes.equals("END")){
            patchX = new ArrayList<Double>();
            patchY = new ArrayList<Double>();
            holeXY = new double[holeX.size()][2];
            for(int i=0; i<holeX.size(); i++){
              holeXY[i][0] = holeX.get(i);
              holeXY[i][1] = holeY.get(i);
            }
            fixable = caseCheck();
            if(fixable){
              fixable = calculateHoles();
            }
            if(fixable){
              System.out.println("Fixable");
            } else {
              System.out.println("Not Fixable");
            }
            break;
          }
          holesArray = holes.split(" ");
          holeX.add(Double.parseDouble(holesArray[0]));
          holeY.add(Double.parseDouble(holesArray[1]));
          holeSize.add(Double.parseDouble(holesArray[2]));
        }
        if(scan.nextLine().equals("ENDFILE")){
          break;
        }
      }
      
    }
    scan.close();
  }
  
  
  public static boolean caseCheck() {
    //Case1
    //Checks if hole is too large for patch size
    for(int i = 0; i<holeX.size(); i++){
      if(holeSize.get(i) >= patchSize-0.09){
        return false;
      }
    }
    
    //Case2
    //Checks if hole is on the edge of the blanket
    for(int i=0; i<holeX.size(); i++){
      double borderXL = holeX.get(i) - (holeSize.get(i)/2);
      double borderXR = holeX.get(i) + (holeSize.get(i)/2);
      double borderYU = holeY.get(i) - (holeSize.get(i)/2);
      double borderYD = holeY.get(i) + (holeSize.get(i)/2);
      if(borderXL <= 0 || borderXR >= sheetX || borderYU <= 0 || borderYD >= sheetY){
        return false;
      }
    }
    return true;
  }
  
  public static boolean calculateHoles(){
    place_patches();
    BorderCheck();
    move_patches();
    return true;
  }
  
  public static void place_patches(){
    for(int i=0; i<holeX.size(); i++){
      patchX.add(holeX.get(i));
      patchY.add(holeY.get(i));
    }
    
    
    //print_eq();
    //System.out.println();
    /*
     for(int i = 0;i<patchY.size()-1;i++){
     double fit = fitness_function(patchX.get(i),patchY.get(i),patchSize/2,patchX.get(i+1),patchY.get(i+1),patchSize/2);
     move_patch(i,i+1,fit);
     }*/
    //print_eq();
    //System.out.println();
    
    
    
    
  }
  
  public static void move_patches(){
    for(int x=0; x<1500; x++){
      for(int i=0;i<patchY.size();i++){
        double largest = 0.0;
        int pos = -1;
        
        for(int j=0; j<patchY.size(); j++){
          double fit = fitness_function(patchX.get(i),patchY.get(i),patchSize/2,patchX.get(j),patchY.get(j),patchSize/2);
          if(j != i){         
            if(fit > largest){
              largest = fit;
              pos = j;
              
            }
          }
        }
        if(pos != -1){
          move_patch(i,pos,largest);
        }
        
      }
    }
    
    
    unpatchedHoles();
    System.out.println("Patches");
    for(int i=0; i<patchX.size(); i++){
      System.out.println("X: " + patchX.get(i) + " Y: " + patchY.get(i));
    }
    //print_eq();
    
    //print_eq();
    //System.out.println();
  }
  
  
  
  public static void print_eq(){
    for(int h=0; h<holeX.size(); h++){
      //System.out.println(String.format("(x-%f)^2 + (y-%f)^2 = %f^2", holeX.get(h), holeY.get(h), holeSize.get(h)/2));
    }
    for(int i=0; i<patchX.size(); i++){
      //System.out.println(String.format("(x-%f)^2 + (y-%f)^2 = %f^2", patchX.get(i), patchY.get(i), patchSize/2));
    }
    
  }
  
  
  public static void move_patch(int first_patch, int second_patch, double fitness){
    
    
    
    //x to the right
    double current_fitness = fitness;
    
    //just try and move right
    double old_x = patchX.get(first_patch);
    double new_x = patchX.get(first_patch) + 1;
    boolean border;
    
    patchX.set(first_patch, new_x);
    
    double new_fitness = fitness_function(patchX.get(first_patch), patchY.get(first_patch), patchSize/2, patchX.get(second_patch), patchY.get(second_patch),patchSize/2);
    
    if (new_fitness > current_fitness){
      patchX.set(first_patch, old_x);
    }else{
      border = movingBorderCheck(patchX.get(first_patch), patchY.get(first_patch));
      if(border){
        return;
      } else {
        patchX.set(first_patch, old_x);
      }
    }
    
    
    //x to the left
    new_x = patchX.get(first_patch) - 1;
    patchX.set(first_patch,new_x);
    new_fitness = fitness_function(patchX.get(first_patch), patchY.get(first_patch), patchSize/2, patchX.get(second_patch), patchY.get(second_patch),patchSize/2);
    
    
    if (new_fitness > current_fitness){
      //revert to old value
      patchX.set(first_patch, old_x);
      
    }else{
      //we have improved fitness we just move onto the next patch
      border = movingBorderCheck(patchX.get(first_patch), patchY.get(first_patch));
      if(border){
        
        return;
      }else {
        patchX.set(first_patch, old_x);
      }
    }
    
    double old_y = patchY.get(first_patch);
    double new_y = patchY.get(first_patch) + 1;
    
    patchY.set(first_patch,new_y);
    new_fitness = fitness_function(patchX.get(first_patch), patchY.get(first_patch), patchSize/2, patchX.get(second_patch), patchY.get(second_patch),patchSize/2);
    
    if (new_fitness > current_fitness){
      patchY.set(first_patch, old_y);
    }else{
      border = movingBorderCheck(patchX.get(first_patch), patchY.get(first_patch));
      if(border){
        return;
      }else {
        patchY.set(first_patch, old_y);
      }
    }
    
    //move down
    new_y = patchY.get(first_patch) - 1;
    
    patchY.set(first_patch,new_y);
    
    new_fitness = fitness_function(patchX.get(first_patch), patchY.get(first_patch), patchSize/2, patchX.get(second_patch), patchY.get(second_patch),patchSize/2);
    
    if (new_fitness > current_fitness){
      //revert to old value
      patchY.set(first_patch, old_y);
      
    }else{
      //we have improved fitness we just move onto the next patch
      border = movingBorderCheck(patchX.get(first_patch), patchY.get(first_patch));
      if(border){
        return;
      }else {
        patchY.set(first_patch, old_y);
      }
    }
    
  }
  
  
  
  public static void BorderCheck(){
    //checks for the edge cases (outside of sheet)
    for(int i=0; i<patchX.size(); i++){
      
      if(patchX.get(i) <= (patchSize/2)){
        patchX.set(i, patchSize/2);
      }else if(patchX.get(i) >= (sheetX - patchSize/2)){
        patchX.set(i, sheetX - patchSize/2);
      }
      if(patchY.get(i) <= (patchSize/2)){
        patchY.set(i, patchSize/2);
      }else if(patchY.get(i) >= (sheetY - patchSize/2)){
        patchY.set(i, sheetY - patchSize/2);
      }
    }
  }
  
  public static boolean movingBorderCheck(double x, double y){
    if(x < (patchSize/2)){
      return false;
    }else if(x > (sheetX - patchSize/2)){
      return false;
    }
    if(y < (patchSize/2)){
      return false;
    }else if(y > (sheetY - patchSize/2)){
      return false;
    }
    return true;
    
  }
  public static double fitness_function(double x1, double y1, double r1, double x2, double y2, double r2){
    double result;
    result = Math.pow(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2), 0.5);
    //result = patchSize - result;
    /*
     System.out.println(result);
     System.out.println(patchSize - result);
     */
    
    return patchSize-result;
  }
  
  public static void patchIntercepts(){
    
  }
  
  public static void unpatchedHoles(){
    int[][] patchedholes = new int[patchX.size()][holeX.size()];
    for(int x=0; x<5000; x++){
    
    for(int i=0; i<patchX.size(); i++){
      for(int j=0; j<holeX.size(); j++){
        double distance = Math.pow(Math.pow(holeX.get(j) - patchX.get(i), 2) + Math.pow(holeY.get(j) - patchY.get(i), 2), 0.5);
        if(distance < patchSize/2 - holeSize.get(j)/2 - 0.1){
          patchedholes[i][j] = 1;
        } else if(distance > patchSize/2 + holeSize.get(j)/2){
          patchedholes[i][j] = 0;
        } else {
          patchedholes[i][j] = 2;
        }
      }
      
      
    } 
    
    for(int i=0; i<patchX.size(); i++){
      int pos = 0;
      double largest = 0;
      
      //pick holes with intercepts
      for(int j=0; j<holeX.size(); j++){
        if(patchedholes[i][j] == 2){
          double distance = Math.pow(Math.pow(holeX.get(j) - patchX.get(i), 2) + Math.pow(holeY.get(j) - patchY.get(i), 2), 0.5);
          double overlap = patchSize - distance - 0.1;
          if(overlap > largest){
            largest = overlap;
            pos = j;
          }
        }
        
      
      }
      if(largest != 0){
          move_patch_to_hole(patchX.get(i), patchY.get(i), holeX.get(pos), holeY.get(pos), i);
        }
      
    }
    }
    
    for(int i=0; i<patchX.size(); i++){
      for(int j=0; j<holeX.size(); j++){
        //System.out.print(patchedholes[i][j] + " ");
      }
      //System.out.println();
    }
  }
  
  public static void move_patch_to_hole(double pX, double pY, double hX, double hY, int pos){
    //System.out.println(pos);//picks 2 5 times (smol circle) then the pos 4 over and over until it ends.
    double up;
    double down;
    double left;
    double right;
    double highest = 0;
    
    double prev_fitness = fitness_function(pX, pY, 0, hX, hY, 0);
    
    pY += 0.05;
    up = fitness_function(pX, pY, 0, hX, hY, 0);
    highest = up;
    
    pY -= 0.1;
    down = fitness_function(pX, pY, 0, hX, hY, 0);
    if(down > highest){
      highest = down;
    }
    
    pY += 0.05;
    
    pX += 0.05;
    right = fitness_function(pX, pY, 0, hX, hY, 0);
    if(right > highest){
      highest = right;
    }
    
    pX -= 0.1;
    left = fitness_function(pX, pY, 0, hX, hY, 0);
    if(left > highest){
      highest = left;
    }
    
    pX += 0.05;
    
    if(highest == left){
      pX -= 0.05;
      patchX.set(pos, pX);
    } else if(highest == right){
      pX += 0.05;
      patchX.set(pos, pX);
    } else if(highest == up){
      pY += 0.05;
      patchY.set(pos, pY);
    } else if(highest == down){
      pY -= 0.05;
      patchY.set(pos, pY);
    } else {
      //System.out.println("DIE");
    }

  }
}
