package cfgdist;

import java.util.Map;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;

public class DistanceDB {

  private AnalysisCache cache;
  
  //actual analysis result
  private final Map<Block, Integer> bl2dist;
  
  public DistanceDB(AnalysisCache cache,
      Map<Block, Integer> bl2dist) {
    this.cache = cache; 
    this.bl2dist = bl2dist;
  }
  
  public int getDistance(String clazz, String method, int srcLine) {
    SootClass cl = Scene.v().getSootClass(clazz);
    try {
      SootMethod sootMethod = cl.getMethodByName(method);
      
      //Find the basic block containing the target
      BlockGraph bg = cache.getBlockGraph(sootMethod);
      Block tgtBlock = cache.getBlock(bg, srcLine);
      
      if(bl2dist.containsKey(tgtBlock))
        return bl2dist.get(tgtBlock);
      return Integer.MAX_VALUE;
    } catch (RuntimeException e) { //getMethodByName may throw a runtime exception if the class is a phantom
      return 0; //this happens for instance when dealing with jpf lib methods e.g. Verify.* -- these should not contribute to the distance!
    }
  }
}
