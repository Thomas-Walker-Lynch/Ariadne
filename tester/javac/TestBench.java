package com.ReasoningTechnology.TestBench;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Map;

public class TestBench{

  /* --------------------------------------------------------------------------------
    Static Data
  */

  private static PrintStream original_out;
  private static PrintStream original_err;
  private static InputStream original_in;
  
  private static ByteArrayOutputStream out_content;
  private static ByteArrayOutputStream err_content;
  private static InputStream in_content;

  /* --------------------------------------------------------------------------------
    Test utility methods
  */

  // typically used to gather results before a return
  public static boolean all(boolean[] conditions){
    for( boolean condition : conditions ){
      if( !condition ){
        return false;
      }
    }
    return true;
  }

  /* --------------------------------------------------------------------------------
    Test run helpers
  */
  public static void log_output(String test_name ,String stream ,String output_data){
    try(FileWriter log_writer = new FileWriter("test_log.txt" ,true)){  // Append mode
      log_writer.write("Test: " + test_name + "\n");
      log_writer.write("Stream: " + stream + "\n");
      log_writer.write("Output:\n" + output_data + "\n");
      log_writer.write("----------------------------------------\n");
    } catch(IOException e) {
      System.err.println("Error writing to log for test: " + test_name + ", stream: " + stream);
      e.printStackTrace(System.err);
    }
  }

  public static boolean method_is_wellformed(Method method) {
    // Check if the method returns boolean
    if(!method.getReturnType().equals(boolean.class)){
      System.out.println("Structural problem: " + method.getName() + " does not return boolean.");
      return false;
    }

    // Check if the method has exactly three arguments
    Class<?>[] parameterTypes = method.getParameterTypes();
    if(parameterTypes == null || parameterTypes.length != 3){
      System.out.println("Structural problem: " + method.getName() + " does not have three arguments.");
      return false;
    }

    // Check that all parameters are ByteArrayOutputStream
    if(
       !parameterTypes[0].equals(ByteArrayOutputStream.class) // Check first parameter
       || !parameterTypes[1].equals(ByteArrayOutputStream.class) // Check second parameter
       || !parameterTypes[2].equals(ByteArrayOutputStream.class) // Check third parameter
       ){
      System.out.println("Structural problem: " + method.getName() + " has incorrect argument types.");
      return false;
    }

    return true;
  }
  
  public static void flush_stdin() throws IOException{
    while(System.in.available() > 0){
      System.in.read();
    }
  }

  public static void set_test_input(String input_data){
    ByteArrayInputStream test_in = new ByteArrayInputStream(input_data.getBytes());
    System.setIn(test_in);
  }


  public static void run(Object test_suite){

    int failed_test = 0;
    int passed_test = 0;

    Method[] methods = test_suite.getClass().getDeclaredMethods();

    for(Method method : methods){

      // Ways a test can fail ,not exclusive
      boolean fail_testbench = false;
      boolean fail_malformed = false;
      boolean fail_reported = false;
      boolean fail_exception = false;
      boolean fail_extraneous_stdout = false;
      boolean fail_extraneous_stderr = false;

      if( !method_is_wellformed(method) ){
        // the malformed check prints specific messages
        System.out.println("TestBench: malformed test counted as a failure:\'" + method.getName() + "\'");
        failed_test++;
        continue;
      }

      PrintStream original_out = null;
      PrintStream original_err = null;
      InputStream original_in  = null;

      ByteArrayOutputStream out_content = null;
      ByteArrayOutputStream err_content = null;
      ByteArrayInputStream in_content = null;

      try{
        // Redirect the I/O channels so the tests can manipulate them as data.
        original_out = System.out;
        original_err = System.err;
        original_in = System.in;
  
        out_content = new ByteArrayOutputStream();
        err_content = new ByteArrayOutputStream();
        in_content = new ByteArrayInputStream();

        System.setOut(new PrintStream(out_content));
        System.setErr(new PrintStream(err_content));
        System.setIn(in_content);

      } catch(Throwable e){  // Catches both Errors and Exceptions
        // Restore stdout ,stderr ,and stdin before reporting the error
        System.setOut(original_out);
        System.setErr(original_err);
        System.setIn(original_in);

        // Report the error
        System.out.println("TestBench:: when redirecting i/o in preparation for running test \'" + method.getName() + "\' ,test bench itself throws error: " + e.toString());
        failed_test++;
        continue;
      }

      // Capture detritus 
      String exception_string = "";
      String stdout_string = "";
      String stderr_string = "";

      // Finally the gremlins run the test!
      try{

        Object result = method.invoke(test_suite ,in_content ,out_content ,err_content);
        fail_reported = !Boolean.TRUE.equals(result); // test passes if ,and only if ,it returns exactly 'true'.

        // A test fails when there is extraneous output
        fail_extraneous_stdout = out_content.size() > 0;
        fail_extraneous_stderr = err_content.size() > 0;

        // We keep it to log it
        if(fail_extraneous_stdout){ stdout_string = out_content.toString(); }
        if(fail_extraneous_stderr){ stderr_string = err_content.toString(); }

      } catch(Exception e){

        // A test fails when there is an unhandled exception.
        fail_exception = true;

        // We keep it to report it
        exception_string = e.toString();

      } finally{
        
        // Restore original stdin ,stdout ,and stderr
        System.setOut(original_out);
        System.setErr(original_err);
        System.setIn(original_in);
      }

      // Report the test result.
      if(
         fail_reported 
         || fail_exception 
         || fail_extraneous_stdout
         || fail_extraneous_stderr
         ){

        failed_test++;

        if(fail_reported) System.out.println("failed: \'" + method.getName() + "\' by report from test.");
        if(fail_exception) System.out.println("failed: \'" + method.getName() + "\' due to unhandled exception: " + exception_string);
        if(fail_extraneous_stdout){
          System.out.println("failed: \'" + method.getName() + "\' due extraneous stdout output ,see log.");
          log_output(method.getName() ,"stdout" ,stdout_string);
        }
        if(fail_extraneous_stderr){
          System.out.println("failed: \'" + method.getName() + "\' due extraneous stderr output ,see log.");
          log_output(method.getName() ,"stderr" ,stderr_string);
        }

      } else{
        passed_test++;
      }

    }

    // Summarize all the test results
    System.out.println("Total tests run: " + (passed_test + failed_test));
    System.out.println("Total tests passed: " + passed_test);
    System.out.println("Total tests failed: " + failed_test);
  }

}
