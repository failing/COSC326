import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        get_input();
    }

    public static void get_input(){

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String[] numbers = line.split(" ");

            if (!sc.hasNextLine()){
                System.out.println("Invalid Input");
            }

            line = sc.nextLine();

            Arth a = new Arth();
            a.type = line.split(" ")[1].toCharArray()[0];
            a.target = Integer.valueOf(line.split(" ")[0]);

            for(int i =0;i<numbers.length;i++){
                if (Integer.valueOf(numbers[i]) < 1){
                    System.out.println("Invalid Input");
                }
                a.input.add(Integer.valueOf(numbers[i]));
            }

            if(a.type != 'L' && a.type != 'N'){
                System.out.println("Type must either be N or L");
            }else{
                a.calculate();
            }
        }


    }
}

