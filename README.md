[ ![Codeship Status for luckow/CFGDistanceAnalysis](https://codeship.com/projects/2d8f16c0-4d1f-0133-8132-6e1cce453881/status?branch=master)](https://codeship.com/projects/106513)
# Interprocedural Branch Distance Analysis #
Computes the shortest interprocedural distance between two basic blocks. The interprocedural CFG can be obtained using CHA or points-to analysis with Spark.

## Installation ##
The CFG distance analysis relies on soot. To obtain that dependency, please run `gradle prep` *before* compiling.

Then
* `gradle build` builds the target
* `gradle fatJar` creates a jar with dependencies (soot)

## usage ##
Instantiate `InterproceduralDistanceAnalysis` and invoke `computeDistance`. This returns a `DistanceDB` object, that can be queried.

See examples in `cfgdist.examples`

## Issues ##
Does not support recursion yet.

## TODO ##

* Remove method parameter for the analysis -- it is superfluous. A target can uniquely be identified in terms of class and source line.
* Put examples in separate source folder
* fix recursion
* make analysis builder that allows to use the points-to analysis instead of the current CHA analysis for building the call graph
