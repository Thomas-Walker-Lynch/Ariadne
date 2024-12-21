
--------------------------------------------------------------------------------
Documents

Note the directories: Ariadne/document🖉, Ariadne/developer/document🖉, and Ariadne/tester/document🖉. Also not documents in the RT-project-share/document🖉 directory.

--------------------------------------------------------------------------------
Ariadne

Ariadne is a graph based build tool.

When you are tasked with a project build that is as complex as Daedalus's impossible to solve maze, modern Ariadne hands you a piece of chalk to draw arrows with. It works even better than a ball of string.

A tool like Ariadne is useful for projects that have compiled languages, where there are many steps in getting from source code to a loadable set of instructions. At is able to handle complex cases with generation of sources from ANTLR grammars, and the implied dependencies of Java programs.

Ariadne can also be used to describe dependencies on libraries or other programs used by a given program.  This is useful with both compiled and interpreted languages.

In the Java implementation of Ariadne, everything is coded in Java, including the developer's description of the dependency graph. 

If a developer were to compare `Ariadne` with `make`, the file corresponding to the 'make file' is a dynamically loaded Java program known as the 'graph definition'.  Whereas a make file for a complex project will contain a lot of shell code, the Ariadne graph definition is all Java code. Where a make file has 'pattern rules' Ariadne will typically has regular expressions. Make can not backwards chain through pattern rules, Ariadne can. 

Ariadne is more procedural than Maven, which is more descriptive. Ariadne allows for finer grain description of dependencies and finer grain decision making than with Gradle task dependencies.

--------------------------------------------------------------------------------
General Comments about Graphs

Conceptually a graph is a set with two members, nodes and edges, both also being sets. In turn, each edge is a pair of nodes. In a directed graph, one of the nodes of such a pair is said to be the tail, while the other is said to be the head.  

A leaf node is unusual in that it is not the tail of any edge.

When a node is selected, a set is implied that contains each and every edge that has the selected node as its tail, if any. These are the outgoing edges for the selected node.  Each outgoing edge then has a head. We say that the set of head nodes are distance one from the selected node. (The selected node itself is at distance zero.) Distance one nodes are said to be 'neighbor' nodes.

Deterministic traversal is an algorithm that is given two arguments when it is started, a graph definition, and a start node. The start node is then assigned to a state variable. At each step of the algorithm, the node in the state variable is selected on the graph, then the state variable is re-assigned to be one of the distance one nodes.

Deterministic search for a node is an example of an algorithm that makes use of traversal.  Deterministic search begins with a graph, a start node, a predicate that is said to recognize the searched for node, and a function that guides the traversal.

A self cycle in a graph consists of a node and an edge, where said edge has the node as both its tail and its head. There is no change in the traversal state variable when taking a traversal step through such an edge.  In general, a cycle consists of a start node and all nodes and edges traversed up until arriving back at said start node.

It is a bad thing to have cycles in a build graph, as cycles will correspond to circular dependencies.  Hence Ariadne looks for these and reports them.

--------------------------------------------------------------------------------
The Ariadne Build Graph

An Ariadne graph definition contains a 'graph definition function' list.  A graph definition function is given a node label, and returns a node, or null if it can not find a node with such a label. A common graph definition function contains a map where each entry is a node label and a node.

An Ariadne node is dictionary that maps a string key to a value. The type of the value is implied by the string key. Depending on the Ariadne tool being used, each node map will typically have entries for the keys "label", "build", and "neighbor".

A "label" value is a string that is unique among the nodes in the graph. The "build" value is a function that a build tool will call when a file corresponding to a node is to be built.  The "neighbor" value is a list of neighbor nodes.

The Ariadne graph definition is optimized to be a run time data structure used for traversal, so the neighbor list is held directly in the node. Also, there is only one edge property, that of 'dependency'. As a consequence, there is no need for a separate edge table.

--------------------------------------------------------------------------------
Ariadne Build Tool

For the default Ariadne build tool, each node label is either symbolic, or is a file path. The tool descends into the graph, based on file modification dates, and builds nodes that are modified less recently than their dependencies.

--------------------------------------------------------------------------------
For Developers/Testers

The project has three entry points, one for each project role: developer, tester, and administrator. To enter the project, source the environment appropriate for the role, either `env_developer`, `env_tester`, or `env_administrator`.

1. Production 

  1.1. development

  cd Ariadne
  . env_developer

  [do work]

  make
  release

  1.2 regression testing

  cd Ariadne
  . env_tester
  make
  run

2. Debugging

  cd Ariadne
  . env_developer
  make
  # puts links to source files on the `scratchpad`
  gather_source_links 

  [change to tester module]

  cd Ariadne
  # the developer parameter links in the actual source in the developer module
  . env_tester developer
  make
  run

The `make` command you see above is a bash script. Version 1.0 of Ariadne uses a direct 'build it all every time' approach. Perhaps in version 2.0 or so, we will use a prior version of Ariadne for the build environment.

In IntelliJ IDEA there are two modules, developer and tester. The output for each module is 'scratchpad' tests are added through the `run edit-configuration` menu. See tool_shared/third_party/document🖉 directory for more information.

Using Ariadne
-------------

After it is built and released the tool will appear in the Ariadne/release directory where it can be regression tested or imported into another project.

