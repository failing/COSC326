import java.util.ArrayList;

public class Arth {

    public ArrayList<Integer> input = new ArrayList<>();
    public String target_expression;
    public Integer target;
    public char type;
    private boolean stop = false;



    public void calculate() {

        //one input case
        if(this.input.size()==1){
            if(!this.input.get(0).equals(this.target)){
                pretty("Impossible");
            }else{
                pretty(this.target.toString());
            }
            return;
        }

        int max = 0;
        int secondMax = 0;
        int min = 0;

        //Should maybe be a long ?
        try{
            max = input.stream().mapToInt(x->x).reduce(1, Math::multiplyExact);
        }catch (ArithmeticException e){
            max = Integer.MAX_VALUE;
        }

        //max case for only two numbers, maybe more cases
        if(this.input.size() == 2){
            max = Math.max(max, input.stream().mapToInt(x->x).reduce(1, Math::addExact));
        }

        if (type == 'L') {

            for(Integer i : input) {
                min += i;
                secondMax += i;
            }

            if (this.input.get(this.input.size()-1) == 1){
                min -= 1;
            }

            secondMax -= input.get(input.size() - 1);
            secondMax *= input.get(input.size() - 1);



            if (target > Math.max(max, secondMax) || target < min) {
                this.target_expression = "Impossible";
                pretty(target_expression);
                return;
            }

        }else if (type != 'N'){
            for (Integer i:input) {
                if (i == 1){
                    min -= 1;
                    max += 1;
                }
                min += i;
            }

            if (this.target < min || this.target > max){
                this.target_expression = "Impossible";
                pretty(target_expression);
                return;
            }
        }


        if (this.type == 'N'){
            normal(1,input.get(0),0,input.get(0).toString());
        }else{
            left(1, input.get(0),input.get(0).toString());
        }

        if(!stop){
            this.target_expression = "Impossible";
        }

        pretty(target_expression);

    }


    public  void normal(int pos, int current_value, int last_value,String current_expression) {

        if(stop){
            return;
        }
        if (pos == input.size()) {
            int calculation;

            if(last_value==0){
                calculation = current_value;
            }
            else {
                calculation = current_value + last_value;
            }
            if (calculation==target){
                this.target_expression = current_expression;
                stop = true;
            }
            return;
        }
        int number = input.get(pos);

        if(current_value>target || last_value>target ){return; }

        if(last_value != 0){
            normal(pos + 1, current_value+last_value, number, current_expression + " + " + input.get(pos));
            normal(pos + 1, current_value, last_value*number,current_expression + " * " + input.get(pos) );

        }
        else{
            normal(pos+1, current_value, number, current_expression + " + " + input.get(pos));
            normal(pos+1,current_value*number, last_value,current_expression + " * " + input.get(pos));
        }
    }
    private void left(int pos, int temp_sum,String current_expression) {

        if(stop)return;

        if(pos == input.size()){
            if(temp_sum == target) {
                stop = true;
                this.target_expression = current_expression;
            }
            return;
        }

        int add = temp_sum + input.get(pos);
        int times = temp_sum * input.get(pos);

        if (temp_sum > target) return;

        left(pos+1,add,current_expression + " + " + input.get(pos));
        left(pos+1,times,current_expression +  " * " + input.get(pos));

    }

    public void pretty(String exp){

        StringBuilder sb = new StringBuilder();

        sb.append(this.type);
        sb.append(" ");
        sb.append(target);
        sb.append(" ");
        sb.append(exp);

        System.out.println(sb.toString());
    }

}
