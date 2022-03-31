package main;


import java.io.IOException;

import algorithms.Sparsifier;
import graphComponents.Graph;
import graphUtils.General;
import graphUtils.SimpleTuple;


public class Main {

    static Graph g;
    static Sparsifier spars;

    static int startRange, endRange;
    static boolean useRange = false;

	public static void main(String[] args) throws IOException {

        loadFromArgs(args);

        if (useRange) {
            for (int i = startRange; i <= endRange; i++) {
                args[3] = Integer.toString(i);
                loadFromArgs(args);

		        SimpleTuple results = spars.sparsify();
                
                System.out.println("Early stopping at " + i + "%");
                System.out.println("Time taken: "      + results.getFirst());
                System.out.println("Average Quality: " + results.getSecond());
                System.out.println("Worst Quality: "   + results.getThird());
            }

        } else {
		    SimpleTuple results = spars.sparsify();

            System.out.println("Time taken: "      + results.getFirst());
            System.out.println("Average Quality: " + results.getSecond());
            System.out.println("Worst Quality: "   + results.getThird());
        }

	}


    private static void loadFromArgs(String[] args) {
        /*Create the Graph object*/
        try {
            g = General.fromSNAPFile(args[0]);

        } catch (Exception e) {
            System.out.println("Unable to load file.\n");
            return;

        }

        
        /*Create the Sparsifier object*/
        try {
            spars = new Sparsifier(g);

        } catch (Exception e) {
            System.out.println("Unable to create sparsifier.\n");
            return;
        }

        
        try {
            spars.setMethod(args[1]);

        } catch (Exception e) {
            System.out.println("Method not given, or not recognised.\n");

        }
        
        try {
            spars.randomTerminals(Integer.parseInt(args[2])); //Assign random terminals if number supplied

        } catch (Exception e) {
            if (e instanceof ArrayIndexOutOfBoundsException) {
                System.out.println("No terminals supplied.\n");
                return;

            } else if (e instanceof NumberFormatException) {
                try {
                    spars.setTerminals(General.terminalsFromFile(args[2])); //Otherwise assign list of defined terminals

                } catch (Exception f) {
                    System.out.println("Terminals formatted incorrectly.\n");
                    return;
                }

            } else {
                System.out.println("Unknown error when loading terminals.\n");
            }

        }
        

        /*See if early stopping percentage was supplied as either integer or range*/            
        try {
            spars.setEarlyStopping(Integer.parseInt(args[3]));
            spars.setMethod("");    //Return to REC if early stopping used

        } catch (Exception e) {
            try {
                String[] ranges = args[3].split("-");

                startRange = Integer.parseInt(ranges[0]);
                endRange   = Integer.parseInt(ranges[1]);

                useRange = true;

                spars.setMethod("");

            } catch (Exception f) {
                System.out.println("No early stopping or incorrec formatting.");

            }

        }

        try {
            String indSet = args[4];
            spars.setMethod("");
            spars.setUseIndSet(true);

        } catch (Exception e) {
            System.out.println("No independent set supplied.\n");

        }
    }
}
