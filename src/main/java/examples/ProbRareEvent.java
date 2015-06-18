package examples;

public class ProbRareEvent {
  public static void main(String[] args) {
    testMethod(100);
  }
  
  public static void testMethod(int x) {
    /*if(Verify.getBoolean()) {
      if(x > 10) {
        System.out.println("success");
      } else
        assert false;
    } else {*/
      if(x < 10) {
        System.out.println("success1");
      } else if(x < 20) {
        System.out.println("success2");
      } else if(x < 30) {
        System.out.println("success3");
      } else if(x < 40) {
        System.out.println("success4");
      } else if(x < 50) {
        System.out.println("success5");
      } else if(x < 60) {
        System.out.println("success6");
      } else if(x < 70) {
        System.out.println("success7");
      } else if(x < 80) {
        System.out.println("success8");
      } else if(x < 90) {
        System.out.println("success9");
      } else if(x < 95) {
        System.out.println("success10");
      } else if(x < 98) {
        System.out.println("success11");
      } else
        assert false;
    //}
  }
}
