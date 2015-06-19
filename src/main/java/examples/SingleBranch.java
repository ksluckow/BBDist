package examples;


/**
 * random example...
 *
 */
public class SingleBranch {
	
	public static void main(String[] args) {
		SingleBranch s = new SingleBranch();
		int val = comp(2);
	}

	public static int comp(int a) {
	  if(a > 20) {
	    System.out.println("in");
	    a *= 2;
	  }
	  a++;
	  
	  if(a > 20) {
      System.out.println("in");
      a *= 2;
    } else {
      a *= 2;
      a *= 2;
    }
	  return a;
	}
}
