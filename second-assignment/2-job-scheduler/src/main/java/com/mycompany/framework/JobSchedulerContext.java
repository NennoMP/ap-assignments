/*
* JobSchedulerContext
*
* @description: Context object of the JobScheduler.
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.framework;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobSchedulerContext<K,V> {
    
    /* The strategy (scheduling) to use */
    private JobSchedulerStrategy strat;
    
    /**
     * Constructor.
     * 
     * @param strat The strategy to set.
     */
    public JobSchedulerContext(JobSchedulerStrategy<K,V> strat) {
        this.strat = strat;
    }
    
    /**
     * Entry point of the framework.
     */
    public void run() {
        if (strat == null) {
            throw new RuntimeException("Strategy is null!");
        }
        strat.output(collect(compute(strat.emit())));
    }
    
    /**
     * Setter method for the strategy.
     * 
     * @param strat The strategy to set.
     */
    public void setScheduling(JobSchedulerStrategy<K,V> strat) {
        this.strat = strat;
    }
    
    /**
     * Executes the jobs received from 'emit' by invoking 'execute' on them,
     * then concatenates the output of the jobs to create the result.
     * 
     * @frozenSpot
     * 
     * @param jobs The jobs to compute
     * @return A single stream of (key,value) pairs
     */
    public final Stream<Pair<K,V>> compute(Stream<AJob<K,V>> jobs) {
        return jobs.flatMap(x -> x.execute());
    }

    /**
     * Groups all the pairs with the same keys in a single pair, having the 
     * same key and the list of all values.
     * 
     * @frozenspot
     * 
     * @param computed_jobs The pairs created by 'compute'
     * @return Groups of pairs with the same key
     */
    public final Stream<Pair<K, List<V>>> collect(
            Stream<Pair<K,V>> computed_jobs) {
        
        /* Mapping the pairs by key */
        Map<K, List<V>> map = computed_jobs
            .collect(Collectors.groupingBy(
                Pair::getKey, 
                Collectors.mapping(Pair::getValue, Collectors.toList())
            ));
        
        /* To groups of Pair and return. */
        return map
            .entrySet()
            .stream()
            .map(x -> new Pair(x.getKey(), x.getValue()));
    }
    
}
