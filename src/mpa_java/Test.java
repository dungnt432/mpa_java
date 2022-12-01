package mpa_java;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting MPA Algorithm");
		MPA mpaSolver = new MPA();
		System.out.println("Result:");
		for(int i=0; i!=mpaSolver.elite.size; i++) {
			System.out.printf("x%d: %d\n", i+1, mpaSolver.elite.x[i]);
		}
	}

}
