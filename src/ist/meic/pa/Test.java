package ist.meic.pa;

public class Test extends SuperTest {

	public int d;
	public boolean b;
	public String name = "Test";
	
	public Test m1(){
		System.out.println("Hello, I am " + this);
		return this;
	}
	
	public Test m2(int i){
		System.out.println("Printing integer " + i);
		return this;
	}
	
	public Test m3(float f) {
		System.out.println("Printing float " + f);
		return this;
	}
	
	public Test sum(int i1, int i2){
		System.out.println("Sum: " + (i1+i2));
		return this;
		
	}
	
	public Test sum(float f1, float f2){
		System.out.println("Sum: " + (f1+f2));
		return this;
		
	}
	
	public Test sum(String s1, String s2) {
		System.out.println(s1 + s2);
		return this;
	}
	
	public Test sayMyName(String name){
		System.out.println("Your name is " + name);
		return this;
	} 
	
	public Test longNumber(long num){
		System.out.println("This is a loooooooooooooooooooong number: " + num);
		return this;
	}
	
	public Test aChar (char c){
		System.out.println("This is a tiny little char: " + c);
		return this;
	}
	
	public Test doubleTrouble(double n){
		System.out.println("A Double: " + n);
		return this;
	}
	
	public Test myBool(boolean b){
		System.out.println("Your argument is " + b);
		return this;
	}
	
	public int giveMeInt() {
		return 100;
	}
	
	public Test anArray(int[] arr) {
		System.out.print("This is an array: [");
		for (int i : arr) {
			System.out.print(" ");
			System.out.print(i);
		}
		System.out.println(" ]");
		return this;
	}
	
	public Test anArrayFloating(float[] arr) {
		System.out.print("This is a floating array: [");
		for (float i : arr) {
			System.out.print(" ");
			System.out.print(i);
		}
		System.out.println(" ]");
		return this;
	}
	

	public void noReturn() {
		System.out.println("No turning back");
	}
	
	public String aString() {
		return "Hello world";
	}

	public Test testObj (Test obj){
		System.out.println("I got an object: " + obj);
		return this;

	}
	
	public void m5() {
		System.out.println("Call from class");
	}
	
	public void m6(Integer i) {
		System.out.println("This is an integer: " + i);
	}
	
	public void m7(Integer i, Integer k) {
		System.out.println("Two integers: " + i + " and " + k);
	}
	
	public void m7(Float f, Float k) {
		System.out.println("A float and an integer: " + f + " and " + k);
	}
	
	public void twoVars(int i, String s) {
		System.out.println("There are two variables saved: " + i + " and "+ s);
	}
}
