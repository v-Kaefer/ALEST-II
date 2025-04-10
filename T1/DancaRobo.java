package T1;
import java.util.HashSet; // Import the HashSet class


public class DancaRobo {
    public static void main(String[] args) {
        // Primeira Receita ex:
        HashSet<String> filaDeRobos = new HashSet<String>();
        

        int[] receita01 = {0, 1, 2, 3, 4, 5, 6};

        filaDeRobos.add(java.util.Arrays.toString(receita01));

        System.out.println(java.util.Arrays.toString(receita01)+" em hash:\n"+filaDeRobos);
        
    }

}