package cfgdist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.toolkits.annotation.purity.DirectedCallGraph;
import soot.jimple.toolkits.annotation.purity.SootMethodFilter;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.thread.mhp.PegCallGraphToDot;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.util.dot.DotGraph;
import cfgdist.cg.CGConstructor;
import cfgdist.cg.CHACGGenerator;
import cfgdist.debug.CFGToDotDistance;

public class InterproceduralDistanceAnalysis {
  private final String classpath;
  private final String entryClass;
  private AnalysisCache cache;
  
  public InterproceduralDistanceAnalysis(String classpath, String entryClass) {
    this.classpath = classpath;
    this.entryClass = entryClass;
    this.cache = new AnalysisCache();
  }
  
  public DistanceDB computeDistance(
      String targetClass, 
      String targetMethod, 
      int targetSrcLine) {
    return computeDistance(targetClass, targetMethod, targetSrcLine, false);
  }
  
  public DistanceDB computeDistance(
                             String targetClass, 
                             String targetMethod, 
                             int targetSrcLine,
                             boolean debug) {
    
    CGConstructor cgCon = new CHACGGenerator(this.classpath);
    CallGraph cg = cgCon.getCallGraphFromClass(this.entryClass);
    
    if(debug)
      printCG(cg);

    SootClass cl = Scene.v().getSootClass(targetClass);
    SootMethod method = cl.getMethodByName(targetMethod);
    
    //Find the basic block containing the target
    BlockGraph bg = cache.getBlockGraph(method);
    Block tgtBlock = cache.getBlock(bg, targetSrcLine);
    
    //This is local to the cfg distance result.
    Map<Block, Integer> bl2dist = new HashMap<>();
    
    Stack<Block> callSiteWorkList = new Stack<>();
    
    //not strictly needed -- only for debug
    Set<SootMethod> methods = new HashSet<>();
    
    //Target has distance 0
    bl2dist.put(tgtBlock, 0);
    callSiteWorkList.push(tgtBlock);
    cache.addCallSite(tgtBlock, method);
    
    while(!callSiteWorkList.isEmpty()) {
      Block callSiteBlock = callSiteWorkList.pop();
      
      Stack<Block> blockWorklist = new Stack<>();
      blockWorklist.push(callSiteBlock);
      while(!blockWorklist.isEmpty()) {
        Block tgt = blockWorklist.pop();
        int nxtDist = bl2dist.get(tgt) + 1;
        for(Block pred : tgt.getPreds()) {
          if(!bl2dist.containsKey(pred)) {
            bl2dist.put(pred, nxtDist);
            blockWorklist.push(pred);
          } else {
            int predDist = bl2dist.get(pred);
            //update the distance if it is less than the current distance
            //TODO: could merge the two conditions (the bodies are the same) -- probably not readable though.
            if(predDist > nxtDist) {
              bl2dist.put(pred, nxtDist);
              blockWorklist.push(pred);
            }
          }
        }
      }
      SootMethod callSiteMethod = cache.getCallSiteMethod(callSiteBlock);
      
      //TODO: Be sure that this actually works
      int rootCount = bl2dist.get(bg.getHeads().get(0));

      methods.add(callSiteMethod);
      Iterator<Edge> inEdges =  cg.edgesInto(callSiteMethod);
      while(inEdges.hasNext()) {
        Edge inEdge = inEdges.next();
        SootMethod srcMethod = inEdge.src();
        Block targetBlock = cache.getCallSiteBlock(inEdge);
        cache.addCallSite(targetBlock, srcMethod);
        bl2dist.put(targetBlock, rootCount);
        callSiteWorkList.push(targetBlock);
      }
    }
    if(debug)
      printCFGsWithDistances(methods, bl2dist);
    return new DistanceDB(this.cache, bl2dist);
  }
  
  private void printCG(CallGraph cg) {
    DirectedCallGraph dircg = new DirectedCallGraph(cg, new SootMethodFilter() {
      @Override
      public boolean want(SootMethod arg0) {
        return true;
      }
    }, 
    cg.sourceMethods(), 
    true); 
    PegCallGraphToDot.toDotFile("file.dot", dircg, "Call Graph");
  }
  
  private void printCFGsWithDistances(Set<SootMethod> methods, Map<Block, Integer> bl2dist) {
    int id = 0;
    for(SootMethod meth : methods) {
      
      CFGToDotDistance cfgDot = new CFGToDotDistance();
      
      BlockGraph callsiteBlockGraph = cache.getBlockGraph(meth);
      
      DotGraph dotG = cfgDot.drawCFG(callsiteBlockGraph, null, bl2dist);
      dotG.plot("./cfg" + meth.getName() + "_" + id++ + ".dot"); 
    }
    for(Block b : bl2dist.keySet()) {
      System.out.println(b.getHead() + " dist: " + bl2dist.get(b));
    }
  }

}
