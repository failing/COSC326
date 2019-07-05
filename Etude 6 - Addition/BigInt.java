import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/* Bradley Carter
 * Maurice Andrews
 */
public class BigInt {
    public ArrayList<Integer> digits = new ArrayList<>();
    private int base;

    public BigInt(int b,char[] nums){
        this.base = b;
        for(int i =0;i<nums.length;i++){
            int temp = Character.getNumericValue(nums[i]);
            if (temp>=base){
                System.out.println("Not a valid number");
                return;
            }
            this.digits.add(temp);
        }
    }
    public BigInt(int b){
        this.base = b;
    }

    public BigInt add_digits(BigInt b) {
        BigInt new_Number = new BigInt(this.base);
        int max = Math.max(this.digits.size(), b.digits.size());
        int difference = max - Math.min(this.digits.size(), b.digits.size());

        /* Make sure the arraylists are the same length with leading zeros */
        if (this.digits.size()!=max || b.digits.size()!=max){
            for(int i =0;i<difference;i++){
                if(this.digits.size()==max){
                    b.digits.add(0,0);
                }else{
                    this.digits.add(0,0);
                }
            }
        }

        /* adding */
        int carry = 0;
        for (int i = max-1; i >= 0; i--) {
            /* add the two digits up and the carry*/
            int t = this.digits.get(i) + b.digits.get(i) + carry;
            /* if the digit is greater than the base, the digit is now digit-base, set carry to one*/
            if (t > this.base) {
                int temp = t - this.base;
                new_Number.digits.add(temp);
                carry = 1;
            } else if (t == this.base) { /* digit = base, set digit to 0 set carry to 1 */
                carry = 1;
                new_Number.digits.add(0);
            } else {
                /* if digits added aren't => than the base then just add it to our new number, set carry to 0 */
                new_Number.digits.add(t);
                carry = 0;
            }
        }

        if(carry!=0){
            new_Number.digits.add(max,carry);
        }

        Collections.reverse(new_Number.digits);

        return new_Number;

    }

    public BigInt div() {
        int r = 0;

        BigInt q = new BigInt(this.base);


        for (int i = 0; i < this.digits.size(); i++) {
            q.digits.add(this.digits.get(i)/2);
            /* if number isn't even */
            if(this.digits.get(i)%2 == 1){
                /* if digit isn't the last digit */
                if (i != this.digits.size() - 1){
                    int temp = this.digits.get(i+1) + this.base;
                    this.digits.set(i+1, temp);
                }else{
                    r = 1;
                }
            }
        }

        /* removing leading zeros. https://stackoverflow.com/a/36059226 */
        int i = 0;
        Iterator<Integer> it = q.digits.iterator();
        while (it.hasNext() && it.next() == 0) {
            ++i;
        }
        q.digits.subList(0, i).clear();

        q.digits.add(0, r);

        return q;
    }

    @Override
    public String toString() {
        StringBuilder j = new StringBuilder();

        for (Integer i: digits) {
            j.append(i);
        }
        return j.toString();
    }


}
