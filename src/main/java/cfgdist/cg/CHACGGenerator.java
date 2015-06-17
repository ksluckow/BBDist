package cfgdist.cg;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import soot.EntryPoints;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;

/**
 * @author Kasper Luckow
 */
public class CHACGGenerator extends CGConstructor {

  public CHACGGenerator(String[] applClasspaths) {
    super(applClasspaths);
  }
  
  public CHACGGenerator(String applClasspath) {
    super(applClasspath);
  }

  @Override
  public CallGraph getCallGraph() {
    CHATransformer.v().transform();
    CallGraph cg = Scene.v().getCallGraph();
    return cg;
  }
}
