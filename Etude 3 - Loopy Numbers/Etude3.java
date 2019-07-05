import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Bradley Carter 8277205
 * Maurice Andrews
 */
public class Etude3 {

    private static int MAX_LOOP = 12000000; /* magic number which i know will return all loops that will contain numbers under 9 million, this was found after some testing */
    private static int[] sums_of_divs = new int[MAX_LOOP];
    private static ArrayList<Integer> loopy_numbers = new ArrayList<>();

    public static void main (String[] args) {


        generate_sums(); /* Generate the sum divisors for all numbers 0-MAX_LOOP */

        get_numbers(MAX_LOOP); /* Get loopy number sets from 0-MAX_LOOP */

        print_stats(); /* Print the loopy numbers */

    }

    public static void print_stats(){

        /* ensure only loopy numbers under 9 million are printed */
        ArrayList<Integer> loop_set = loopy_numbers.stream().distinct().filter(i -> i < 9000000).sorted().collect(Collectors.toCollection(ArrayList::new));

        for (Integer i: loop_set) {
            System.out.println(i);
        }

        System.out.println("Loop count: " + loop_set.size());

    }
    public static void get_numbers(int n){

        int max_cycle_count = 28; /* The max cycle of a loopy (sociable) number that is known, only 1 is known of n=28 */

        for(int i = 1;i<n;i++){ /* For each number i=0; to n (MAX_LOOP) */
            int previous_number = i; /*Back up the first number in the for loop */
            boolean stop = false; /* stop check */
            int cycle = 0; /* the current cycle */

            while(cycle < max_cycle_count && !stop){
                cycle++;
                int sums = sums_of_divs[i]; /* get the first sum of divisors from the first number in the loop */

                if (sums > n  || sums < previous_number) break;

                if(sums == previous_number){
                    if (cycle == 1){ /* no loops length > 1 */
                        break;
                    }
                    stop = true;

                    /*
                    ArrayList<Integer> loopy_set = new ArrayList<>();
                    loopy_set.add(previous_number)
                    */

                    loopy_numbers.add(previous_number);
                    for (int k = 1;k<=cycle;k++) { /* cycle through from 1 to current cycle count */
                        int temp = sums_of_divs[previous_number]; /* get the sum of the next number */
                        loopy_numbers.add(temp);
                        /*loopy_set.add(temp)*/
                        previous_number = temp; /* go to the next number */
                    }

                }
                i = sums; /* set i to the original sums */
            }
            i = previous_number; /* set i back to the i  */
        }

    }
    public static void generate_sums(){

        for(int i = 1; i <MAX_LOOP; i++){
            for(int j = i; j < MAX_LOOP; j += i){
                if(j != i){
                    sums_of_divs[j] += i;
                }
            }
        }
    }
}