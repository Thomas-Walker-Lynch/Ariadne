Ariadne is a project in progress.

Ariadne had an idea, and gave a ball of string to Theseus, so he could find his way
back out of Daedalus's maze after killing the Minotaur.

Building experimental code for GQL_to_Cypher has turned out to be such a
maze. `make` for example does not backwards chain pattern rules, and gradle/grovy
is predisposed to build up the project in layers, although no full layer
ever seems to be working at one time.

To use the 'Ariadne' build tool, a developer uses the groovy language to define
a dependency grpah, while using a map and a list of regular expression mathching
functions. Then to build, or cleanup, the project, the developer calls Ariadne.

Ariadne was originally written in Groovy because it was incrementally developed
as part of a gradle script. It is planned to port the build tool to addiitonal
languages; however groovy turns out to be a reasonable language, so this is not
pressing.


