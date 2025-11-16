import java.util.ArrayList;

public class CaminhamentoProfundidade {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public CaminhamentoProfundidade(Graph g, int s){
        this.s = s;
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        dfs(g,s);
    }

    private void dfs (Graph g, int v){
    //    System.out.println("Entrei em: " + v);
        marked[v] = true;
        for (int w : g.adj(v)){
        //    System.out.println("Adjacente: " + w);
            if (!marked[w]){
                edgeTo[w] = v; 
                dfs(g,w);
            }
        }
      //  System.out.println("Terminei: " + v);
    }

    public boolean hasPathTo (int v){
        return marked[v];
    }

    public ArrayList<Integer> pathTo (int v){
        if (!hasPathTo(v)) {
            return null;
        }
        //ja sei que tem caminho, qual é ?
        //lista popula com vertices que compoem o trajeto
        ArrayList<Integer> path = new ArrayList<>();
        //a cada vertice visitado, insere na lista
        while(v != s){ //enquanto nao chega no verticie inicial, monta o caminho
            path.add(0, v);
            v = edgeTo[v];
        }
        path.add(0, s);
        return path;
    }

    public static void main (String[] args){
//        Graph G = new Graph(4);
//        G.addEdge(0, 1);
//        G.addEdge(1, 2);
//        G.addEdge(0, 3);
//        StdOut.println(G);
        
        In in = new In("tinyG.txt");
        Graph g = new Graph(in);
        StdOut.println(g);
        CaminhamentoProfundidade dfs = new CaminhamentoProfundidade(g,0);

        System.out.println("Caminhos existentes:");
        System.out.println("0 : Vértice Inicial");
        for (int v = 1; v < g.V(); v++){
            //se tem caminho para v, ele passa por cada um dos caminhos
            if (dfs.hasPathTo(v)){
                for (int w : dfs.pathTo(v)){
                    System.out.print(w + " ");
                }
                System.out.println();
            }
        }
    }
}
