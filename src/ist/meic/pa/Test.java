package ist.meic.pa;

public class Test {

	public int d;
	
	public Test m1(){
		System.out.println("Hello, I am " + this);
		return this;
	}
	
	public Test m2(int i){
		System.out.println("Printing integer " + i);
		return this;
	}
	
	public Test sum(int i1, int i2){
		int sum = i1 + i2;
		System.out.println("Sum: " + sum);
		return this;
		
	}
}
