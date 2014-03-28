package ist.meic.pa;

public class SuperTest {
	public int d;
	public float f;
	
	public void m5() {
		System.out.println("Call from superclass");
	}
	
	public Integer m8() {
		System.out.println("Call from superclass only");
		return new Integer(8);
	}
}
