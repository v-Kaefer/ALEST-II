package T1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;


public class DancaRobo {
    public static void main(String[] args) {

        // Receeeeita que os robôs começam (0 a n-1)
        int n = 7; // Total de Robôs
        int[] receitaInicial = new int[n];
        // Primeira Receita ex:
        for (int i = 0; i < n; i++) {
            receitaInicial[i] = i;
        }


        HashSet<String> filaDeRobos = new HashSet<String>();
        HashMap<HashSet, Integer> receitasUsadas = new HashMap<HashSet, Integer>();
        
        int rodada = 0;
        String receitaAtual;

        while (auxContagemReceita < filaDeRobos.size()) {
            auxContagemReceita = auxContagemReceita++;



            System.out.println(filaDeRobos+"operações: "+auxContagemReceita);
        }

        int[] receitaInicial = {0, 1, 2, 3, 4, 5, 6};
        int[] receita02 = {0, 1, 2, 3, 4, 5, 7};

        filaDeRobos.add(java.util.Arrays.toString(receitaInicial));
        filaDeRobos.add(java.util.Arrays.toString(receitaInicial));
        filaDeRobos.add(java.util.Arrays.toString(receita02));

        System.out.println(java.util.Arrays.toString(receita01)+" em hash:\n"+filaDeRobos);
        System.out.println(filaDeRobos+"operações: "+auxContagemReceita);
        
    }


}