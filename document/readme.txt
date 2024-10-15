
Building a project can be like finding your way out of Daedalus's impossible to
solve maze. Ariadne hands you a ball of string.

Ariadne is a graph based build tool.

Each graph node represents either a build target or symbolic objective.

Nodes have properties, and among these properties is a block of code that
presumably either builds the target or accomplishes the symbolic objective.

Edges that link the nodes represent dependencies.

The build tool is given a graph. It traverses down to leaf dependencies then
follows the string it laid out during the traversal, and works its way back up
to achieve the root objectives.

The build tool recognizes and skips over cycles and build failures.

