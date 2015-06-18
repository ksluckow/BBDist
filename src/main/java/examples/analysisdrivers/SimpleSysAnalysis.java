package examples.analysisdrivers;

import cfgdist.DistanceDB;
import cfgdist.InterproceduralDistanceAnalysis;

public class SimpleSysAnalysis {
  public static void main(String[] args) {
    InterproceduralDistanceAnalysis analysis = new InterproceduralDistanceAnalysis("./build/classes/main", "examples.SimpleSys");
    DistanceDB db = analysis.computeDistance("examples.SimpleSys$SubInner", "compute", 91, true);
  }
}
