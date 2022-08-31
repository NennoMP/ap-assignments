/*
* AnagramMain
*
* @description: Main class for the AnagramJobScheduler.
*
* @author: m.pinna10@studenti.unipi.it
*/

package com.mycompany.anagram;

import com.mycompany.framework.JobSchedulerContext;


public class AnagramMain {

    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please specify the absolute path of a directory!");
            return;
        }
        String dir_path = args[0];
        
        AnagramStrategy strat = new AnagramStrategy(dir_path);
        JobSchedulerContext context = new JobSchedulerContext(strat);
        context.run();
    }
}
