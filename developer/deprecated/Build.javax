import java.util.List;

public class Build {

    // Function to load the graph class dynamically
    public static Class<?> includeAClass(String aClassFp) {
        ClassLoader classLoader = Build.class.getClassLoader();
        String className = aClassFp.replace('/', '.').replace(".class", "");
        try {
            return classLoader.loadClass(className);
        } catch (Exception e) {
            System.out.println("Error loading class '" + className + "': " + e.getMessage());
            return null;
        }
    }

    // Build function
    public static void build(String graphDefinitionFp, List<String> rootNodeLabels) {
        // Print summary of what we are doing
        System.out.println("build:: Building targets for graph '" + graphDefinitionFp + ".class'");
        if (rootNodeLabels.isEmpty()) {
            System.out.println("No build targets specified. Please provide root node labels to build.");
            System.exit(0);
        }
        System.out.println("Building targets: " + String.join(", ", rootNodeLabels));

        // Load the dependency graph class from arg[1]
        Class<?> graphDefinitionClass = includeAClass(graphDefinitionFp);
        if (graphDefinitionClass != null) {
            System.out.println("build:: loaded " + graphDefinitionFp + ".class");
        } else {
            System.out.println("build:: failed to load " + graphDefinitionFp + ".class");
            System.exit(1);
        }

        // Get the node_map and node_f_list from the graph class
        // Assuming these methods are static and return the appropriate types
        // Replace with actual method calls if they are different
        Object nodeMap = null;
        Object nodeFList = null;
        try {
            nodeMap = graphDefinitionClass.getMethod("getNodeMap").invoke(null);
            nodeFList = graphDefinitionClass.getMethod("getNodeFList").invoke(null);
        } catch (Exception e) {
            System.out.println("Error invoking methods on graphDefinitionClass: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("node_map: " + nodeMap);
        System.out.println("node_f_list: " + nodeFList);

        // Create an instance of AriadneGraph, and run the build scripts
        // Assuming AriadneGraph has a constructor that takes nodeMap and nodeFList
        // Replace with actual constructor call if it is different
        try {
            Class<?> ariadneGraphClass = Class.forName("AriadneGraph");
            Object graph = ariadneGraphClass.getConstructor(nodeMap.getClass(), nodeFList.getClass()).newInstance(nodeMap, nodeFList);
            ariadneGraphClass.getMethod("runBuildScriptsF", List.class).invoke(graph, rootNodeLabels);
        } catch (Exception e) {
            System.out.println("Error creating or invoking AriadneGraph: " + e.getMessage());
            System.exit(1);
        }
    }

    // Entry point when run as a script
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ./build <graph_definition.class> [root_node_labels...]");
            System.exit(1);
        }

        // Get graph definition file and root node labels
        String graphDefinitionFp = args[0];
        List<String> rootNodeLabels = args.length > 1 ? List.of(args).subList(1, args.length) : List.of();
        build(graphDefinitionFp, rootNodeLabels);
    }
}
