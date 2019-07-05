/** 
  * COSC326 Etude 11 Catalan
  * 
  * Matthew Neil (7388248), Ben Highsted(5536340), Bradley Carter (8277205), Maurice Andrews (8833639)
  * 
  * This program counts the amount of possibilities for Catalans at C(n) for n = 1...35.
  * If n goes over 35 it will overflow.
  * 
  * Each C(n) formula needs all the previous values from C(0) to C(n-1) in order to calculate its own value, so each calculation
  * that is done is put into the array so it can be referenced for each increasing c(n) calculation. Only c(0) = 1 is
  * hard coded into the program.
  * 
  * Then the program prints the values for c(1) to c(35)
  * 
  **/
public class Catalan {  
  public static void main(String[] args) {    
    int n = 35;
    long[] answers = new long[n + 1];
    
    answers[0] = 1;
    
    for(int i = 1; i<=n; i++){
      for(int j = 0; j<i; j++){
        answers[i] += answers[j] * answers[i - 1 - j];
      }
    }
    
    for(int i = 1; i< n + 1; i++){
      System.out.println("C(" + i + "): " + answers[i]);
    }
  }
}