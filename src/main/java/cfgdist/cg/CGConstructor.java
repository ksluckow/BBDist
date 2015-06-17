package cfgdist.cg;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import soot.EntryPoints;
import soot.MethodOrMethodContext;
import soot.PhaseOptions;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.annotation.purity.DirectedCallGraph;
import soot.jimple.toolkits.annotation.purity.SootMethodFilter;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.PegCallGraphToDot;
import soot.options.Options;

/**
 * @author Kasper Luckow
 */
public abstract class CGConstructor {

  private static final Logger logger = Logger.getLogger(CHACGGenerator.class.getName());
  
  public CGConstructor(String[] applClasspaths) {
    StringBuilder sb = new StringBuilder();
    for(String cl : applClasspaths)
      sb.append(cl).append(File.pathSeparator);
    setupSoot(sb.toString());
  }
  
  public CGConstructor(String applClasspath) {
    setupSoot(applClasspath);
  }
  
  private void setupSoot(String applClasspath) {
    Properties p = System.getProperties();    
    String exPath = "";//p.getProperty("java.class.path");
    String lPath = p.getProperty("sun.boot.class.path");
    //exPath = exPath + File.pathSeparator;
    if(!applClasspath.equals(""))
      exPath += applClasspath + File.pathSeparator;
    String classpath = exPath + lPath;    
    String temppath = "/tmp"; //OS X might not like this

    Options.v().set_whole_program(true);
    Options.v().set_app(true);
    Options.v().set_verbose(false);
    Options.v().set_soot_classpath(classpath);
    Options.v().set_output_dir(temppath);
    Options.v().set_output_format(Options.output_format_none);
    Options.v().set_keep_line_number(true);
    Options.v().set_print_tags_in_output(true); 
    Options.v().setPhaseOption("jb", "use-original-names:true");
    PhaseOptions.v().setPhaseOption("jb", "use-original-names:true");      
  }
  
  public CallGraph getCallGraphFromJar(String jarFile) {
    //Load the jar and set as application classes
    Scene scene = Scene.v();
    Options.v().set_process_dir(Collections.singletonList(jarFile));
    Options.v().set_no_bodies_for_excluded(true);
    Options.v().set_allow_phantom_refs(true);
    scene.loadNecessaryClasses();
    
    //Define the entry points
    scene.setEntryPoints(EntryPoints.v().mainsOfApplicationClasses());
    for(SootMethod entry : EntryPoints.v().mainsOfApplicationClasses())
      logger.info(entry.getName());
    return getCallGraph();
  }
  
  public CallGraph getCallGraphFromClass(String className) {
    //Not sure why, but these options MUST be set before
    //obtaining the scene and loading the main class
    Options.v().set_no_bodies_for_excluded(true);
    Options.v().set_allow_phantom_refs(true);
    
    Scene scene = Scene.v();
    SootClass mainClass = scene.loadClassAndSupport(className);    
    mainClass.setApplicationClass();    
    scene.setMainClass(mainClass);

    scene.loadNecessaryClasses();
    List<SootMethod> entryPoints = EntryPoints.v().application();
    scene.setEntryPoints(entryPoints);
    for(SootMethod entry : EntryPoints.v().mainsOfApplicationClasses())
      logger.info(entry.getName());
    return getCallGraph();
  }
  
  protected abstract CallGraph getCallGraph();
}
