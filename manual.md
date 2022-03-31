# Graph Compression for Preserving Shortest Path Distances
This is the manual providing usage instructions for all code used in this project. A main.java file is provided which accepts command line arguments for prodividing inputs and will output the execution time, average, and worst quality of the compression. The code does not log outputs as this may be performed as required using the Linux output operator >>.

## Command-Line Argument Usage
The code is provided with a pre-defined main.java file. The code may be compiled by navigating to the src folder and entering the command:  
    ```javac -client main/Main.java```  
The code may then be run by entering a command of the form:  
    ```java -client main.Main [Graph Filename] [Sparsification Method] [Terminals] [Percentage for Early Stopping] [Independent Set]```  
    
### Graph Filename
The graph may be supplied as any text file containing a representation of a graph. The command line input expects a graph of a specific format, particularly those found in SNAP graphs (snap.stanford.edu/data/). The format is as follows:  
*  *Lines 1 and 2:* Reserved for description of the graph, these **MUST** be included
*  *Line 3:* The number of vertices and edges in the following format
    *  Nodes:[Number of Nodes]  Edges:[Number of Edges]
    *  Please not that there is no space after the : and a tab separates the two
*  *Line 4:* A header of the form
    *  FromNodeId   ToNodeId
    *  Please not the tab separating the two
*  The rest of the file contains two vertex indexes per line separated by tabs indicating the from and to node IDs for example
    *  1 5 indicates an edge between vertices 1 and 5
