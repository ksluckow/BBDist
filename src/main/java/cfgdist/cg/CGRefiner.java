package cfgdist.cg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

/**
 * @author Kasper Luckow
 */
public class CGRefiner {

  public static CallGraph refine(CallGraph cg, String symbolicClass, String symbolicMethod) {
    return refine(cg, 0, symbolicClass, symbolicMethod);
  }

  public static CallGraph refine(CallGraph cg, int allowedLibDepth, String symbolicClass, String symbolicMethod) {
    allowedLibDepth--;
    CallGraph refinedCG = new CallGraph();

    //TODO: If we can construct the soot method signature,  Scene.v().getMethod(..) might be better
    SootClass cl = Scene.v().getSootClass(symbolicClass);

    //TODO: we do NOT support overloading atm
    SootMethod meth = cl.getMethodByName(symbolicMethod);
    Set<SootMethod> processed = new HashSet<SootMethod>();
    Map<SootMethod,Integer> m2depth = new HashMap<SootMethod, Integer>();
    Stack<SootMethod> stack = new Stack<SootMethod>();
    stack.add(meth);

    while(!stack.isEmpty()){
      SootMethod m = (SootMethod)stack.pop();

      boolean firstTime = processed.add(m);
      Integer depth = m2depth.get(m);
      if(depth==null){
        depth = 0;
        m2depth.put(m, 0);
      }
      depth = depth + 1;

      for(Iterator<Edge> it = cg.edgesOutOf(m);it.hasNext();){
        Edge e = it.next();     
        SootMethod tgt = e.tgt();

        if(firstTime){
          refinedCG.addEdge(e); 
        }

        if(isLibMethod(tgt)){           
          if(depth <= allowedLibDepth){
            Integer lastDepth = m2depth.get(tgt);

            // non passed, or this time depth is smaller
            if(lastDepth==null || depth<lastDepth){
              m2depth.put(tgt, depth);
              stack.push(tgt);
            }
          }
        }
        else{
          if(!processed.contains(tgt)){
            m2depth.put(tgt, 0);
            stack.push(tgt);              
          }
        }
      }
    }  
    return refinedCG;
  }

  private static boolean isLibMethod(SootMethod m){
    SootClass cls = m.getDeclaringClass();
    String className = cls.getName();
    int size = JAVA_LIB_PACKAGES.length;
    for(int i=0;i<size;i++){
      String pkg = JAVA_LIB_PACKAGES[i];
      if(className.startsWith(pkg)){
        return true;
      }
    }
    return false;      
  }

  private static String[] JAVA_LIB_PACKAGES = {
    "sun.", "sunw.", "com.sun.", "java.", "javax.", "jdk."
  };
}
