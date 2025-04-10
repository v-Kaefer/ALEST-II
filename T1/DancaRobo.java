package T1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class DancaRobo {
    public static void main(String[] args) {
        // A receita do exemplo
        int[] receita = {5, 6, 0, 4, 2, 3, 1}; 

        // Receeeeita que os robôs começam (0 a n-1)
        int n = 7; // Total de Robôs
        int[] cicloAtual = new int[n];
        // Primeira Receita ex:
        for (int i = 0; i < n; i++) {
            cicloAtual[i] = i;
        }


        HashMap<String, Integer> receitasVistas = new HashMap<String, Integer>();
        
        int rodada = 0;
        String receitaAtual;
        System.out.println("Rodada " + rodada + ": " + Arrays.toString(cicloAtual));

        while (true) {
            // Converte em String pra usar em chave
            String cicloString = Arrays.toString(cicloAtual);
            if (receitasVistas.containsKey(cicloString)) {
                int rodadaAnterior = receitasVistas.get(cicloString);
                System.out.println("Ciclo encontrado na rodada " + rodadaAnterior + 
                                   " se repete na rodada " + rodada);
                System.out.println("Tamanho do ciclo: " + (rodada - rodadaAnterior));
                break;
            }

            // Armazenar a configuração atual, associada à rodada
            receitasVistas.put(cicloString, rodada);
            
            // Atualizar o estado aplicando a receita de forma modular
            cicloAtual = trocaPosicao(cicloAtual, receita);
            rodada++;
            System.out.println("Rodada " + rodada + ": " + Arrays.toString(cicloAtual));
        }

    }


    public static int[] trocaPosicao(int[] cicloAtual) {
        int n = cicloAtual.length;
        int[] novoEstado = new int[n];
        
        // Garantir que haja elementos suficientes para os movimentos definidos.
        if (n < 6) {
            throw new IllegalArgumentException("É necessário ter pelo menos 6 robôs para aplicar os movimentos.");
        }
        
        // Movimentos fixos:
        novoEstado[0] = cicloAtual[6]; // 1)Quinta posição vai pra posição 0
        novoEstado[1] = cicloAtual[5]; // 2)Sexta posição vai pra posição 1
        novoEstado[2] = cicloAtual[0]; // 3)Posição zero vai pra posição 2
        // Extras não declarados
        novoEstado[3] = cicloAtual[4];
        novoEstado[4] = cicloAtual[2];
        novoEstado[5] = cicloAtual[3];
        novoEstado[6] = cicloAtual[1];
        
        
        
        return novoEstado;
    }
}
