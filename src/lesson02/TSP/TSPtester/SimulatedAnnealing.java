/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

SimmulatedAnnealing approach to the TSP : 
	TALK ABOUT ALGORITHM HERE
*/
import java.util.*;

public class SimulatedAnnealing extends Solver{
    int evalnum = 100000;
    int keepProb = 995; //(out of 1000)
    Random rgen = new Random();
    PathGene cur;
    
    public SimulatedAnnealing(ArrayList<double[]> cityList){
	super(cityList);
	cur = new PathGene(cityList);
        cur.fitness = cur.evaluate();
    }
    
    public ArrayList<double[]> run(){ 
	for(int i=0; i<evalnum; i++){
	    PathGene oriCur = new PathGene(cur);
	    PathGene newCur  = new PathGene(cur);
	    newCur.mutate();
	    newCur.fitness = newCur.evaluate();
	    if(newCur.fitness<oriCur.fitness){
		cur = newCur;
		//System.out.println("hi");
	    }else{
		if(rgen.nextInt(1000)<keepProb){
		    cur = oriCur;
		}else{
		    cur = newCur;
		}
		//System.out.println("yo");
	    }
	}
	return cur.genome;
    }
}