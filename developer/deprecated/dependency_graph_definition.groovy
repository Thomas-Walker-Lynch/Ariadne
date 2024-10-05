/*
 Here the developer describes the build environment and the graph of
 dependencies between files to be built.

 Each file is given a 'node'.  The node label is a path
 to the file, existing or not. Node's can also be 'symbolic'
 in that the label does not correspond to a file.

 File paths are relative to the developer's directory.  Also
 known as $DEVELOPER_HOME.

 The program that processes this file is
 `executor/build_from_dependency_graph.groovy`.  There are more notes and
 definitions there.
*/


/*--------------------------------------------------------------------------------
 The build environment

 Each tool has its '_IN' and '_OUT' directories that are referenced in
 their build scripts. This keeps references in the build script local.
 (Flow of files between tools can be seen in the overlapping definitions
 among the various _IN and _OUT directories.)

 These suffixes are sometimes used to eliminate ambiguity:

 _FN  = File Name
 _FP  = File Path (last element of the FP is the FN)
 _DN  = Directory Name
 _DP  = Directory path (last element of the DP is the DN)

 _FNL = File Name list
 _FPL = File Path List 
 _DNL = Directory path list
 _DPL = Directory path list

 Files in a _LEAF directory can not be built, and should never be deleted by the tools.

*/

def env = [:]

// Required shell environment variables
def varName_List = [
  'REPO_HOME'
  ,'PROJECT'
  ,'ENV_BUILD_VERSION'
  ,'DEVELOPER_HOME'
]
varName_List.each { varName ->
  env[varName] = System.getenv(varName) ?: ""
}

// Optional shell environment variables
def varNameOptional_List = [
  'CLASSPATH'
]
varNameOptional_List.each { varName ->
  env[varName] = System.getenv(varName) ?: ""
}

env.CLASSPATH += ":${env.ANTLR_JAR}"

// tools used in build scripts
def JAVA_COMP_FP = "${env.JAVA_HOME}/bin/javac"   // Java compiler path
def JAVA_INTERP_FP = "${env.JAVA_HOME}/bin/java"  // Java interpreter path
def JAVA_ARCHIVE_FP = "${env.JAVA_HOME}/bin/jar"  // Java archive tool path

def dir_map = [
  'ANTLR_IN_LEAF'       : 'ANTLR/'
  ,'ANTLR_OUT'           : 'javac/ANTLR/'
  ,'ANTLR_OUT_PARENT'    : 'javac/' // required by the antlr command to be a base for OUT
  ,'EXECUTOR_IN'         : 'executor/'
  ,'JAVA_COMP_IN'        : 'javac/'
  ,'JAVA_COMP_IN_LEAF': 'javac/leaf/'
  ,'JAVA_COMP_IN_ANTLR'  : 'javac/ANTLR/'
  ,'JAVA_COMP_IN_SYN'    : 'javac/synthesized/'
  ,'JAVA_COMP_OUT'       : 'jvm/'
  ,'JVM_IN'              : 'jvm/'
  ,'TEMP'                : 'scratch_pad/'
]

env.CLASSPATH += ":${dir_map.JVM_IN}"

dir_map.JAVA_COMP_IN_LIST = 
  "${dir_map.JAVA_COMP_IN_LEAF}" 
  +":${dir_map.JAVA_COMP_IN_ANTLR}" 
  +":${dir_map.JAVA_COMP_IN_SYN}"

//--------------------------------------------------------------------------------
// required environment variable check.  Will add additional preface checks here
// should they become available.
//

// a helper function for `environment_check`
def print_missing_var_list(missing_var_list){
  if(missing_var_list.isEmpty()){
    // Print nothing if the list is empty
    return
  } else if(missing_var_list.size() == 1){
    println "This environment variable was not set: ${missing_var_list[0]}"
  } else {
    println "These environment variables were not set: ${missing_var_list.join(' ,')}"
  }
}

