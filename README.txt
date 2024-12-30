
--------------------------------------------------------------------------------
Documents

Note the directories: Ariadne/document🖉, Ariadne/developer/document🖉, and Ariadne/tester/document🖉. Also not documents in the RT-project-share/document🖉 directory.

--------------------------------------------------------------------------------
Ariadne

When you are tasked with a project build that is as complex as Daedalus's impossible to solve maze, modern Ariadne hands you a piece of chalk to draw arrows with. She has had a lot of time to think about it, and chalk works even better than a ball of string.

Ariadne is an directed graph based build tool. The tool and its configuration file are written in Java.  We don't brag about this, rather this is the environment for which it came to fruition.  Other language versions will surely follow, hence the source code directory is called javac🖉 and it builds to a scratchpad directory.

Ariadne was developed to be used in a large project research environment where code is synthesized, not all of the code is working at any one time, and there are many steps for getting from authored content to loadable machine code.

Ariadne will also be helpful for compiled language projects, such as C, C++.  Ariadne can also be used to describe dependencies on libraries or other programs so it will be useful with interpreted languages also.

If a developer were to compare `Ariadne` with `make`, the file corresponding to the 'make file' is a dynamically loaded Java program known as the 'graph definition'.  Whereas a make file for a complex project will contain a lot of shell code, the Ariadne graph definition is all Java code. Where a make file has 'pattern rules' Ariadne will typically has regular expressions. Make can not backwards chain through pattern rules, Ariadne can. 

Ariadne is more procedural than Maven, which is more descriptive. Ariadne allows for finer grain description of dependencies and finer grain decision making than with Gradle task dependencies.

--------------------------------------------------------------------------------
See also:

~RT-project-share/documents🖉  for information about RT projects.
~Ariadne/documents🖉  for more general information about Ariadne
~Ariadne/developer/documents🖉  specific information about the code
~Ariadne/tester/documents🖉  specific information about the tests
