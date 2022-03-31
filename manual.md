# Graph Compression for Preserving Shortest Path Distances
This is the manual providing usage instructions for all code used in this project. A main.java file is provided which accepts command line arguments for prodividing inputs and will output the execution time, average, and worst quality of the compression. The code does not log outputs as this may be performed as required using the Linux output operator >>.

## Command-Line Argument Usage
The code is provided with a pre-defined main.java file. The code may be compiled by navigating to the src folder and entering the command:  
    ```javac -client main/Main.java```  
The code may then be run by entering a command of the form:  
    ```java -client main.Main [Graph Filename] [Sparsification Method] [Terminals] [Percentage for Early Stopping] [Independent Set]```  
    
