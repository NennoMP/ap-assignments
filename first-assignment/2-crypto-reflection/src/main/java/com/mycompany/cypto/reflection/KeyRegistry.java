/*
* KeyRegistry
*
* @description: Registry for maintaining a set of classes (crypto algorithms) 
*               and their corresponding key.
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.cypto.reflection;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;


public class KeyRegistry {

    /**
     * Registry containing (sorted) pairs (algorithm -> key).
     */
    private final LinkedHashMap<Class<?>, String> registry;
    
    /* Constructor */
    public KeyRegistry() {
        this.registry = new LinkedHashMap<>();
    }
    
    /**
     * Utility function to insert a new pair (algorithm -> key)
     * 
     * @param c The instance of the class (algorithm)
     * @param key
     */
    public void add(Class<?> c, String key) {
        this.registry.put(c, key);
    }
    
    /**
     * Getter method for retrieving the last key associated to a class.
     * 
     * @param c The class to use as key in the registry.
     * @return The associated key if present, nothing (empty) otherwise
     */
    public Optional<String> get(Class<?> c) {
        if(registry.containsKey(c)) {
            return Optional.of(this.registry.get(c));
        }
        return Optional.empty();
    }
    
    /**
     * Utility function to get all classes (set of keys) in the registry.
     * 
     * @return The set of keys (classes) in the hashmap if not empty, nothing
     *         otherwise
     */
    public Optional<Set<Class<?>>> getClasses() {
        if (!this.registry.isEmpty()) {
            return Optional.of(this.registry.keySet());
        }
        return Optional.empty();
    }
}
