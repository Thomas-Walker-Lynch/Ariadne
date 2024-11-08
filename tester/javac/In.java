/*
  Import to the package, rather than to individual files.
*/
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;

import com.ReasoningTechnology.Ariadne.File;
import com.ReasoningTechnology.Ariadne.Util;
import com.ReasoningTechnology.Mosaic.IO;
import com.ReasoningTechnology.Mosaic.TestBench;


public class In {
    // Mosaic imports
    public static final Class<com.ReasoningTechnology.Mosaic.IO> MIO = com.ReasoningTechnology.Mosaic.IO.class;
    public static final Class<com.ReasoningTechnology.Mosaic.Util> MU = com.ReasoningTechnology.Mosaic.Util.class;
    public static final Class<com.ReasoningTechnology.Mosaic.TestBench> TB = com.ReasoningTechnology.Mosaic.TestBench.class;

    // Ariadne imports
    public static final Class<com.ReasoningTechnology.Ariadne.File> File = com.ReasoningTechnology.Ariadne.File.class;
    public static final Class<com.ReasoningTechnology.Ariadne.Util> AU = com.ReasoningTechnology.Ariadne.Util.class;
    public static final Class<com.ReasoningTechnology.Ariadne.Graph> Graph = com.ReasoningTechnology.Ariadne.Graph.class;
    public static final Class<com.ReasoningTechnology.Ariadne.Label> Label = com.ReasoningTechnology.Ariadne.Label.class;
    public static final Class<com.ReasoningTechnology.Ariadne.LabelList> LabelList = com.ReasoningTechnology.Ariadne.LabelList.class;
    public static final Class<com.ReasoningTechnology.Ariadne.Node> Node = com.ReasoningTechnology.Ariadne.Node.class;
    public static final Class<com.ReasoningTechnology.Ariadne.NodeList> NodeList = com.ReasoningTechnology.Ariadne.NodeList.class;
    public static final Class<com.ReasoningTechnology.Ariadne.ProductionList> ProductionList = com.ReasoningTechnology.Ariadne.ProductionList.class;
    public static final Class<com.ReasoningTechnology.Ariadne.Token> Token = com.ReasoningTechnology.Ariadne.Token.class;
    public static final Class<com.ReasoningTechnology.Ariadne.TokenSet> TokenSet = com.ReasoningTechnology.Ariadne.TokenSet.class;

    // Java standard library imports
    public static final Class<java.util.Arrays> Arrays = java.util.Arrays.class;
    public static final Class<java.nio.file.Files> Files = java.nio.file.Files.class;
    public static final Class<java.nio.file.Path> Path = java.nio.file.Path.class;
    public static final Class<java.nio.file.Paths> Paths = java.nio.file.Paths.class;
    public static final Class<java.nio.file.attribute.FileTime> FileTime = java.nio.file.attribute.FileTime.class;
    public static final Class<java.util.HashMap> HashMap = java.util.HashMap.class;
    public static final Class<java.util.List> List = java.util.List.class;
    public static final Class<java.util.Map> Map = java.util.Map.class;
}
/*

public class In {
    // Class references for direct access to imported class definitions
    public static final Class<?> MIO = com.ReasoningTechnology.Mosaic.IO.class;
    public static final Class<?> MU = com.ReasoningTechnology.Mosaic.Util.class;
    public static final Class<?> TestBench = com.ReasoningTechnology.Mosaic.TestBench.class;
    
    public static final Class<?> File = com.ReasoningTechnology.Ariadne.File.class;
    public static final Class<?> Graph = com.ReasoningTechnology.Ariadne.Graph.class;
    public static final Class<?> Label = com.ReasoningTechnology.Ariadne.Label.class;
    public static final Class<?> LabelList = com.ReasoningTechnology.Ariadne.LabelList.class;
    public static final Class<?> Node = com.ReasoningTechnology.Ariadne.Node.class;
    public static final Class<?> NodeList = com.ReasoningTechnology.Ariadne.NodeList.class;
    public static final Class<?> ProductionList = com.ReasoningTechnology.Ariadne.ProductionList.class;
    public static final Class<?> Token = com.ReasoningTechnology.Ariadne.Token.class;
    public static final Class<?> TokenSet = com.ReasoningTechnology.Ariadne.TokenSet.class;
    public static final Class<?> AU = com.ReasoningTechnology.Ariadne.Util.class;

    // For standard Java classes
    public static final Class<?> Arrays = java.util.Arrays.class;
    public static final Class<?> Files = java.nio.file.Files.class;
    public static final Class<?> Path = java.nio.file.Path.class;
    public static final Class<?> Paths = java.nio.file.Paths.class;
    public static final Class<?> FileTime = java.nio.file.attribute.FileTime.class;
    public static final Class<?> HashMap = java.util.HashMap.class;
    public static final Class<?> List = java.util.List.class;
    public static final Class<?> Map = java.util.Map.class;

    // Optional: Utility methods to create instances or perform actions on these classes could be added here
}
*/
