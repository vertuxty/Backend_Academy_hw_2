package backend.academy.generators;

/**
 * Class that define disjoint-set-union structure.
 * Using in maze's generator algorithms to define
 * if path between to cell exist
 * */
public class DisjointSetUnion {

    private final int[] parent;
    private final int[] rank;

    /**
     * Create instance of DSU.
     * @param size maz count of elements in DSU
     * */
    public DisjointSetUnion(int size) { // height * width
        this.parent = new int[size + 1];
        this.rank = new int[size + 1];
        for (int i = 0; i < size + 1; i++) {
            parent[i] = i;
        }
    }

    /**
     * Gets representative of given element.
     * @param v element in DSU
     * @return representative of it's element
     * */
    public int getRep(int v) {
        if (v == parent[v]) {
            return v;
        }
        int rep = getRep(parent[v]);
        parent[v] = rep;
        return rep;
    }

    /**
     * Union two sets.
     * @param x element in first set
     * @param y element in second set
     * */
    @SuppressWarnings("ReturnCount")
    public void union(int x, int y) {
        if (x == y) {
            return;
        }
        int repX = getRep(x);
        int repY = getRep(y);
        if (repY == repX) {
            return;
        }
        if (rank[repX] == rank[repY]) {
            rank[repX]++;
        }
        if (rank[repX] > rank[repY]) {
            parent[repY] = repX;
        } else {
            parent[repX] = repY;
        }
    }
}
