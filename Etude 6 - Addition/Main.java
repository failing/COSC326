import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* Bradley Carter
 * Maurice Andrews
 */
public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the base: ");
        int base = Integer.parseInt(reader.readLine());
        System.out.print("Number 1: ");
        char[] test = reader.readLine().toCharArray();
        System.out.print("Number 2: ");
        char[] test2 =reader.readLine().toCharArray();
        BigInt Number_1 = new BigInt(base,test);
        BigInt Number_2 = new BigInt(base,test2);

        BigInt Number_3 = Number_1.add_digits(Number_2);

        System.out.println("Result: " + Number_3);

        BigInt div = Number_3.div();

        int r = div.digits.remove(0);

        System.out.println("Remainder: " + r);
        System.out.println("Quotient: " + div);

    }
}
