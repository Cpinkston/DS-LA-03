/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

RandomSearch approach to the TSP : 
	Loop a set number of times -
		Start at the first city
		Construct a random path
	 	Save as the best if this beats the older paths
	 Return the best seen path
*/
import java.util.*;

public class RandomSearch extends Solver{
    int popsize = 100000;
    ArrayList<PathGene> population;
    public RandomSearch(ArrayList<double[]> cityList){
	super(cityList);
	population = new ArrayList<PathGene>();
	for(int i=0; i<popsize; i++){
	    PathGene temp = new PathGene(cityList);
	    temp.fitness = temp.evaluate();
	    //System.out.println(temp.evaluate());
	    population.add(temp);
	}
    }
	
    //Simply returns the best answer among the randomly generated individuals.
    public ArrayList<double[]> run(){
	PathGene min = population.get(0);
	for(int i=1; i<popsize; i++){
	    if(population.get(i).fitness<min.fitness){
		min = population.get(i);
	    }
	}

	return min.genome;
    }
}
		