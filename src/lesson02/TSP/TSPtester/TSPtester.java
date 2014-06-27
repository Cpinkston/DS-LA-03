/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

Main file of the TSP algorithm performance testing :
	-currently 8 cases are available for use
	-if no graph is specified then the sample graph will be employed
	-launches a visualizer based on the solution of an algorithm
*/
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TSPtester{
	static int optimalDist = 100;
	
	public static ArrayList<double[]> loadCities(String fileloc){
		ArrayList<double[]> cities = new ArrayList<double[]>();
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(fileloc));
 			
 			boolean flag = false;
 			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.equals("EOF")){
					flag = false;
				}
				if(flag==true){
					//System.out.println(sCurrentLine);
					String[] temp = sCurrentLine.split(" ");
					//System.out.println(temp[1]+" "+temp[2]);
					double[] temp2 = new double[3];
					temp2[0] = Double.parseDouble(temp[0]);
					temp2[1] = Double.parseDouble(temp[1]);
					temp2[2] = Double.parseDouble(temp[2]);
					cities.add(temp2);
				}
				if(sCurrentLine.equals("NODE_COORD_SECTION")){
					flag = true;
				}
					
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
		return cities;
	}
	
	public static double calcDist(ArrayList<double[]> path){
		double sum = 0;
		double distance = 0;
		for(int i=0; i<path.size()-1; i++){
		    double[] p1 = path.get(i);
		    double[] p2 = path.get(i+1);
		    distance = Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		    sum += distance;
		}
		// connect the tour in evaluation
		double[] p1 = path.get(0);
		double[] p2 = path.get(path.size()-1);
		sum+=Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		return sum;
	}

	public static void main(String[] args) {
		if(args.length == 0){
			 
			 System.out.println("\nUSAGE: java TSPsolver algorithm#");
			 System.out.println("-------------------------------------------");
			 System.out.println("1 - Random approach");
			 System.out.println("2 - Nearest neighbor optimization");
			 System.out.println("3 - Insertion heuristics");
			 System.out.println("4 - 2-opt tour improvement");
			 System.out.println("5 - Simulated annealing");
			 System.out.println("6 - Genetic algorithm");
			 System.out.println("7 - Genetic algorithm with fitness sharing");
			 System.out.println("8 - Ant-colony optimization");
			 System.out.println("-------------------------------------------");
			 
		}else{
			int choice = Integer.parseInt(args[0]);
			
			//Loading cityfile
			String fileloc;
			if(args.length != 2){
				fileloc = "sample.tsp";
			}else{
				fileloc = args[1];
				System.out.println("Loading a tsp file: "+fileloc);
			}
			ArrayList<double[]> cities;
			cities = loadCities(fileloc);
			
			//Stores a solution.
			ArrayList<double[]> solution = new ArrayList<double[]>();;
			
			//Run an algorithm based on a user choice.
			switch (choice) {
				case 1: 
					System.out.println("Random search");
					RandomSearch RS = new RandomSearch(cities);
					solution = RS.run();
					System.out.println("Total distance for RandomSearch is : "+calcDist(solution));
					break;
				case 2: 
					System.out.println("Nearest neighbor heuristic");
					NearestNeighbor NN = new NearestNeighbor(cities);
					solution = NN.run();
					System.out.println("Total distance for NearestNeighbor is : "+calcDist(solution));
					break;
				case 3: 
					System.out.println("Insertion");
					InsertionHeuristic IH = new InsertionHeuristic(cities);
					solution = IH.run();
					System.out.println("Total distance for Insertion is : "+calcDist(solution));
					break;
				case 4: 
					System.out.println("2-optimization tour improvment");
					TwoOptTour TT = new TwoOptTour(cities);
					solution = TT.run();
					System.out.println("Total distance for 2Opt is : "+calcDist(solution));
					break;
				case 5: 
					System.out.println("Simulated annealing");
					SimulatedAnnealing SA = new SimulatedAnnealing(cities);
					solution = SA.run();
					System.out.println("Total distance for SimulatedAnnealing is : "+calcDist(solution));
					break;
				case 6: 
					System.out.println("Regular genetic algorithm");
					RegularGA RGA = new RegularGA(cities);
					solution = RGA.run();
					System.out.println("Total distance for RegGeneticAlgorithm is : "+calcDist(solution));
					break;
				case 7: 
					System.out.println("Niched genetic algorithm");
					NichedGA NGA = new NichedGA(cities);
					solution = NGA.run();
					System.out.println("Total distance for NichedGeneticAlgorithm is : "+calcDist(solution));
					break;
				case 8: 
					System.out.println("Ant-Colony optimization (swarm intelligence)");
					SwarmIntelligence SI = new SwarmIntelligence(cities);
					solution = SI.run();
					System.out.println("Total distance for SwarmIntelligence is : "+calcDist(solution));
					break;
				default:
					System.out.println(choice+" is not a valid choice.");
					System.exit(0);
			}
			

			//Run visualizer on a solution.
			Plot2D.run(solution);	
		}
	}
}



