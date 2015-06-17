# CFGDistance #
Computes distance in terms of minimum number of branches to a target from an interprocedural analysis

## Installation ##
`gradle build` builds the target

`gradle fatJar` creates a jar with dependencies (soot)

## usage ##
Instantiate `InterproceduralDistanceAnalysis` and invoke `computeDistance`. This returns a `DistanceDB` object, that can be queried.

See examples in `cfgdist.examples`

## issues ##
Does not support recursion yet..

## TODO ##

* Put examples in separate source folder
* fix recursion
* make analysis builder that allows to use the points-to analysis instead of the current CHA analysis for building the call graph