task environment_check {
  dependsOn ':installTools'
  doFirst {

    println "CLASSPATH: ${env.CLASSPATH}"
    println "JAVA_COMP_IN_LIST: ${dir_map.JAVA_COMP_IN_LIST}"
    
    // did the required variables have definitions?
    def error_missing = false
    def error_project = false
    def missing_var_list = [] // To collect missing environment variables
    varName_List.each { varName ->
      if(!env[varName]){
        error_missing = true
        missing_var_list << varName
      }
    }
    print_missing_var_list(missing_var_list)
    
    // did the required variables have definitions?
    if(env.PROJECT != "GQL_to_Cypher"){
      error_project = true
      println "Expected project 'GQL_to_Cypher' ,but found '${env.PROJECT}'."
    }
    if(error_missing || error_project){
      throw new GradleException("Bailing due to missing environment variables.")
    }
  }
  doLast {
    println "================================================================================"
    println "Building project .."
  }
}

/*--------------------------------------------------------------------------------
  Map keyed on label node definitions

*/

def node_map = [

  "all" : [
    ,type: "symbol"
    ,neighbor: [
      "ANTLR_OUT_FL"
      ,"RuleNameList"
      ,"RuleNameListRegx"
      ,"Synthesize_SyntaxAnnotate"
      ,"Synthesize_SyntaxAnnotate_PrintVisitor"
      ,"Synthesize_SyntaxAnnotate_PrintVisitorMethod"
    ]
  ]

  "ANTLR_OUT_FL" : [
    ,type: "symbol"
    ,neighbor: ["${dir_map.EXECUTOR_IN}/ANTLR_OUT_FL"]
  ]

  ,"RuleNameList" : [
    ,type: "symbol"
    ,neighbor: ["${dir_map.EXECUTOR_IN}/RuleNameList"]
  ]

  ,"RuleNameListRegx" : [
    ,type: "symbol"
    ,neighbor: ["${dir_map.EXECUTOR_IN}/RuleNameListRegx"]
  ]

  ,"Synthesize_SyntaxAnnotate" : [
    ,type: "symbol"
    ,neighbor: [
      "${dir_map.JAVA_COMP_IN_LEAF}/StringUtils.java"
      ,"${dir_map.EXECUTOR_IN}/Synthesize_SyntaxAnnotate"
    ]
  ]

  "Synthesize_SyntaxAnnotate.class" : [
    type: 'path' , // It's a path type node
    ,neighbor: [
      "${dir_map.JAVA_COMP_IN_LEAF}/Synthesize_SyntaxAnnotate.java" , // Dependency
      "${dir_map.JAVA_COMP_IN_LEAF}/StringUtils.java"  // Dependency
    ]
    ,build: { node ,neighbor ->
      def javac_cmd = "${JAVA_COMP_FP} -d ${dir_map.JAVA_COMP_OUT} ${neighbor.join(' ')}"
      javac_cmd.execute().waitFor()
    }
  ]

  ,"Synthesize_SyntaxAnnotate_PrintVisitor" : [
    ,type: "symbol"
    ,neighbor: [
      "${dir_map.JAVA_COMP_IN_LEAF}/StringUtils.java"
      ,"${dir_map.JAVA_COMP_IN_LEAF}/Synthesize_SyntaxAnnotate_PrintVisitorMethod.java"
      ,"${dir_map.EXECUTOR_IN}/Synthesize_SyntaxAnnotate_PrintVisitor"
    ]
  ]

  ,"Synthesize_SyntaxAnnotate_PrintVisitorMethod" : [
    ,type: "symbol"
    ,neighbor: [
      "${dir_map.JAVA_COMP_IN_LEAF}/StringUtils.java"
      ,"${dir_map.EXECUTOR_IN}/Synthesize_SyntaxAnnotate_PrintVisitorMethod"
    ]
  ]
]



//--------------------------------------------------------------------------------
// node making functions
//

// javac/leaf/<x> returns leaf node for javac/leaf/<x>
def node_leaf_f(node_label) {
  def leafNodePattern = ~/${dir_map['JAVA_COMP_IN_LEAF']}(.*)/
  def match = node_label =~ leafNodePattern
  if (!match) {
    return [status: "no_match"]
  }
  def baseName = match[0][1]

  def leafFilePath = "${dir_map['JAVA_COMP_IN_LEAF']}${baseName}"
  def leafFile = new File(leafFilePath)
  if (!leafFile.exists()) {
    return [status: "no_match"]
  }

  return [
    status: "matched"
    ,label: node_label
    ,type: "leaf"
    ,neighbor: []
  ]
}

