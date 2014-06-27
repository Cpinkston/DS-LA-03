/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

NearestNeighbor approach to the TSP : 
	Start at the first city
	While there are unused cities -
		Add to the tour the city nearest the last added city
	Return the tour
	
	This is an O(n^2) operation
	
TODO : 
	it could be possible to keep track of a path's distance while it's constructed so
	we could avoid recalculating it, but it doesn't seem to be slow enough to warrant the effort
*/
import java.util.*;

public class NearestNeighbor extends Solver{
	
    ArrayList<double[]> greedyCities;	//storage for later use


	public NearestNeighbor(ArrayList<double[]> cityList){
		super(cityList);
		greedyCities = cityList;
	}
	
	
	private double[] pickNearest(double[] current_city, ArrayList<double[]> unchosen_cities){

		double[] p1 =current_city;
		double[] p2 = unchosen_cities.get(0);
		double min_distance = Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		double[] best_choice = unchosen_cities.get(0);

		double distance = 0;
		for(int i=0; i<unchosen_cities.size(); i++){
			p1 =current_city;
			p2 = unchosen_cities.get(i);
			distance = Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
			if (distance < min_distance){
				min_distance = distance;
				best_choice = p2;
			}
		}
		return best_choice;
	}


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

		//Process the path choices
		double[] next_city;
		double[] current_city = greedyCities.get(0);

		while (unchosen_cities.size() > 0){
			next_city = pickNearest(current_city,unchosen_cities);
			chosen_cities.add(next_city);
			unchosen_cities.remove(unchosen_cities.indexOf(next_city));

			current_city = next_city;
		}

		//Store the path
		solution = chosen_cities;
		return solution;
	}
}