/*
* TestAlgs
*
* @description: Test all encryption algorithms (.class files) contained in a 
*               directory passed as a command line argument that respect the
                following properties:
                  - a public constructor with a single String parameter
                  - a method starting with 'enc' with a single String parameter
                  - a method starting with 'dec' with a single String parameter
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.cypto.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TestAlgs {
    
    /**
     * 
     * @param args First and only argument must be the root path of 'crypto'
     *             directory
     */
    public static void main(String[] args) {
        String path;
        KeyRegistry registry;
        List<String> secrets;
        
        if (args.length != 1) {
            System.out.println(
                    "Missing args, insert parent directory of crypto!");
            return;
        }

        path = args[0];
        try {
            registry = loadKeys(path); /* Load list of keys in a KeyRegistry */
            secrets = loadSecrets(path); /* Load list of secrets */
            checkAlgorithms(path, registry, secrets); /* Test each algorithm */  
        } catch (IOException | ClassNotFoundException | InstantiationException | 
                IllegalAccessException | InvocationTargetException e) {
            System.err.println("[ERROR]: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check a set of classes/algorithms to find which ones are encryption 
     * algorithms, they must have the following properties:
     *   1 - a public constructor with a single String parameter;
     *   2 - a method starting with 'enc' with a single String parameter;
     *   3 - a method starting with 'dec' with a single String parameter.
     * 
     * @param path The root path of the directory containing the classes
     * @param registry The KeyRegistry containing the algorithms and their keys
     * @param secrets The list of secrets
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    protected static void checkAlgorithms(String path, 
            KeyRegistry registry, List<String> secrets) 
            throws ClassNotFoundException, InstantiationException, 
                   IllegalAccessException, IllegalArgumentException, 
                   InvocationTargetException {
        
        for(Class<?> c: registry.getClasses().get()) {
            
            /* Check 1 */
            if (!hasPublicConstructor(c)) {
                System.out.printf("]%s] - No public constructor\n", 
                        c.getSimpleName());
                continue;
            }
            
            /* Check 2 and 3 */
            Optional<Method> enc = getMethodStartingWith(c, "enc");
            Optional<Method> dec = getMethodStartingWith(c, "dec");
            if (enc.isEmpty() || dec.isEmpty()) {
                System.out.printf("[%s] - Enc/Dec methods not found\n", 
                        c.getSimpleName());
                continue;
            }
        
            /* Run encryption/decryption tests if all checks are passed */
            testEncDec(c, registry, enc.get(), dec.get(), secrets);
        }    
    }
    
    /**
     * Check if a class has a single String parameter.
     * 
     * @param params Array with parameter types of a method object
     * @return True or False
     */
    protected static boolean hasOneStringParameter(Class<?>[] params) {
        return params.length == 1 && params[0].equals(String.class);
    }
    
    /**
     * Check if a class has a public contrusctor with a single String parameter.
     * 
     * @param c The class to be checked
     * @return True or False
     */
    protected static boolean hasPublicConstructor(Class<?> c) {
        
        for (Constructor constructor: c.getConstructors()) {
            /* Get constructor parameters */
            Class[] params = constructor.getParameterTypes();
            
            /* If public and only one String parameter */
            if (Modifier.isPublic(constructor.getModifiers())
                    && hasOneStringParameter(params)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return a method starting with a specific pattern and with a single String
     * parameter if present.
     * 
     * @param c The class to be checked
     * @param pattern The pattern to check for in the method name
     * @return The method if it exists, empty Optional otherwise
     */
    protected static Optional<Method> getMethodStartingWith(Class<?> c, 
                                                        String pattern) {
        
        for (Method m: c.getMethods()) {
            /* Get method parameters */
            Class[] params = m.getParameterTypes();
            
            /* 
             * If method's name starts with pattern and only one String 
             * parameter 
            */
            if (m.getName().startsWith(pattern) 
                    && hasOneStringParameter(params)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Get all classes (.class) in a specific folder.
     * 
     * @param path The folder to check for classes
     * @return List of class objects
     */
    protected static List<Class<?>> getClasses(String path) throws ClassNotFoundException {
        File folder = new File(path.concat("/crypto/algos"));
        File[] listOfFiles = folder.listFiles();
        
        List<Class<?>> listOfClasses = new ArrayList<>();
        for(File f: listOfFiles) {
            
            // only .class files
            String name = f.getName();
            name = "crypto/algos".concat(name);
            if (name.endsWith(".class")) {
                Class<?> c = Class.forName(name);
                listOfClasses.add(c);
            }
        }
        
        return listOfClasses;
    }
    
    /**
     * Runs encryption/decryption test on an encryption algorithm.
     * 
     * @param c The class/algorithm  to test
     * @param registry The KeyRegistry containing the classes and their keys
     * @param enc The encryption method of the algorithm
     * @param dec The decryption method of the algorithm
     * @param secrets The list of secrets
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     * @throws java.lang.InstantiationException 
     */
    protected static void testEncDec(Class<?> c, KeyRegistry registry, 
                                    Method enc, Method dec, List<String> secrets) 
            throws IllegalAccessException, IllegalArgumentException, 
                    InvocationTargetException, InstantiationException {
        
        boolean testPassed = true;
        String className = c.getSimpleName();
        
        /* Get corresponding key of the class/algorithm */
        Optional<String> key = registry.get(c);
        if (key.isEmpty()) {
            System.out.printf("[%s]: no key associated\n", className);
            return;
        }
        
        /* Create instance of the algorithms using its key */
        Object algo = c.getConstructors()[0].newInstance(key.get());
        
        /* Call encryption and decryption methods for each secret */
        for(String wrd: secrets) {
            /* Word after encryption */
            String encwrd = (String) enc.invoke(algo, wrd);
            /* Word after decryption */
            String decwrd = (String) dec.invoke(algo, encwrd);
            
            /* 
            * decword and encword must be equal, except for possible padding
            * characters (#)
            */
            if (!decwrd.startsWith((wrd))) {
                testPassed = false;
                System.out.printf("[%s] - [KO]: %s -> %s -> %s\n", 
                        className, wrd, encwrd, decwrd);
            }
        }
        
        if (testPassed) {
            System.out.printf("[%s] - [OK]\n", className);
        }
    }
    
    /**
     * Read all secrets (lines) from 'secrets.list' file.
     * 
     * @param path The file's path
     * @return A list containing each line
     * @throws IOException 
     */
    protected static List<String> loadSecrets(String path) throws IOException {
        return Files.readAllLines(Paths.get(path.concat("/crypto/secret.list")));
    }
    
    /**
     * Load and store all pairs (algorithm -> key) from 'keys.list' file
     * into a KeyRegistry.
     * 
     * @param path The file's root path
     * @return A KeyRegistry with all pairs (algorithm -> keys) found
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    protected static KeyRegistry loadKeys(String path) 
            throws IOException, ClassNotFoundException {
        
        String fileName = path.concat("/crypto/keys.list");
        KeyRegistry registry = new KeyRegistry();
        
        /* Read lines from 'keys.list' file and tokenize them */
        List<String[]> lines = Files.lines(Paths.get(fileName))
                .map(x -> x.split(" "))
                .collect(Collectors.toList());
        
        /* Create class loader */
        File f = new File(path);
        URL[] urls = new URL[]{f.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        
        for(String[] line: lines) {
            Class<?> c = loader.loadClass(line[0]);
            String key = line[1];
            registry.add(c, key);
        }
        
        return registry;
    }
    
}

