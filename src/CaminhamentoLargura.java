import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CaminhamentoLargura {
    //atributos : vetores auxiliares
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    //atributo : vertice inicial
    private int s;


    public CaminhamentoLargura(Graph g, int s){
        //inicializar os atributos
        // vertice inicial
        this.s = s;

        //vetores
        marked = new boolean[g.V()];
        edgeTo = new int[g.V()];
        distTo = new int[g.V()];

        //fila
        Queue<Integer> q = new LinkedList<Integer>();

        //algoritmo - caminhamento
        //1. adiciona vertice inicial - fila
        q.add(s);
        //2. marca visitado - vertice inicial
        marked[0] = true;
        //3. Repita enquanto fila nao vazia
        while(q.peek() != null){
            //3.1 remove vertice v da fila
            int v = q.remove();
            //System.out.println(v);
            //3.2 pegar adj(v) e verificar se adiciona ou nao na fila
            for (int w : g.adj(v)){
                //se nao esta marcado, adiciona
                if (!marked[w]){
                    q.add(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    public int distTo (int v){
        return distTo[v];
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
        String fileName = "tinyG.txt";
        In in = new In(fileName);
        Graph g = new Graph(in);
        CaminhamentoLargura bfs = new CaminhamentoLargura(g, 0);

//        if (bfs.hasPathTo(3)){
//            System.out.println("Existe caminho entre: 0 e 3");
//            for (int v : bfs.pathTo(3)){
//                System.out.println(v + " ");
//            }
//        }

        System.out.println("Caminhos existentes:");
        System.out.println("0 : Vértice Inicial");
        for (int v = 1; v < g.V(); v++){
            //se tem caminho para v, ele passa por cada um dos caminhos
            if (bfs.hasPathTo(v)){
                System.out.print(v + ": (" + bfs.distTo[v] + ") ");
                for (int w : bfs.pathTo(v)){
                    System.out.print(w + " ");
                }
                System.out.println();
            }
        }
    }
}
