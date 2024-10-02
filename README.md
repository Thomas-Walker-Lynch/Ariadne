Theseus volunteered to enter the inescapable maze designed by Daedalus to slay
the Minotaur and put an end to the Athenian sacrifices. Ariadne, fearing for his
life and desperate to help, thinks to give him a ball of string, which Theseus
then uses to trace his way out of the maze thus saving his life.

Building experimental code for GQL_to_Cypher has proven to be as complex as
navigating Daedalus's maze. The traditional build tool `make` does not
backward-chain pattern rules. Gradle/Groovy tends to build projects in
layers. However, in experimental code, it is often the case that
no complete layer will build without errors.

Ariandne will attempt to build all that is buildable, no matter where in 
a the dependency graph the buildable node is found, while leaving other
parts untouched.

To use the Ariadne build tool, a developer writes their dependency graph in
Groovy, utilizing a map and a set of regular expression matching
functions. Then, to build or clean up the project, the developer invokes
Ariadne. 

Ariadne was originally written in Groovy because it evolved incrementally as
part of a Gradle script. While there are plans to port the build tool to other
languages, Groovy has proven to be a practical language for this purpose, so this
is currently not a priority.  

Ariadne is a project in progress.

