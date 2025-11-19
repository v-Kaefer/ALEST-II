import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * LabirintoMatriz - Resolve um labirinto usando busca em largura (BFS)
 * diretamente sobre uma matriz, encontrando se existe caminho entre
 * os pontos A (início) e B (fim), usando apenas movimentos horizontais e verticais.
 */
public class LabirintoMatriz {
    private char[][] labirinto;
    private int linhas, colunas;
    private int startLinha, startColunas;
    private int fimLinha, fimColuna;

    private int distancia = -1;         // dist em passos
    private long tempoExecucaoNs = -1L; // tempo da BFS em nanos
    
    // p/ testes
    private boolean contarDistancia = false;
    private boolean guardarCaminho = false;

    // Último caminho encontrado (se guardarCaminho == true)
    private ArrayList<int[]> ultimoCaminho = null;


    /**
     * Construtor - lê o labirinto do arquivo e busca A e B
     * @param filename nome do arquivo contendo o labirinto
     */
    public LabirintoMatriz(String filename) {
        lerLabirinto(filename);
        buscaComecoEFim();
    }


    public void setContarDistancia(boolean contar) {
        this.contarDistancia = contar;
    }

    public void setGuardarCaminho(boolean guardar) {
        this.guardarCaminho = guardar;
    }

    public int getCaminhoDist() {
        return distancia;
    }

    public long getTempoExecucaoNs() {
        return tempoExecucaoNs;
    }

    public ArrayList<int[]> getUltimoCaminho() {
        return ultimoCaminho;
    }


    // Lê o labirinto do arquivo
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
        
        // Converte lista de strings para matriz de caracteres (2D)
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

    // Verifica se é uma posição válida e não é parede
    private boolean ehValido(int linha, int coluna) {
        return linha >= 0 && linha < linhas &&
            coluna >= 0 && coluna < colunas &&
            labirinto[linha][coluna] != '#';
    }

    /**
     * Executa BFS a partir de A
     *
     * Retorna TRUE se alcançar B.
     * Para teste usa flags:
     *  - se contarDistancia == true, 'distancia' guarda o nº mínimo de passos A->B;
     *  - se guardarCaminho == true, 'ultimoCaminho' guarda o caminho completo.
     *
     * Não usa matriz de distâncias (dist[][]) → overhead bem menor.
     */
    public boolean BFS() {
        long inicio = System.nanoTime(); //Timer

        //Substitui Array "pai"
        // Fila de ids codificando (linha, coluna) como um único int: id = linha * colunas + coluna
        byte[][] anterior = new byte[linhas][colunas];
        ArrayDeque<Integer> fila = new ArrayDeque<>();

        //marca o início
        fila.add(startLinha * colunas + startColunas);
        anterior[startLinha][startColunas] = -1; // raiz

        distancia = -1;
        ultimoCaminho = null;

        int[] dLinha  = {-1, 1, 0, 0};
        int[] dColuna = {0, 0, -1, 1};

        int passos = 0;

        while (!fila.isEmpty()) {
            int size = fila.size(); // nós

            
            for (int s = 0; s < size; s++) {
                int id = fila.poll();
                int l = id / colunas;
                int c = id % colunas;

                // Chegou em B
                if (l == fimLinha && c == fimColuna) {
                    //Diminui overhead, modularizando
                    if (contarDistancia) {
                        distancia = passos;
                    } else {
                        distancia = -1;
                    }

                    tempoExecucaoNs = System.nanoTime() - inicio;

                    if (guardarCaminho) {
                        ultimoCaminho = reconstruirCaminho(anterior);
                    }

                    return true;
                }
                

                // Explora vizinhos (cima, baixo, esquerda, direita)
                for (int k = 0; k < 4; k++) {
                    int nl = l +dLinha[k];
                    int nc = c +dColuna[k];



                    // anterior[nl][nc] == 0 -> ainda não foi visitado
                    if (ehValido(nl, nc) && anterior[nl][nc] == 0) {
                        //Lógica de onde veio
                        // k = 0 (subiu)  -> 1
                        // k = 1 (desceu) -> 2
                        // k = 2 (esq)    -> 3
                        // k = 3 (dir)    -> 4
                        anterior[nl][nc] = (byte) (k + 1);
                        fila.add(nl * colunas + nc);
                    }
                }
                
            }

            //terminou uma "camada"
            passos++;
        }

        // Se não encontrou caminho
        distancia = -1;
        tempoExecucaoNs = System.nanoTime() - inicio;
        ultimoCaminho = null;
        return false;
    }

