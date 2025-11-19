import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * LabirintoMatriz - Converte um labirinto em um grafo e usa CaminhamentoLargura
 * para encontrar o caminho mais curto entre dois pontos (A e B) usando apenas
 * movimentos horizontais e verticais.
 */
public class LabirintoMatriz {
    private char[][] labirinto;
    private int linhas, colunas;
    private int startLinha, startColunas;
    private int fimLinha, fimColuna;
    
    /**
     * Construtor - lê o labirinto do arquivo e busca A e B
     * @param filename nome do arquivo contendo o labirinto
     */
    public LabirintoMatriz(String filename) {
        lerLabirinto(filename);
        buscaComecoEFim();
    }
    
    /**
     * Lê o labirinto do arquivo
     */
    private void lerLabirinto(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Lê todas as linhas
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        linhas = lines.size();
        colunas = lines.get(0).length();
        labirinto = new char[linhas][colunas];
        
        // Converte para array 2D
        for (int i = 0; i < linhas; i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                labirinto[i][j] = line.charAt(j);
            }
        }
    }


    // Scan para encontrar A & B
    private void buscaComecoEFim() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (labirinto[i][j] == 'A') {
                    startLinha = i;
                    startColunas = j;
                } else if (labirinto[i][j] == 'B') {
                    fimLinha = i;
                    fimColuna = j;
                }
            }
        }
    }

    /**
     * Verifica se uma posição é válida (dentro dos limites e não é parede)
     */
    private boolean ehValido(int linha, int coluna) {
        return linha >= 0 && linha < linhas &&
            coluna >= 0 && coluna < colunas &&
            labirinto[linha][coluna] != '#';
    }

    /**
     * Resolve o labirinto usando CaminhamentoLargura
     * @return ArrayList de coordenadas [row, col] representando o caminho, ou null se não existe
     */
    public ArrayList<int[]> solve() {
        boolean[][] visitado = new boolean[linhas][colunas];
        int[][] pai = new int[linhas][colunas]; // armazena o "pai" como um único int (row * colunas + col)

        // inicializa paies com -1
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                pai[i][j] = -1;
            }
        }

        // fila de ints codificando (row, col)
        java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();

        visitado[startLinha][startColunas] = true;
        queue.add(startLinha * colunas + startColunas);

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        boolean found = false;

        while (!queue.isEmpty()) {
            int id = queue.poll();
            int l = id / colunas;
            int c = id % colunas;

            if (l == fimLinha && c == fimColuna) {
                found = true;
                break;
            }

            for (int k = 0; k < 4; k++) {
                int nl = l + dr[k];
                int nc = c + dc[k];

                if (ehValido(nl, nc) && !visitado[nl][nc]) {
                    visitado[nl][nc] = true;
                    pai[nl][nc] = id; // veio de (r,c)
                    queue.add(nl * colunas + nc);
                }
            }
        }

        if (!found) {
            return null; // sem caminho
        }

        // Reconstruir caminho a partir de B
        ArrayList<int[]> caminho = new ArrayList<>();
        int curId = fimLinha * colunas + fimColuna;

        while (curId != -1) {
            int r = curId / colunas;
            int c = curId % colunas;
            caminho.add(new int[]{r, c});
            curId = pai[r][c];
        }

        // está de B até A, então inverte
        java.util.Collections.reverse(caminho);
        return caminho;
    }
    
    /**
     * Retorna a distância do caminho
     */
    public int getCaminhoDist(ArrayList<int[]> caminho) {
        if (caminho == null) {
            return -1;
        }
        return caminho.size() - 1;
    }
    
    /**
     * Imprime o labirinto
     */
    public void printLabirinto() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(labirinto[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Imprime o labirinto com o caminho marcado
     */
    public void printaCaminho(ArrayList<int[]> caminho) {
        if (caminho == null) {
            System.out.println("Nenhum caminho encontrado!");
            return;
        }
        
        // Cria uma cópia do labirinto
        char[][] labirintoCopy = new char[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                labirintoCopy[i][j] = labirinto[i][j];
            }
        }
        
        // Marca o caminho com '*'
        for (int[] coord : caminho) {
            int linha = coord[0];
            int coluna = coord[1];
            if (labirintoCopy[linha][coluna] != 'A' && labirintoCopy[linha][coluna] != 'B') {
                labirintoCopy[linha][coluna] = '*';
            }
        }
        
        // Imprime o labirinto com o caminho
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(labirintoCopy[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Método main para testes
     */
    public static void main(String[] args) {
        String filename = "../Casos-T2/caso7.txt";
        if (args.length > 0) {
            filename = args[0];
        }
        
        System.out.println("=== Resolvedor de Labirinto usando CaminhamentoLargura ===");
        System.out.println("Arquivo: " + filename);
        System.out.println("Movimentos permitidos: apenas horizontal e vertical");
        System.out.println();
        
        LabirintoMatriz labirinto = new LabirintoMatriz(filename);
        
        ArrayList<int[]> caminho = labirinto.solve();
        
        if (caminho != null) {
            System.out.println("Caminho encontrado!");
            System.out.println("Comprimento do caminho: " + labirinto.getCaminhoDist(caminho) + " passos");
            System.out.println();
            System.out.println("Labirinto com caminho marcado (* representa o caminho):");
            System.out.println();
            // Print de todo o labirinto - desativar para testes >= 5
            //labirinto.printaCaminho(caminho);
        } else {
            System.out.println("Nenhum caminho encontrado entre A e B!");
        }
    }
}
