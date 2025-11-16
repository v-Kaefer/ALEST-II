import java.util.ArrayList;

/**
 * LabirintoGraph - Converte um labirinto em um grafo e usa CaminhamentoLargura
 * para encontrar o caminho mais curto entre dois pontos (A e B) usando apenas
 * movimentos horizontais e verticais.
 */
public class LabirintoGraph {
    private char[][] maze;
    private int rows;
    private int cols;
    private Graph graph;
    private int startVertex;
    private int endVertex;
    
    /**
     * Construtor - lê o labirinto do arquivo e cria o grafo
     * @param filename nome do arquivo contendo o labirinto
     */
    public LabirintoGraph(String filename) {
        readMaze(filename);
        createGraph();
    }
    
    /**
     * Lê o labirinto do arquivo
     */
    private void readMaze(String filename) {
        In in = new In(filename);
        ArrayList<String> lines = new ArrayList<>();
        
        // Lê todas as linhas
        while (in.hasNextLine()) {
            lines.add(in.readLine());
        }
        
        rows = lines.size();
        cols = lines.get(0).length();
        maze = new char[rows][cols];
        
        // Converte para array 2D
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                maze[i][j] = line.charAt(j);
            }
        }
    }
    
    /**
     * Converte coordenada (row, col) para índice do vértice
     */
    private int coordToVertex(int row, int col) {
        return row * cols + col;
    }
    
    /**
     * Converte índice do vértice para coordenada (row, col)
     */
    private int[] vertexToCoord(int vertex) {
        return new int[] { vertex / cols, vertex % cols };
    }
    
    /**
     * Verifica se uma posição é válida (dentro dos limites e não é parede)
     */
    private boolean isValid(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return maze[row][col] != '#';
    }
    
    /**
     * Cria o grafo a partir do labirinto.
     * Cada célula navegável é um vértice e as arestas conectam células adjacentes
     * apenas horizontal e verticalmente.
     */
    private void createGraph() {
        // Número total de células no labirinto
        int totalCells = rows * cols;
        graph = new Graph(totalCells);
        
        // Direções: cima, baixo, esquerda, direita (apenas horizontal e vertical)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};
        
        // Para cada célula do labirinto
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Verifica se é a posição inicial (B) ou final (A)
                if (maze[i][j] == 'B') {
                    startVertex = coordToVertex(i, j);
                } else if (maze[i][j] == 'A') {
                    endVertex = coordToVertex(i, j);
                }
                
                // Se não é parede, cria arestas para vizinhos válidos
                if (isValid(i, j)) {
                    int currentVertex = coordToVertex(i, j);
                    
                    // Verifica cada direção (horizontal e vertical)
                    for (int d = 0; d < 4; d++) {
                        int newRow = i + dRow[d];
                        int newCol = j + dCol[d];
                        
                        // Se o vizinho é válido, adiciona aresta
                        if (isValid(newRow, newCol)) {
                            int neighborVertex = coordToVertex(newRow, newCol);
                            // Adiciona aresta apenas uma vez (graph.addEdge adiciona em ambas as direções)
                            if (currentVertex < neighborVertex) {
                                graph.addEdge(currentVertex, neighborVertex);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Resolve o labirinto usando CaminhamentoLargura
     * @return ArrayList de coordenadas [row, col] representando o caminho, ou null se não existe
     */
    public ArrayList<int[]> solve() {
        // Usa CaminhamentoLargura para encontrar o caminho
        CaminhamentoLargura bfs = new CaminhamentoLargura(graph, startVertex);
        
        // Verifica se existe caminho
        if (!bfs.hasPathTo(endVertex)) {
            return null;
        }
        
        // Obtém o caminho em termos de vértices
        ArrayList<Integer> vertexPath = bfs.pathTo(endVertex);
        
        // Converte vértices para coordenadas
        ArrayList<int[]> coordPath = new ArrayList<>();
        for (int vertex : vertexPath) {
            coordPath.add(vertexToCoord(vertex));
        }
        
        return coordPath;
    }
    
    /**
     * Retorna a distância do caminho
     */
    public int getPathDistance() {
        CaminhamentoLargura bfs = new CaminhamentoLargura(graph, startVertex);
        return bfs.distTo(endVertex);
    }
    
    /**
     * Imprime o labirinto
     */
    public void printMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Imprime o labirinto com o caminho marcado
     */
    public void printPath(ArrayList<int[]> path) {
        if (path == null) {
            System.out.println("Nenhum caminho encontrado!");
            return;
        }
        
        // Cria uma cópia do labirinto
        char[][] mazeCopy = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeCopy[i][j] = maze[i][j];
            }
        }
        
        // Marca o caminho com '*'
        for (int[] coord : path) {
            int row = coord[0];
            int col = coord[1];
            if (mazeCopy[row][col] != 'A' && mazeCopy[row][col] != 'B') {
                mazeCopy[row][col] = '*';
            }
        }
        
        // Imprime o labirinto com o caminho
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(mazeCopy[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Método main para testes
     */
    public static void main(String[] args) {
        String filename = "Casos-T2/caso1.txt";
        if (args.length > 0) {
            filename = args[0];
        }
        
        System.out.println("=== Resolvedor de Labirinto usando CaminhamentoLargura ===");
        System.out.println("Arquivo: " + filename);
        System.out.println("Movimentos permitidos: apenas horizontal e vertical");
        System.out.println();
        
        LabirintoGraph labirinto = new LabirintoGraph(filename);
        
        ArrayList<int[]> path = labirinto.solve();
        
        if (path != null) {
            System.out.println("Caminho encontrado!");
            System.out.println("Comprimento do caminho: " + labirinto.getPathDistance() + " passos");
            System.out.println();
            System.out.println("Labirinto com caminho marcado (* representa o caminho):");
            System.out.println();
            labirinto.printPath(path);
        } else {
            System.out.println("Nenhum caminho encontrado entre A e B!");
        }
    }
}
