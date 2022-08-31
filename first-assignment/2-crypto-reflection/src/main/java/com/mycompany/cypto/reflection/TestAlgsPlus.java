/*
* TestAlgsPlus
*
* @description: Test all encryption algorithms (.class files) contained in a 
*               directory passed as a command line argument that respect the
                following properties:
                  - a public constructor with a single String parameter
                  - a method starting with 'enc' OR exactly one annotated with
                     @Encrypt, wiaht a single String parameter 
                  - a method starting with 'dec' OR extacly one annotated with 
                    @Decrypt, with a single String parameter 
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.cypto.reflection;

import static com.mycompany.cypto.reflection.TestAlgs.getMethodStartingWith;
import static com.mycompany.cypto.reflection.TestAlgs.hasPublicConstructor;
import static com.mycompany.cypto.reflection.TestAlgs.loadKeys;
import static com.mycompany.cypto.reflection.TestAlgs.loadSecrets;
import static com.mycompany.cypto.reflection.TestAlgs.testEncDec;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;


public class TestAlgsPlus extends TestAlgs {
    
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
     *  1 - a public constructor with a single String parameter;
     *  2 - exaclty one method starting with 'enc' OR annotated with 
     *      @Encrypt, wiaht a single String parameter; 
     *  3 - exaclty one method starting with 'dec' OR annotated with 
     *      @Decrypt, with a single String parameter.
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
                System.out.printf("[%s] - No public constructor\n", 
                        c.getSimpleName());
                continue;
            }
            
            /* Check 2 and 3 */
            Optional<Method> enc = getMethodStartingWith(c, "enc");
            Optional<Method> dec = getMethodStartingWith(c, "dec");
            Optional<Method> encAnn = getMethodWithAnnotation(c, Encrypt.class);
            Optional<Method> decAnn = getMethodWithAnnotation(c, Decrypt.class);
            if ((enc.isEmpty() || dec.isEmpty()) 
                    && (encAnn.isEmpty() || decAnn.isEmpty())) {
                
                System.out.printf("[%s] - Enc/Dec methods not found\n", 
                    c.getSimpleName());
                continue;
            }
            
            
            Method encm = enc.isPresent() ? enc.get() : encAnn.get();
            Method decm = dec.isPresent() ? dec.get() : decAnn.get();
            
            /* Run encryption/decryption tests if all checks are passed */
            testEncDec(c, registry, encm, decm, secrets);
        }    
    }
    
    /**
     * Return a method with annotation 'ann' if EXACTLY ONE exists, 
     * empty optional otherwise.
     * 
     * @param c The class to search for the annotated method
     * @param ann The annotation to look for
     * @return The annotated method if only one exists, 
     *         empty optional otherwise
     */
    public static Optional<Method> getMethodWithAnnotation(Class<?> c, 
            Class<?> ann) {
        
        int cont = 0; /* number of methods with annotation 'ann' */ 
        Optional<Method> result = Optional.empty();
        
        
        Method[] methods = c.getMethods();
        int len = methods.length;
        int i = 0;

        while(i < len && cont <= 1) {
            Class[] params = methods[i].getParameterTypes();
            Annotation[] annotations = methods[i].getAnnotations();
            
            /* Check if annotation is present and only one String parameter */
            if(annotations.length == 1 
                    && annotations[0].annotationType().getSimpleName()
                            .equals(ann.getSimpleName()) 
                    && params.length == 1 && hasOneStringParameter(params)){
                result = Optional.of(methods[i]);
                cont++;
            }
            
            i++;
        }
        
        /* More than one method with annotation 'ann' found */
        if (cont > 1) {
            System.out.printf("[%s] - More than one Enc/Dec methods found\n",
                    c.getSimpleName());
            return Optional.empty();
        }
        
        return result;  
    }
}
