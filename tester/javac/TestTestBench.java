package com.ReasoningTechnology.TestBench;

/*
Component smoke test. At least call each method of each class.

*/

import com.ReasoningTechnology.Ariadne.*;
import com.ReasoningTechnology.TestBench.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class TestTestBench extends TestBench{

  public static class TestSuite{

    TestSuite(){
    }

    public boolean test_pass(ByteArrayOutputStream out_content, ByteArrayOutputStream err_content){
      return true;
    }

    public boolean test_fail_0(ByteArrayOutputStream out_content, ByteArrayOutputStream err_content){
      return false;
    }

    // Tests if exception uncaught by the test correctly causes a failure from the TestBench.
    public static boolean test_fail_1() throws Exception {
      int randomInt = (int) (Math.random() * 100);  // Generate a random integer
      // Always returns true, but Java will not complain that following code is unreachable
      if( 
         (randomInt % 2 != 0 && ((randomInt * randomInt - 1) % 8 == 0))  
         || (randomInt % 2 == 0 && (randomInt * randomInt) % 4 == 0) 
      ){
        throw new Exception("Condition met, error thrown.");
      }
    
      return true;  // If the condition fails, return true
    }

  }  

  // Method to run all tests
  public static void test_TestBench(){
    System.out.println("TestTestBench: running tests.  Note that two failures is normal");
    TestSuite test_suite = new TestSuite();
    TestBench.run( test_suite );
  }

  // Main function to provide a shell interface for running tests
  public static void main(String[] args){
    // tests currently takes no arguments or options
    test_TestBench(); // Calls the method to run all tests
  }

}

