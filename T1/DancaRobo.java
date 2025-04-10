package T1;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class DancaRobo {
    public static void main(String[] args) {
        int totalRobos = 7; 
        int[] estadoAtual = new int[totalRobos];
        for (int i = 0; i < totalRobos; i++) {
            estadoAtual[i] = i;
        }
        
        // HashMap para armazenar configurações já vistas (para identificar ciclos)
        HashMap<String, Integer> configuracoesVistas = new HashMap<>();
        
        int rodada = 0;
        System.out.println("Rodada " + rodada + ": " + Arrays.toString(estadoAtual));
        
        while (true) {
            String estadoString = Arrays.toString(estadoAtual);
            if (configuracoesVistas.containsKey(estadoString)) {
                int rodadaAnterior = configuracoesVistas.get(estadoString);
                System.out.println("Ciclo encontrado! A configuração da rodada " +
                                   rodadaAnterior + " se repete na rodada " + rodada);
                System.out.println("Tamanho do ciclo: " + (rodada - rodadaAnterior));
                break;
            }
            configuracoesVistas.put(estadoString, rodada);
            
            // O próximo estado é calculado de acordo com a regra definida:
            // - newState[0] = estadoAtual[4] (quinta posição)
            // - newState[1] = estadoAtual[6] (sexta posição)
            // - newState[2] = estadoAtual[0] (posição zero)
            // Os demais elementos são obtidos, em ordem FIFO, dos números que não participam dos movimentos.
            estadoAtual = proximoEstado(estadoAtual);
            rodada++;
            System.out.println("Rodada " + rodada + ": " + Arrays.toString(estadoAtual));
        }
    }
    
    /**
     * Calcula o próximo estado dos robôs segundo as seguintes regras:
     * 1. O elemento da quinta posição (índice 4) do estadoAtual é colocado na posição 0 do novo estado.
     * 2. O elemento da sexta posição (índice 6) do estadoAtual é colocado na posição 1.
     * 3. O elemento da posição zero (índice 0) do estadoAtual é colocado na posição 2.
     * 4. Os demais elementos (dos índices que não são 0, 4 ou 6) são adicionados a uma fila FIFO,
     *    e, em seguida, preenchendo as posições restantes do novo estado (a partir da posição 3) com os itens
     *    dessa fila, preservando sua ordem.
     *
     * @param estadoAtual Vetor representando o estado atual.
     * @return Vetor com o novo estado após a transformação.
     */
    public static int[] proximoEstado(int[] estadoAtual) {
        int n = estadoAtual.length;
        int[] novoEstado = new int[n];
        
        // Garantir que haja elementos suficientes para os movimentos definidos.
        if (n < 5) {
            throw new IllegalArgumentException("É necessário ter pelo menos 5 robôs para aplicar os movimentos.");
        }
        
        // Movimentos fixos:
        novoEstado[0] = estadoAtual[5]; // Quinta posição (índice 4) vai para posição 0
        novoEstado[1] = estadoAtual[6]; // Sexta posição (índice 6) vai para posição 1
        novoEstado[2] = estadoAtual[0]; // Posição zero (índice 0) vai para posição 2
        
        // Cria uma fila FIFO para os demais elementos que não participam dos movimentos.
        Queue<Integer> fila = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            // Ignora os elementos que já foram "movidos"
            if (i != 0 && i != 5 && i != 6) {
                fila.add(estadoAtual[i]);
            }
        }
        
        // Preenche as demais posições do novo estado (a partir da posição 3)
        int pos = 3;
        while (!fila.isEmpty() && pos < n) {
            novoEstado[pos++] = fila.poll();
        }
        
        return novoEstado;
    }
}
