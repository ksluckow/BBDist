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
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;

/**
 * @author Kasper Luckow
 */
public class PtsToCGGenerator extends CGConstructor {

  public PtsToCGGenerator(String[] applClasspaths) {
    super(applClasspaths);
  }
  
  public PtsToCGGenerator(String applClasspath) {
    super(applClasspath);   
  }
  
  @Override
  public CallGraph getCallGraph() {
    performPtsTo();
    CallGraph cg = Scene.v().getCallGraph();
    return cg;
  }

  private void performPtsTo() {
    HashMap<String,String> defaultOptions = new HashMap<String,String>();
    defaultOptions.put("enabled","true");
    defaultOptions.put("verbose","true");
    defaultOptions.put("ignore-types","false");          
    defaultOptions.put("force-gc","false");            
    defaultOptions.put("pre-jimplify","false");          
    defaultOptions.put("vta","false");                   
    defaultOptions.put("rta","false");                   
    defaultOptions.put("field-based","false");           
    defaultOptions.put("types-for-sites","false");        
    defaultOptions.put("merge-stringbuffer","true");   
    defaultOptions.put("string-constants","false");   
    defaultOptions.put("simple-edges-bidirectional","false");
    defaultOptions.put("on-fly-cg","true");            
    defaultOptions.put("simplify-offline","false");      // true
    defaultOptions.put("simplify-sccs","false");        
    defaultOptions.put("ignore-types-for-sccs","false");
    defaultOptions.put("propagator","worklist");
    defaultOptions.put("set-impl","double");
    defaultOptions.put("double-set-old","hybrid");         
    defaultOptions.put("double-set-new","hybrid");
    defaultOptions.put("dump-html","false");           
    defaultOptions.put("dump-pag","false");             
    defaultOptions.put("dump-solution","false");        
    defaultOptions.put("topo-sort","false");           
    defaultOptions.put("dump-types","true");             
    defaultOptions.put("class-method-var","true");     
    defaultOptions.put("dump-answer","false");          
    defaultOptions.put("add-tags","false");             
    defaultOptions.put("set-mass","false");   
    defaultOptions.put("trim-clinit","true");   
    defaultOptions.put("all-reachable","false");  

    // Set the following configurations to false may reduce safety, 
    // but dramatically improve performance
    defaultOptions.put("simulate-natives","true");       //false to increase speed    
    defaultOptions.put("implicit-entry","true");         //false to ignore implicit entries

    SparkTransformer.v().transform("",defaultOptions);
  }
}
