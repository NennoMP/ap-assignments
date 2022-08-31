/*
* JobSchedulerStrategy
*
* @description: Strategy object implementing the JobScheduler concrete actions.
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.framework;

import java.util.List;
import java.util.stream.Stream;


abstract public class JobSchedulerStrategy<K,V> {
    
    /**
     * Generates a stream of jobs.
     * 
     * @hotspot
     * 
     * @return A stream of jobs
     */
    protected abstract Stream<AJob<K,V>> emit();
    
    
    /**
     * Prints the result of 'collect' (@forzenspot).
     * 
     * @hotspot
     * 
     * @param groups The groups of pairs from 'collect'
     */
    protected abstract void output(Stream<Pair<K, List<V>>> groups);
}
