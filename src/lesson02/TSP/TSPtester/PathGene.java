/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

Helper class for the evolutionary algorithms :
	Stores genome information, mutation functions, fitness values, etc.
*/
import java.util.*;

public class PathGene{
    Random rgen;
    ArrayList<double[]> genome;
    double fitness;

    //Instantiates an individual based on a city list.
    public PathGene(ArrayList<double[]> cityList){
	//Initializing random path genome
	Collections.shuffle(cityList);
	genome = new ArrayList<double[]>(cityList);
	//Make sure it start with id number 1
	for(int i=0; i<genome.size(); i++){
	    if(genome.get(i)[0]==1){
		double[] temp = genome.get(0);
		genome.set(0, genome.get(i));
		genome.set(i, temp);
	    }
	}	
	rgen = new Random();
	fitness = 0;
    }

    public PathGene(PathGene dup){
	genome = new ArrayList<double[]>(dup.genome);
	rgen = new Random();
	fitness = dup.fitness;
    }

    //Shuffles two alleles randomly = mutation.
    public void mutate(){
	//replacement mutations
	if(rgen.nextInt(3)<1){
	    int index1 = rgen.nextInt(genome.size()-1)+1;
	    int index2 = rgen.nextInt(genome.size()-1)+1;
	    double[] temp = genome.get(index1);
	    genome.set(index1, genome.get(index2));
	    genome.set(index2, temp);
	    //sliding mutations
	}else if(rgen.nextInt(3)<2){
	    int index1 = rgen.nextInt(genome.size()-1)+1;
	    double[] temp = genome.get(index1);
	    for(int i=index1; i<genome.size()-1; i++){
		genome.set(i,genome.get(i+1));
	    }
	    genome.set(genome.size()-1, temp);
	}else{
	    int index1 = rgen.nextInt(genome.size()-1)+1;
	    double[] temp = genome.get(index1);
	    for(int i=index1; i>0; i--){
		genome.set(i,genome.get(i-1));
	    }
	    genome.set(0, temp);
	}
    }

    //Crossover. A modified version of 2-point cross over. 
    //If parent1 = "2736514" and parent2 = "7451236" and you want cross over at index 3 and 5, the result looks as follows:
    //The child will inherit "651" part of parent 1 and the rest is going to be from parent 2. 
    //Thus, it will look like child = 742"651"3.
    public ArrayList<double[]> crossover(PathGene gene2){
	int index1 = rgen.nextInt(genome.size());
	int index2 = rgen.nextInt(genome.size());
	int count = 0;
	ArrayList<double[]> child = new ArrayList<double[]>();
	
	for(int i=0; i<gene2.genome.size(); i++){
	    double[] temp = gene2.genome.get(i);
	    boolean flag = true;
	    for(int j=index1; j<index2; j++){
		if(temp[0] == genome.get(j)[0]){
		    flag = false;
		}
	    }
	    if(flag == true){
		child.add(gene2.genome.get(i));
	    }
	    if(i==index1){
		for(int t=index1; t<index2; t++){
		child.add(genome.get(t));
		}
	    }
	}
	return child;
    }

    //Calculates the length of the path
    public double evaluate(){
	double sum = 0;
	double distance = 0;
	for(int i=0; i<genome.size()-1; i++){
	    double[] p1 = genome.get(i);
	    double[] p2 = genome.get(i+1);
	    distance = Math.sqrt(Math.pow(Math.abs(p1[1]-p2[1]),2)+Math.pow(Math.abs(p1[2]-p2[2]),2));
	    sum += distance;
	}
	double[] p1 = genome.get(0);
	double[] p2 = genome.get(genome.size()-1);
	sum+=Math.sqrt(Math.pow(Math.abs(p1[1]-p2[1]),2)+Math.pow(Math.abs(p1[2]-p2[2]),2));
	return sum;
    }
	
	
}