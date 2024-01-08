package utils;

/**
 *
 * @author raffy
 */


public class SolutionStatistics {
    private long sol_nodes = 0;
    private long explored_nodes = 0; 
    private long generated_nodes = 0;
    

    public SolutionStatistics(){}
    /**
     * Memorizza il numero di nodi soluzione trovati durante
     * l'esecuzione degli algortimi di risoluzione e il numero totale
     * di nodi esplorati.
     * @param explored_nodes: numero dei nodi esplorati.
     * @param sol_nodes: numero dei nodi soluzione esplorati.
     */

    public void recordSolutionStatistics(long explored_nodes, long sol_nodes, long generated_nodes){

        this.explored_nodes = explored_nodes;
        this.sol_nodes = sol_nodes;
        this.generated_nodes = generated_nodes;
    }

    public long getGeneratedNodes(){
        return this.generated_nodes;
    }
    public long getSolNodes(){
    

        return this.sol_nodes;
    }
    public long getExploredNodes(){
        return this.explored_nodes;
    }
}
