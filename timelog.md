# Timelog

* Graph Compression for Preserving Shortest Path Distances
* Jake Haakanson
* 2407682H
* Gramoz Goranci

## Week 1
### 20/09/2021
* *2 Hours* Read project guidance slides

## Week 2
### 30/09/2021
* *3 Hours* Researched potentially relevant papers in the area prior to the initial meeting with supervisor

### 01/10/2021
* *3 Hours* Researched readings as above

## Week 3
### 04/10/2021
* *0.5 Hours* Meeting with supervisor

### 05/10/2021
* *0.5 Hours* Setup Github repo with project files, readme, timelog, etc...
* *1 Hour* Created basic data structures of graph
* *1 Hour* Researched edge contractions
* *1 Hour* Possible idea for edge contraction method

## Week 4
### 11/10/2021
* *0.5 Hours* Meeting with supervisor

###12/10/2021
* *0.75 Hours* Meeting with supervisor
* *1.5 Hours* Created graphUtils and main packages, updated fields and data types, craeted toString method in Graph
* *1.5 Hours* Implemented basic version of Gaussian elimination algorithm. Also added utility methods such as removing vertex from graph and adj list, and updating weight of adjnode.
* *1 Hour* Upadated Vertex representations, fixed graph resizing

###17/10/2021
* *0.5 Hours* Added plan.md, set up Overleaf
* *0.5 Hours* Wrote formal definition of graph compression into introduction of dissertation
* *2 Hours* Implemented Vertex sparsification method, fixed some errors in Gaussian method
* *0.5 Hours* Minor tweaks to both algorithms
* *0.5 Hours* Fixed issues with algorithms giving incorrect weights

## Week 5
### 18/10/2021
* *0.5 Hours* Meeting with supervisor

### 19/10/2021
* *0.5 Hours* Implemented priority/FIFO queue data structure

### 24/10/2021
* *3.3 Hours* Fixed issues with priority queue, implemented Dijkstra's algorithm, implemented min-degree heuristic for contracting nonterminals, ran tests and visualised graphs using diagrams
* *0.5 Hours* Created simple graph generation script
* *0.5 Hours* Researched Alias method of sampling from a discrete distribution
* *2.5 Hours* Implemented edge sampling using alias method for edge contractions, rewrote contract function, create new 3-tuple class

## Week 6
### 30/10/2021
* *2 Hours* Fixed issues with contract method, see issue 19 on git. Began implementing random terminal selection for quality checks
* *2.5 Hours* Tidied up code, implemented random terminal selection, implemented basic sparsifier quality checks

## Week 7
### 01/11/2021
* *0.5 Hours* Supervisor meeting, added issues to Git issue tracker

### 07/11/2021
* *5 Hours* Major changes to code, reworked adjacency list representation and adjusted algorithms to use this. Reworked Gauss and contract methods promoting efficiency and minimal code. Adjusted Dijkstra's algorithm to find shortest paths to all Vertices in graph from a given start point.
* *2 Hours* Added random terminal selection to sparsifier and ability to extract terminal sets for reuse. Added basic quality checking in sparsify method. Made comments more informative.

## Week 8
### 08/11/2021
* *0.5 Hours* Meeting with supervisor

### 09/11/2021
* *2 Hours* Worked on dissertation

## Week 9
### 16/11/2021
* *1 Hour* Corrected quality checks

## Week 10
### 22/11/2021
* *0.5 Hours* Meeting with supervisor
* *1.5 Hours* Implemented naive method for sparsifying using shortest path tree

### 27/11/2021
* *4.5 Hours* Implemented AVLTree, modified partitioning to use this instead. Refined test outputs and new sptree method

## Week 11
### 08/12/2021
* *2.5 Hours* Worked on dissertation

### 12/12/2021
* *1 Hour* Read Paper on Maximum Independent Set approx algorithms

## Week 12
### 13/12/2021
* *0.5 Hours* Meeting with supervisor
* *1.5 Hours* Read papers briefly on greedy independent set and Dijkstra's shortest path tree algorithm
* * 2.5 Hours* Worked on dissertation implementation descriptions, considered issues that may arise in current implementation (see issues)

### 14/12/2021
* *2 Hours* Wrote status report and submitted to moode and project supervisor
* *1 Hour* Created new graph and tested compression on personal machine, took too long to terminate :(
* *1 Hour* Read details of greedy maximum independent set approximation algorithm
* *2 Hours* Implemented approx algorithm for maximum independent set of non-terminals
* *1.5 Hours* Began separating sparsifier class and packages into individual classes for each sparsification method

### 15/12/2021
* *2.5 Hours* Finished separating sparsifiers into individual classes, added independent set functionality, fixed issues with it not working properly

### 16/12/2021
* *7.5 Hours* Minor tests, read paper on dijkstra minors a bit more, worked on dissertation: added to intro and subsections, added figures, completely rewrote section on gaussian elimination

### 18/12/2021
* * 5 Hours* Updated definitions section to be more general definitions of graph theory terms. Some changes to intro and section describing Gaussian elimination algorithm. Began writing description of REC algorithm. Searched for and read papers/chapters describing details of alias method sampling + edge contractions etc.

## Week 13
### 06/01/2022
* *2.5 Hours* Looked for and read some papers on the alias sampling method

### 12/01/2022
* *2 Hours* Re-read and wrote critical summary of Graph Theory and Gaussian Elimination by Robert Tarjan (1975)
* *2 Hours* Read and wrote critical summary of Compression of Weighted Graphs by Tiovonen H and Zhou F et. al. (2011) (To be completed)

### 13/01/2022
* *1 Hour* Finished critical summary of Compression of Weighted Graphs (2011) from yesterday
* *4 Hours* Read and wrote critical summary of Preserving Terminal Distances Using Graph Minor (2018)
* 1 Hour* Improvements and abstractions to sparsifier class, new sparsification method

## Week 14
### 17/01/2022
* *0.5 Hours* Meeting with supervisor

### 20/01/2022
* *2 Hours* Implemented and tested early stopping and independent set methods
* *1 Hour* Planned experiments to be conducted with each aslgorithm
* *1 Hour* Constructed smaller graphs from the provided database

### 23/01/2022
* *2 Hours* Looked further into the large graph database and began implementing algorithm for constructing large graphs using the database files

## Week 15
### 24/01/2022
* *1 Hour* Finished implementing function for reading in large graph files from SNAP database
* *0.5 Hours* Meeting with supervisor
* *0.5 Hours* Refined quality checks to report average and worst quality

### 25/01/2022
* *1 Hour* Obtained larger graph, added to timings and quality checks

### 26/01/2022
* *2.5 Hours* Modified quality checks slightly and also independent set algorithm

### 27/01/2020
* *2 Hours* Fixed issues with independent set alg, reworked quality checks, obtained bigger file (200K nodes)
* *2 Hours* More testing and tweaks to quality and some algorithms, better abstractions for neatness/readability, fixed some slow parts of the code such as vertex retrieval

## Week 16
### 31/01/2022
* *0.5 Hours* Minor tweaks, removed eclipse stuff as no longer using it
* *0.5 Hours* Meeting with supervisor
