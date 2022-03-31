# Graph Compression for Preserving Shortest Path Distances
This is the manual providing usage instructions for all code used in this project. A main.java file is provided which accepts command line arguments for prodividing inputs and will output the execution time, average, and worst quality of the compression. The code does not log outputs as this may be performed as required using the Linux output operator >>.

## Command-Line Argument Usage
The code is provided with a pre-defined main.java file. The code may be compiled by navigating to the src folder and entering the command:  
    ```javac -client main/Main.java```  
The code may then be run by entering a command of the form:  
    ```java -client main.Main [Graph Filename] [Sparsification Method] [Terminals] [Percentage for Early Stopping] [Independent Set]```  
Some examples are shown below:  
    ```java -client main.Main Graphs/3KAS.txt sptree 10```  
    ```java -client main.Main Graphs/6KAS.txt REC Graphs/exampleTerminals.txt 10-15```  
    ```java -client main.Main Graphs/60KBrightKite.txt REC 10 100 ind```  

### Graph Filename
The graph may be supplied as any text file containing a representation of a graph. The command line input expects a graph of a specific format, particularly those found in SNAP graphs (snap.stanford.edu/data/). The format is as follows:  
*  *Lines 1 and 2:* Reserved for description of the graph, these **MUST** be included
*  *Line 3:* The number of vertices and edges in the following format
    *  Nodes:[Number of Nodes]  Edges:[Number of Edges]
    *  Please not that there is no space after the : and a tab separates the two
*  *Line 4:* A header of the form
    *  FromNodeId   ToNodeId
    *  Please note the tab separating the two
*  The rest of the file contains two vertex indexes per line separated by tabs indicating the from and to node IDs for example
    *  1 5 indicates an edge between vertices 1 and 5  
Several graphs of this format, specifically those used for experimentation in the dissertation, are provided in the src/Graphs/ folder.

### Sparsification Method
This flag indicates which compression algorithm should be used. The choices are REC, gauss, random, or sptree. The method is necessary, but will be overridden if a percentage of non-terminals, or the independent set flag is supplied.

### Terminals
This flag indicates the number of terminals to assign in the graph. This can be provided either as a number in which case that amount of random terminals will be generated in the graph, or as a file. The file should contain a list of comma separated vertex indexes **with spaces**, for example 1, 2, 3. An example terminal file is provided in the src/Graphs/ and the required format is shown.

### Percentage for Early Stopping
This is an **optional** flag which will override the sparsification method flag. If this flag is supplied it will indicate the percentage of non-terminals to contract before halting prematurely. This can either be provided as a single integer, or a range of the format n-m for example 10-15.

### Independent Set
This is an **optional** flag which will override both the sparsification method flag **and** the early stopping percentage flag. The early stopping percentage flag **must** be supplied and so it is recommended to simply enter either 0 or 100 for command clarity. The independent set argument can be anything deemed a sensible input for command clarity, however anything may be entered.

## Custom Usage
Command line usage as provided is quite simplistic and is merely used for experimentation as the outputs are vague and experimentation is limited. Custom usage in the form of importing the libraries or defining your own main.java file is supported and this section describes the utilities and functionalities of the code.  
  
Firstly a graph must be created. The Graph data structure can be found in the src/graphComponents/Graph.java file. Two utilities exist for loading a graph from a file, the utility described in the previous **Graph Filename** section can be found in the src/graphUtils/General.java file and is called fromSNAPFile(String filename). The expected format is described in the aforementioned section. Another utility for loading a graph from a text file contained within the Genera.java file is the fromFile(String filename) function, which loads a graph from an adjacency matrix representation where the first line indicates the number of vertices and the remaining lines are the adjacency matrix of edge weights. An example graph creation is shown below.
    ```Graph g = General.fromSNAPFile(filename);```
  
A sparsifier object, found in the src/algorithms/Sparsifier.java file may be created with this graph as follows:  
    ```Sparsifier spars = new Sparsifier(g);```  
Sparsification cannot begin until a method is chosen or until early stopping or independent set usage is defined and a set of terminals is provided or generated.  
  
The sparsifier will default to using the REC method and this can be left as an empty string ("") to indicate that REC is the desired method. Other methods include "gauss" for the Gaussian elimination method, "sptree" for the shortest path tree method, and "random" for the random selection method. (**NOTE: These will not be overridden as in command line usage**).  
  
If REC with either early stopping **or** independent set is desired this can be set as follows. For early stopping this can be set with ```spars.setEarlyStopping(n)``` where n is the percentage of non-terminals to contract, and for independent set simply calling ```spars.setUseIndSet(true);```.  
  
Terminals can be given to the sparsifier in two ways. The first method is to provide an Integer list (**Note: This must be an Integer[] list and not an int[] list**) of the vertex indices to be set as terminals to the ```spars.setTerminals(intList)``` function, and the second method is to call ```spars.randomTerminals(n)``` where n is the number of terminals to create.  
  
Once the sparsifier has been set up, compression of the graph may be performed with the sparsifier by calling ```spars.sparsify()```. This function will return a 3-tuple as a generic SimpleTuple object with types Double, Double, and Double. SimpleTuple.java can be found in src/graphUtils/simpleTuple.java and provides functions getFirst(), getSecond(), and getThird() which retrieve item respectively. The items returned by the call to ```spars.sparsify()``` are run time, average quality, and worst quality **in this order**.  
  
Quality calculations are a time-consuming process, and therefore the sparsifier provides functions ```spars.getPathLengths()``` which returns type ```double[][]``` and ```spars.setPathLengths(double[][] pathLengths)``` which enable the retrieval and assignment of path lengths for the first stage of quality calculations. This stage is where all shortest path lengths pre-compression are computed and allows for this step to be skipped for later sparsifications with the same terminal set. Path lengths may only be retrieved **after** the ```spars.sparsify()``` call has been made.
