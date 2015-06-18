package examples.analysisdrivers;

import cfgdist.DistanceDB;
import cfgdist.InterproceduralDistanceAnalysis;

public class ProbRareEventAnalysis {

  public static void main(String[] args) {
    InterproceduralDistanceAnalysis analysis = new InterproceduralDistanceAnalysis("./build/classes/main", "examples.ProbRareEvent");
    DistanceDB db = analysis.computeDistance("examples.ProbRareEvent", "testMethod", 38, true);
  }
}
