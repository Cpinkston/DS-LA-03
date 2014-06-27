README

Authors : Yasin Dara & Evan Albright & Naozumi Hiranuma 
Last Modified : [Redacted]
Professor : [Redacted]
Class : Evolutionary Computing


- Paper 
	To view our assignments paper, locate the file in the current directory labeled
	
		"Travelling Salesman Project Paper (Evolutionary Computation).docx"

	(Edit: Data Science students. If you really want to read my paper, feel free to ask). 
		
		
- Presentation
	If you wish to view our presentation slides, locate the file in the current directory labeled
	
	"Comparative Study of the Application of Evolutionary Computing.pptx"
	
	
- Code
	To run and view our code, change directories into
		"./TSPtester"
	From there, a simple
		javac *.java
	will compile all necessary files, and to interface with the project itself please run
		java TSPtester #
	where # is replaced by an integer between 1 and 8.
	if you wish to test our code on a different graph of cities
		http://www.tsp.gatech.edu/data/
	is a very good resource for files in our handled format. To run an algorithm on a specified map please use
		java TSPtester # "fileloc.tsp"
	making sure the graph is in the current directory
	
	
	****no code was taken from outside sources****
	
	
	A brief description of each file may be found at the top, but the  basic layout is as follows:
	
		Main File:
			- TSPtester.java
		Algorithms :
			- RandomSearch.java
			- NearestNeighbor.java
			- InsertionHeuristic.java
			- TwoOptTour.java
			- SimulatedAnnealing.java
			- RegularGA.java
			- NichedGA.java
			- SwarmIntelligence.java
		Helper Classes :
			- Ant.java
			- PathGene.java
		Visualizer :
			- Plot2D.java
			- PlotPanel.class
		TSP graph files : 
			- sample.tsp
			- wi29.tsp
