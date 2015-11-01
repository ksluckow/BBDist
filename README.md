# Interprocedural Branch Distance Analysis #
Computes the shortest interprocedural distance between two basic blocks. The interprocedural CFG can be obtained using CHA or points-to analysis with Spark.

## Installation ##
The CFG distance analysis relies on soot. To obtain that dependency, please run `gradle prep` *before* compiling.

Then
* `gradle build` builds the target
* `gradle fatJar` creates a jar with dependencies (soot)

## Usage ##
Instantiate `InterproceduralDistanceAnalysis` and invoke `computeDistance`. This returns a `DistanceDB` object, that can be queried.

See examples in `cfgdist.examples`

## Issues ##
Does not support recursion yet.

## TODO ##

* Remove method parameter for the analysis -- it is superfluous. A target can uniquely be identified in terms of class and source line.
* Put examples in separate source folder
* fix recursion
* make analysis builder that allows to use the points-to analysis instead of the current CHA analysis for building the call graph
