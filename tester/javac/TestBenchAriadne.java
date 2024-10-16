package com.ReasoningTechnology.Ariadne.TestBench;
import  com.ReasoningTechnology.Ariadne.*;
import  com.ReasoningTechnology.TestBench.*;
import java.util.Map;
import java.util.HashMap;

public class TestBenchAriadne extends TestBench{

  public static boolean test_File_unpack_file_path_0(){
    boolean[] conditions = new boolean[5];
    int i = 0;
        
    // Test input
    String test_fp = "/home/user/test.txt";
        
    // Expected output
    String expected_dp = "/home/user/";
    String expected_fn = "test.txt";
    String expected_fn_base = "test";
    String expected_fn_ext = "txt";

    // Actual output
    Map<String, String> result = File.unpack_file_path( test_fp );
        
    conditions[i++] = result.get("dp").equals( expected_dp );
    conditions[i++] = result.get("fn").equals( expected_fn );
    conditions[i++] = result.get("fn_base").equals( expected_fn_base );
    conditions[i++] = result.get("fn_ext").equals( expected_fn_ext );
    conditions[i++] = result.size() == 4;

    // Return true if all conditions are met
    return all( conditions );
  }

  // Method to run all tests
  public static void test_Ariadne(){
    Map<String, Boolean> test_map = new HashMap<>();

    // Adding tests to the map
    test_map.put( "File_unpack_file_path_0", test_File_unpack_file_path_0() );

    // Run the tests using TestBench
    TestBench.run( test_map );
  }

  // Main function to provide a shell interface for running tests
  public static void main(String[] args){
    System.out.println("Running Ariadne tests...");
    test_Ariadne(); // Calls the method to run all tests
  }

}

