package examples.analysisdrivers;

import cfgdist.DistanceDB;
import cfgdist.InterproceduralDistanceAnalysis;

public class SingleBranchAnalysis {
  public static void main(String[] args) {
    InterproceduralDistanceAnalysis analysis = new InterproceduralDistanceAnalysis("./build/classes/main", "examples.SingleBranch");
    DistanceDB db1 = analysis.computeDistance("examples.SingleBranch", "comp",27, true);
    DistanceDB db2 = analysis.computeDistance("examples.SingleBranch", "comp",29, true);
    
    
    int distance1 = db1.getDistance("examples.SingleBranch", "comp", 16);
    System.out.println("distance db1 " + distance1);
    
    int distance2 = db2.getDistance("examples.SingleBranch", "comp", 16);
    System.out.println("distance db2 " + distance2);
  }
}
