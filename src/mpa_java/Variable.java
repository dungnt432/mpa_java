package mpa_java;

import java.lang.Math;
import java.util.Random;

public class Variable {
	public int size = 4;
	public int[] x = new int[size];
	private double fitness;
	Random rd = new Random();

	public double getFitness() {
		this.fitness = this.fitness();
		return fitness;
	}
	
	public void setX(int[] array) {
		for(int i=0; i != size; i++) {
			this.x[i] = array[i];
		}
	}

	public Variable() {
		do {
			for(int i=0; i != size; i++) {
				this.x[i] = rd.nextInt(100);
			}			
		} while(!constraint());
	}
	
	// Define your own constraint
	public boolean constraint() {
		boolean c1 = this.x[0] + this.x[1] + this.x[2] + this.x[3] < 200;
		return c1;
	}
	
	// Define your fitness function
	public double fitness() {
		this.fitness = this.x[0] + this.x[1] - Math.pow(this.x[2], 2) + this.x[3];
		return this.fitness;
	}

}
