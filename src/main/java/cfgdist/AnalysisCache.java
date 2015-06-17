package cfgdist;

import java.util.HashMap;
import java.util.Map;

import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;

public class AnalysisCache {
  private Map<SootMethod, BlockGraph> meth2blockgraph;
  private Map<Block, SootMethod> callsite2Method;
  private Map<Unit, Block> unit2block;
  private Map<Integer, Block> srcLine2block;
  
  public AnalysisCache() {
    this.callsite2Method = new HashMap<>();
    this.meth2blockgraph = new HashMap<>();
    this.unit2block = new HashMap<>();
    this.srcLine2block = new HashMap<>();
  }
  
  public Unit getUnit(SootMethod method, int sourceline) {
    Body bd = method.getActiveBody();
    for(Unit u : bd.getUnits()) {
      if(sourceline == u.getJavaSourceStartLineNumber()) {
        return u;
      }
    }
    return null;
  }
  
  public Block getBlock(BlockGraph bg, int sourceline) {
    Block b = srcLine2block.get(sourceline);
    if(b != null)
      return b;
    Body bd = bg.getBody();
    for(Unit u : bd.getUnits()) {
      if(sourceline == u.getJavaSourceStartLineNumber()) {
        b = getBlock(bg, u);
        srcLine2block.put(sourceline, b);
        return b;
      }
    }
    return null;
  }
  
  public Block getBlock(BlockGraph blockGraph, Unit unit) {
    Block bl = this.unit2block.get(unit);
    if(bl != null)
      return bl;
    for(Block b : blockGraph) {
      for(Unit u : b) {
        if(u.equals(unit)) {
          unit2block.put(unit, bl);
          return b;
        }
      }
    }
    return null;
  }
  
  public BlockGraph getBlockGraph(SootMethod method) {
    BlockGraph bg = this.meth2blockgraph.get(method);
    if(bg != null)
      return bg;
    Body bd = method.getActiveBody();
    bg = new ExceptionalBlockGraph(bd);
    this.meth2blockgraph.put(method, bg);
    return bg;
  }
  
  public Block getCallSiteBlock(Edge inEdge) {
    Unit callSiteUnit = inEdge.srcUnit();
    SootMethod srcMethod = inEdge.src();
    BlockGraph blockGraph = getBlockGraph(srcMethod);
    return getBlock(blockGraph, callSiteUnit);
  }
  
  public void addCallSite(Block callSiteBlock, SootMethod method) {
    callsite2Method.put(callSiteBlock, method);
  }
  
  public SootMethod getCallSiteMethod(Block callSiteBlock) {
    return callsite2Method.get(callSiteBlock);
  }
}
