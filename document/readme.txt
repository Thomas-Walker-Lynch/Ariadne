
--------------------------------------------------------------------------------
Ariadne

Ariadne is a graph based build tool.

Tying to build a project is a lot like trying to find your way out of
Daedalus's impossible to solve maze. Ariadne hands you a ball of string.

--------------------------------------------------------------------------------
Documents

Note the directories:  Ariadne/document, Ariadne/developer/document, 
and Ariadne/tester/document.


--------------------------------------------------------------------------------
How It Works

Everything is coded in Java, including the developers description of the
dependency graph.

A graph is made of nodes and edges.

A node is dictionary. Each node has a 'label', a 'build' function, and a
'neighbor' list.  The neighbor list holds the edges.

Using Java, the developer puts the nodes in a map, keyed on the node label, or
writes functions that when given a label, return either a a node or null.

A node map looks a lot like classic make file.  Each node label is a target file
path, the neighbor list can be listed next, and then we have the build code.

Function defined nodes are very flexible. They are more powerful, and more
flexible than make's pattern rules.  If a developer wants a function to act like
a pattern rule, the developer can use a regular expression match on the given
label. However, unlike make, pattern rules defined in this manner, will
backwards chain potentially through other pattern rules.

The Build function is one of the graph traversal programs. It is given a graph
and a list of top level market targets. It traverses the graph to find leaf
while unwinding its proverbial ball of string. Upon finding leaf nodes, it then
follows the string back until building the targets.

It is possible to define other graph traversal functions.  In fact, Ariadne can
be used to do anything where a directed graph of functions is useful.


--------------------------------------------------------------------------------
Entering the Project

The project has three entry points, one for each project role: developer,
tester, and administrator. To enter the project, source the environment
appropriate for the role, either `env_developer`, `env_tester`, or
`env_administrator`.

For example, to work on the code as a developer, start a fresh shell and:

> cd Ariadne
> . env_developer
>  <run your IDE, I run emacs>

[do work]

> make
> release

[change to tester role]

The `make` command you see above is a bash script. Version 1.0 of Ariadne uses a
direct 'build it all every time' approach. Perhaps in version 2.0 or so, we will
use a prior version of Ariadne for the build environment.


Using Ariadne
-------------

After it is built and released the tool will appear in the Ariadne/release
directory where it can be tested or imported into another project.

