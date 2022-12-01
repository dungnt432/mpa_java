package mpa_java;

import java.util.Random;

public class MPA {
	public final int ITmax = 200;
	public final int N = 100;
	public final double P = 0.5; 	
	public final double FADs = 0.2;

	public int minValue = 0;
	public int maxValue = 100;
	public double beta = 1;
	public double alpha = 1.5;
	public Variable elite;
	
	public Variable[] prey = new Variable[N];
	
	Random rd = new Random();
	
	public MPA() {
		this.initialize();
		int it = 0;
		int top = 0;
		int size = this.prey[0].size; 
		
		top = this.selectTopPrey();			
		this.elite = prey[top];
		
		while(it < ITmax) {			
			if(it < (int)(ITmax/3)) {
				for(int i = 0; i != N; i++) {
					Variable temp_prey = new Variable();
					for(int j = 0; j != size; j++) {
						double RB = brownian(beta);
						double r = rd.nextDouble();
						double step = RB*(elite.x[j] - RB*prey[i].x[j]);
						temp_prey.x[j] = (int)(prey[i].x[j] + P*r*step);
						temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
					}
					
					if(checkDominate(temp_prey, prey[i])) {
						for(int j = 0; j != size; j++) {
							prey[i].x[j] = temp_prey.x[j];
						}
					}
				}
			}
			else if(it < (int)(2*ITmax/3)) {
				double cf = Math.pow(1 - it/ITmax, 2*it/ITmax);
				for(int i = 0; i != N; i++) {
					Variable temp_prey = new Variable();
					if(i < (int)(N/2)) {
						for(int j = 0; j != size; j++) {
							double RL = levy(alpha);
							double r = rd.nextDouble();
							double step = RL*(elite.x[j] - RL*prey[i].x[j]);
							temp_prey.x[j] = (int)(prey[i].x[j] + P*r*step);
							temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
						}
					}
					else {
						for(int j = 0; j != size; j++) {
							double RB = brownian(beta);
							double step = RB*(RB*elite.x[j] - prey[i].x[j]);
							temp_prey.x[j] = (int)(elite.x[j] + P*cf*step);
							temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
						}
					}
					
					if(checkDominate(temp_prey, prey[i])) {
						for(int j = 0; j != size; j++) {
							prey[i].x[j] = temp_prey.x[j];
						}
					}
				}
			}
			else {
				double cf = Math.pow(1 - it/ITmax, 2*it/ITmax);
				for(int i = 0; i != N; i++) {
					Variable temp_prey = new Variable();
					for(int j = 0; j != size; j++) {
						double RL = levy(alpha);
						double step = RL*(RL*elite.x[j] - prey[i].x[j]);
						temp_prey.x[j] = (int)(elite.x[j] + P*cf*step);
						temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
					}
					
					if(checkDominate(temp_prey, prey[i])) {
						for(int j = 0; j != size; j++) {
							prey[i].x[j] = temp_prey.x[j];
						}
					}
				}				
			}
			
			if(rd.nextDouble() <= FADs) {
				double cf = Math.pow(1 - it/ITmax, 2*it/ITmax);
				for(int i = 0; i != N; i++) {
					Variable temp_prey = new Variable();
					for(int j = 0; j != size; j++) {
						double r = rd.nextDouble();
						double Rand = rd.nextDouble();
						int U = r < FADs ? 0 : 1;
						temp_prey.x[j] = (int)(prey[i].x[j] + cf*(minValue + Rand*(maxValue - minValue))*U);
						temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
					}
					
					if(checkDominate(temp_prey, prey[i])) {
						for(int j = 0; j != size; j++) {
							prey[i].x[j] = temp_prey.x[j];
						}
					}					
				}
			}
			else {
				double r = rd.nextDouble();
				int r1 = rd.nextInt(N);
				int r2 = rd.nextInt(N);
				double step[] = new double[size];
				for(int j = 0; j != size; j++) {
					step[j] = (FADs*(1-r) + r)*(prey[r1].x[j] - prey[r2].x[j]);
				}	
				for(int i = 0; i != N; i++) {	
					Variable temp_prey = new Variable();
					for(int j = 0; j != size; j++) {
						temp_prey.x[j] = (int) (prey[i].x[j] + step[j]);
						temp_prey.x[j] = this.checkValue(temp_prey.x[j]);
					}
					
					if(checkDominate(temp_prey, prey[i])) {
						for(int j = 0; j != size; j++) {
							prey[i].x[j] = temp_prey.x[j];
							}
					}	
				}
			}
			
			it++;
			top = this.selectTopPrey();			
			this.elite = prey[top];
		}
	}
	
	public double gamma(double x) {
	      double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
	      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
	                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
	                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
	      double log = tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	      return Math.exp(log); 
	   }
	
	public double levy(double alpha) {
		double num = gamma(1 + alpha)*Math.sin(Math.PI*alpha/2);
		double den = gamma((1 + alpha)/2)*alpha*Math.pow(2, (alpha - 1)/2);
		
		double sigma_x = Math.pow(num/den, 1/alpha);
		
		double x = rd.nextGaussian()*sigma_x*sigma_x;
		double y = rd.nextGaussian();
		
		return 0.05*x/(Math.pow(Math.abs(y), 1/alpha));
	}
	
	public double brownian(double beta) {
		return rd.nextGaussian(0, beta);
	}
	
	public void initialize() {
		for(int i=0; i != N; i++) {
			this.prey[i] = new Variable();
		}
	}

	public boolean checkDominate(Variable prey1, Variable prey2) {
		return prey1.getFitness() < prey2.getFitness();
	}
	
	public int checkValue(int value) {
		if(value > this.maxValue) {
			return this.maxValue;
		}
		else if (value < this.minValue) {
			return this.minValue;
		}
		else {
			return value;
		}
	}
	
	public int selectTopPrey() {
		int length = prey.length;
		int pos = 0;
		for(int i = 1; i != length; i++) {
			if(checkDominate(this.prey[i], this.prey[pos])) {
				pos = i;
			}
		}
		return pos;
	}
	
	
}
