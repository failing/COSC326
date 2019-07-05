import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 Bradley Carter 8277205
 Maurice Andrews 8833639

 Uses the method of finding combinations shown on https://en.wikipedia.org/wiki/Combination.
 Generates a numerator list of terms and denominator list of terms. E.g N=52, K=5, then numerator list would equal = [52,51,50,49,48],
 denominator list would equal [5,4,3,2,1]. We get the prime factors of every number on the bottom row and then start to cancel terms on the top.
 After we just multiply across to get our answer. If any numbers are left in our denominator list after crossing out terms we check if the denominator number is a
 factor of number after multiplying across on each iteration.

 If the number overflows while multiplying, then -1 will be printed

 **/
public class CountingUp {

    //our numerator and denominator arraylists
    private static ArrayList<Long> top;
    private static ArrayList<Long> bottom;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine() && sc.hasNext()) {



            long n = sc.nextLong();
            long k = sc.nextLong();


            if (n < 0 || k < 0){
                System.out.println("N & K must be positive integers");
                continue;
            }
            
            //get terms for both n and k
            get_terms(n,k);

            //crosses off top terms and bottom terms
            cross_terms();

            //multiples the terms across to get an answer
            System.out.println(multiply_across());

        }
    }

    public static void get_terms(long n,long k){

        ArrayList<Long> top_terms = new ArrayList<>();
        ArrayList<Long> bottom_terms = new ArrayList<>();


        long difference;
        long difference2;

        /*
            if k < n-k we set the difference to n-k for our numerator list,
            and difference2 to k for our denominator terms, else difference2 will be negative and will result in us returning 1.
         */
        if (k < n-k){
            difference = n-k;
            difference2 = k;
        }else{
            difference = k;
            difference2 = n-k;
        }

        //gets the numerator numbers from n to difference inclusive
        for(long i = n;i > difference;i--){
            top_terms.add(i);
        }

        //gets the list of numbers in denominator list from difference2 to 1 inclusive
        for(long i = difference2; i > 0;i--){
            bottom_terms.add(i);
        }

        //sets numerator and denominator
        top = top_terms;
        //gets all prime factors of the bottom terms and replaces the entire bottom row with these factors
        bottom = factors(bottom_terms);
    }

    /**
     * Cross off terms in the denominator list by finding factors of the numerator list from the denominator
     * E.g
     * Our initial numerator list -> [52, 51, 50, 49, 48]
     * our initial denominator -> [5, 4, 3, 2, 1]
     * After factoring out -> [5, 2, 2, 3, 2]
     *
     * our ending numerator list -> [13, 17, 5, 49, 48]
     * our ending denominator list -> [1, 1, 1, 1, 1]
     *
     */
    public static void cross_terms() {
        //for each number in the numerator list
        for (int i = 0; i < top.size(); i++) {
            //for each
            for (int j = 0; j < bottom.size(); j++) {
                if (top.get(i) % bottom.get(j) == 0) {
                    top.set(i, top.get(i) / bottom.get(j));
                    bottom.set(j, 1L);
                }
            }
        }


    }

    /***
     *
     * factors out the denominator
     * @param bottom
     * @return the prime factors of all the bottom terms
     */
    public static ArrayList<Long> factors(ArrayList<Long> bottom){

        ArrayList<Long> primes = new ArrayList<>();

        for(Long b: bottom){
            for (long i = 2; i <= b; i++) {
                while (b % i == 0) {
                    primes.add(i);
                    b /= i;
                }
            }
        }

        return primes;
    }

    /**
     * Multiplies every number across in the numerator list.
     * Sometimes the denominator list hasn't been fully cancelled out so we,loop through the remaining numbers to
     * see if they are a factor of our current number after we multiple across at each loop.
     */
    public static long multiply_across(){

        long final_number = 1;

        try{
            for (Long i: top) {
                //We multiple across with the current number and i.
                //For each number in the denominator
                final_number = Math.multiplyExact(final_number ,i );
                for (int j = 0; j < bottom.size(); j++) {
                    if (bottom.get(j) != 1) {
                        if (final_number % bottom.get(j) == 0){
                            final_number = final_number / bottom.remove(j);
                        }
                    }
                }
            }
        }catch (ArithmeticException e){
            return -1;
        }

        return final_number;
    }
}
