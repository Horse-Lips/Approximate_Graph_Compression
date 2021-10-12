package main;


import java.io.IOException;

import graphComponents.Graph;
import graphUtils.General;

public class Main {
	public static void main(String[] args) throws IOException {
		String file = "C:/Users/CallM/Documents/Life/UniGlasgow/Year4/Project/Code/exampleGraph.txt";
		
		Graph G = General.fromFile(file);
		System.out.println(G);
		
	}
}
