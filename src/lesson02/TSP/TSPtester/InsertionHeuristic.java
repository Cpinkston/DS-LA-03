/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

InsertionHeuristic approach to the TSP : 
	Begins with a sub-tour consisting of the first city only
	While there are unused cities -
		Find what the smallest distance increase would be if any city was inserted anywhere in the sub-tour
		Insert the smallest distance increasing city into the correct location
	Return the final sub-tour which at this point is the entire tour of the graph
	
	The smallest distance finder is an O(n^2) operation for the first pass, and decrements n each pass.
	making it slightly more efficient than just an O(n^2) each pass algorithm.
*/
import java.util.*;

public class InsertionHeuristic extends Solver{
	
    ArrayList<double[]> greedyCities;	//storage for use later


	public InsertionHeuristic(ArrayList<double[]> cityList){
		super(cityList);
		greedyCities = cityList;
	}
	
	
	//evaluate the path passed in
    private double totalDistance(ArrayList<double[]> path){
		double sum = 0;
		double distance = 0;
		for(int i=0; i<path.size()-1; i++){
		    double[] p1 = path.get(i);
		    double[] p2 = path.get(i+1);
		    distance = Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		    sum += distance;
		}
		double[] p1 = path.get(0);
		double[] p2 = path.get(path.size()-1);
		sum+=Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		return sum;
    }


	public ArrayList<double[]> run(){
		//Initialize covered & uncovered nodes
		ArrayList<double[]> chosen_cities = new ArrayList<double[]>();
		chosen_cities.add(greedyCities.get(0)); //keep city 1 as starting point
		ArrayList<double[]> unchosen_cities = new ArrayList<double[]>(greedyCities); //copy over all cities
		unchosen_cities.remove(0); //remove city 1 as it's already chosen to be the start

		// initialize min distance info storage variables
		double[] best_city;
		int best_index;
		double min_distance;
		// run until no more cities remain to add
		while (unchosen_cities.size() > 0){
			// set up for min distance
			chosen_cities.add(0,unchosen_cities.get(0));
			min_distance = totalDistance(chosen_cities);
			chosen_cities.remove(0);
			best_city = unchosen_cities.get(0);
			best_index = 0;

			// locate smaller insertion
			double distance;
			for(int i=0; i<unchosen_cities.size(); i++){ // for every remaining city
				for (int j=0; j<chosen_cities.size(); j++){ // test the distance at every index of the tour
					chosen_cities.add(j,unchosen_cities.get(i));
					distance = totalDistance(chosen_cities);
					if (distance < min_distance){
						min_distance = distance;
						best_city = unchosen_cities.get(i);
						best_index = j;
					}
					chosen_cities.remove(j);
				}
			}

			//insert into picked and remove from remaining
			chosen_cities.add(best_index,best_city);
			unchosen_cities.remove(unchosen_cities.indexOf(best_city));
		}

		//Store the path
		solution = chosen_cities;
		return solution;
	}
}