// given executor/<x> returns node to build script wrapping jvm/<x>.jar
def node_executor_f(node) {

  def match = node =~ /^(executor\/)(${base})$/
  if (!match) {
    return [status: "no_match"]
  }
  def baseName = match[0][2]

  def jarFilePath = "${dir_map['JVM_IN']}${baseName}.jar"
  def wrapperFilePath = "${dir_map['EXECUTOR_IN']}${baseName}"

  def jarFile = new File(jarFilePath)
  if (!jarFile.exists()) {
    return [status: "no_match"]
  }

  return [
    status: "matched"
    ,label: node
    ,type: "path"
    ,neighbor: [jarFilePath]
    ,must_have: [jarFilePath]
    ,build: { node ,neighbor -> 

      // The script for wrapping the jar file:
      def wrapper =
      """
      #!/usr/bin/env bash
      ${dir_map['JAVA_INTERP']} -cp \${CLASSPATH}:${dir_map['JVM_IN']}:${dir_map['JVM_IN']}/${baseName}.jar ${baseName} \\\$\\@
      """
      
      new File(wrapperFilePath).withWriter('UTF-8') { writer ->
        writer.write(wrapper)
      }

      println "Creating executable wrapper script for ${baseName} in executor directory."
      "chmod +x ${wrapperFilePath}".execute().text
    }
  ]
}

// given javac/ANTLR/<x>.java, returns node to build grammar <x>.g4
def node_grammar_f(node) {

  def match = node =~ /^(${dir_map['ANTLR_OUT']})(${base})(Lexer|Parser|BaseListener|Listener|BaseVisitor|Visitor)\.java$/
  if( !match ){
    return [status: "no_match"]
  }

  def grammarName = match[0][2]
  def outputType = match[0][3]

  def grammarFilePath = "${dir_map['ANTLR_IN_LEAF']}${grammarName}.g4"
  def grammarFile = new File(grammarFilePath)

  if( !grammarFile.exists() ){
    return [status: "no_match"]
  }

  return [
    status: "matched"
    ,label: node
    ,type: "path"
    ,neighbor: [grammarFilePath]
  ]
}

// given <x>.class returns node to build <x>.class from <x>.java
def node_class_f(node) {

  def match = node =~ /^(${dir_map['JAVA_COMP_OUT']})(${base})\.class$/
  if( !match ){
    return [status: "no_match"]
  }

  def baseName = match[0][2]
  def javaFilePath = "${dir_map['JAVA_COMP_IN_PRIMARY_DIR']}/${baseName}.java"
  def javaFile = new File(javaFilePath)

  if( !javaFile.exists() ){
    return [status: "no_match"]
  }

  return [
    status: "matched",
    label: node,
    type: "path",  // It's a path node since we're building the .class file
    neighbor: [javaFilePath],  // The corresponding .java file
    build: { node, neighbor ->
        def javac_cmd = "${JAVA_COMP_FP} -d ${dir_map.JAVA_COMP_OUT} -sourcepath ${dir_map.JAVA_COMP_IN_DL} ${neighbor[0]}"
        javac_cmd.execute().waitFor()
    }
  ]
}

// given <x>.jar returns node to build <x>.jar from <x>.class
def node_jar_f(node) {

  // Use the symbolic name and base patterns
  def match = node =~ /^(${dir_map['JAVA_COMP_OUT']})(${base})\.jar$/

  if( !match ){
    return [status: "no_match"]
  }

  def baseName = match[0][2]
  def classFilePath = "${dir_map['JAVA_COMP_OUT']}${baseName}.class"
  def classFile = new File(classFilePath)

  if( !classFile.exists() ){
    return [status: "no_match"]
  }

  return [
    status: "matched"
    ,label: node
    ,type: "path"
    ,neighbor: [classFilePath]
    ,build: { node ,neighbor ->
      println "Building jar for ${baseName}"
      def command = "${ext.javaHome}/bin/jar cf ${baseName}.jar -C ${dir_map['JAVA_COMP_OUT']} ${baseName}.class"
      return command.execute().text;
    }
  ]
}

// list of the recognizer functions
def node_f_list = [
  node_leaf_f
  ,node_executor_f
  ,node_grammar_f
  ,node_class_f
  ,node_jar_f
]

// comprehensions to make regexprs more readable
def base = "[a-zA-Z0-9_-]+"
def ext = "[a-zA-Z0-9_-]+$"
def name = "${base}\\.${ext}"
def path = ".+/${name}"



// LocalWords:  wellformed
