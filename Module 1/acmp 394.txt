import java.io.PrintStream;
import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);
    public static PrintStream out = System.out;

    public static int nod(int a, int b) {
        if (b == 0) {
            return a;
        }
        return nod(b, a % b);
    }

    public static void main(String[] args) {
        int a = sc.nextInt(), b = sc.nextInt();
        out.println(a / nod(a, b));
    }
}