    // Reconstruir o caminho a partir de B -> Isolado para tentar diminuir o overhead
    private ArrayList<int[]> reconstruirCaminho(byte[][] anterior) {
        ArrayList<int[]> caminho = new ArrayList<>();

        int r = fimLinha;
        int c = fimColuna;

        caminho.add(new int[]{r, c});

        // volta até A [Método alterado -> evita overhead]
        while (!(r == startLinha && c == startColunas)) {
            byte d = anterior[r][c];
            if (d == 0) {
                // algo deu errado, não há pai registrado
                break;
            }

            switch (d) {
                case 1: // veio de baixo (subimos: -1 no BFS) => pai está em (r+1, c)
                    r = r + 1;
                    break;
                case 2: // veio de cima (descemos: +1 no BFS) => pai em (r-1, c)
                    r = r - 1;
                    break;
                case 3: // veio da direita (fomos pra esquerda) => pai em (r, c+1)
                    c = c + 1;
                    break;
                case 4: // veio da esquerda (fomos pra direita) => pai em (r, c-1)
                    c = c - 1;
                    break;
                default:
                    // raiz ou erro
                    break;
            }

            caminho.add(new int[]{r, c});
        }

        // Caminho inverso
        Collections.reverse(caminho);
        return caminho;
    }

    // Imprime o labirinto
    public void printLabirinto() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(labirinto[i][j]);
            }
            System.out.println();
        }
    }
    
    // Imprime o labirinto com o caminho marcado
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
    
    // Método main para testes
    public static void main(String[] args) {
        String filename = "../Casos-T2/caso7.txt";
        if (args.length > 0) {
            filename = args[0];
        }
        
        System.out.println("=== Resolve o Labirinto usando Caminhamento Em Largura ===");
        System.out.println("Arquivo: " + filename);
        System.out.println("Movimentos apenas na horizontal e vertical");
        System.out.println();
        
        LabirintoMatriz labirinto = new LabirintoMatriz(filename);
        
        // Define o que guardar
        labirinto.setContarDistancia(true);   // distancia sem matriz extra
        labirinto.setGuardarCaminho(true);   // sem pai[][] → menos memória
        
        Boolean existe = labirinto.BFS();
        //ArrayList<int[]> caminho = labirinto.getUltimoCaminho();

        double tempoMs = labirinto.getTempoExecucaoNs() / 1_000_000.0;

        if (existe) {
            System.out.println("Caminho encontrado!");

            int dist = labirinto.getCaminhoDist();
            if (dist != -1) {
                System.out.println("Comprimento do caminho: " + dist + " passos");
            }

            System.out.printf("Tempo de execução do BFS: %.3f ms%n", tempoMs);
            System.out.println();

            ArrayList<int[]> caminho = labirinto.getUltimoCaminho();
            if (caminho != null) {
                System.out.println("Descomente, para imprimir o mapa com o caminho completo.");
                //System.out.println("Labirinto com caminho marcado (* representa o caminho):\n");
                //labirinto.printaCaminho(caminho);
            } else {
                System.out.println("(Caminho não foi armazenado: guardarCaminho == false)");
            }
        } else {
            System.out.println("Nenhum caminho encontrado entre A e B!");
            System.out.printf("Tempo de execução do BFS: %.3f ms%n", tempoMs);
        }
    }
}
