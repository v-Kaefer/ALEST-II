package T1;
import java.util.Arrays;
import java.util.HashMap;


public class DancaRobo {
    public static void main(String[] args) {
        // para testes de desempenho
        long tempoInicio = System.nanoTime();

        // A receita do exemplo
        int[] receita = {5, 6, 0, 4, 2, 3, 1}; 

        // Receeeeita que os robôs começam (0 a n-1)
        int n = 7; // Total de Robôs
        int[] cicloAtual = new int[n];
        // Primeira Receita ex:
        for (int i = 0; i < n; i++) {
            cicloAtual[i] = i;
        }


        HashMap<String, Long> receitasVistas = new HashMap<String, Long>();
        
        long rodada = 0;
        System.out.println("Rodada " + rodada + ": " + Arrays.toString(cicloAtual));

        while (true) {
            // Converte em String pra usar em chave
            String cicloString = Arrays.toString(cicloAtual);
            if (receitasVistas.containsKey(cicloString)) {
                long rodadaAnterior = receitasVistas.get(cicloString);
                System.out.println("Ciclo encontrado na rodada " + rodadaAnterior + 
                                   " se repete na rodada " + rodada);
                System.out.println("Tamanho do ciclo: " + (rodada - rodadaAnterior));
                break;
            }
            // Armazena a receita atual, associada à rodada
            receitasVistas.put(cicloString, rodada);
            
            // Atualiza o estado com a receita
            cicloAtual = trocaPosicao(cicloAtual, receita);
            rodada++;
            // Para testes de execução, comentar eesse print:
            System.out.println("Rodada " + rodada + ": " + Arrays.toString(cicloAtual));
        }

        // Calcula o tempo de execução, comentar até o último print para remover
        long tempoFim = System.nanoTime();
        long duracaoNano = tempoFim - tempoInicio;
        // Converter para milissegundos para melhor legibilidade
        double duracaoMili = duracaoNano / 1_000_000.0;
        
        System.out.println("Tempo de execução: " + duracaoMili + " milissegundos");
        // Para casos com execução muito longa, você pode querer segundos também
        System.out.println("Tempo de execução: " + (duracaoMili / 1000.0) + " segundos");
    }

    public static int[] trocaPosicao(int[] cicloAtual, int[] receita) {
        int n = cicloAtual.length;
        int[] novoCiclo = new int[n];
        
        // Aplicar a receita geral
        for (int i = 0; i < n; i++) {
            // Na posição i do novo ciclo vai o robô, que tá na posição de receita[i]
            novoCiclo[i] = cicloAtual[receita[i]];
        }
        return novoCiclo;
    }
}
