package examples;


/**
 * random example...
 *
 */
public class SimpleSys {
	
	public static void main(String[] args) {
		SimpleSys s = new SimpleSys();
		int val = s.compAB(2, 2);
		if(val > 0) {
		  val++;
		} else
		  val *=2;
	}

	public int compAB(int a, int b) {
	  Inner instan;
		if(a > b) {
		  if(a < b) {
		    instan = new Inner(); //instantiate Inner class here
        b += number(b);
      }  else
        instan = new SubInner();
		  b += 300;
		  instan.compute(a); //We don't know statically which compute method is being invoked
		  if(a > 300) {
		    b += number(b) + calc(12);
		  } else {
		    if(a  < 5) {
		      int i = 0, c=4;
		      while(i < a) {
		        c*=2;
		        i++;
		      }
		    }
		    b += 300;
		    instan.compute(a); //We don't know statically which compute method is being invoked
		  }
		  
		  return 200;
		}
		else
		  return 2* a;
	}
	
	public int number(int a) {
		if(a == 200) {
		  System.out.println("in number()");
		  return calc(2);
		} else {
		  System.out.println("in number()");
		  return calc(2);
		}
	}
	
	 public int calc(int a) {
	    if(a == 200) {
	      System.out.println("in" + "calc()");
	      return 2;
	    } else {
	      System.out.println("in" + "calc()");
	      return 9;
	    }
	  }
	 
	 public class Inner {
	   public Inner() {
	     
	   }
	   
	   public int compute(int i) {
	     while(i < 2) {
	       System.out.println(i++);
	     }
	     System.out.println("inner.compute");
	     return i*2/8;
	   }
	 }
	 
   public class SubInner extends Inner {
     public SubInner() {
       
     }
     
     @Override
     public int compute(int i) {
       if(i > 2)
         return 2; //target
       else
         return i*2/8;
     }
   }
}
