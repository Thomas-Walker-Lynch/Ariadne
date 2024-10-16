package com.ReasoningTechnology.TestBench;
import java.util.Map;

public class TestBench {

  // typically used to gather results before a return
  public static boolean all(boolean[] conditions){
    for( boolean condition : conditions ){
      if( !condition ){
        return false;
      }
    }
    return true;
  }

  public static void run(Map<String, Boolean> test_map){
    int totalTest_Map = test_map.size();
    int passedTest_Map = 0;
    int failedTest_Map = 0;

    for( Map.Entry<String, Boolean> test : test_map.entrySet() ){
      try{
        if( test.getValue() ){
          passedTest_Map++;
        } else{
          System.out.println( "failed: " + test.getKey() );
          failedTest_Map++;
        }
      } catch(Exception e){
        System.out.println( "failed: " + test.getKey() );
        failedTest_Map++;
      }
    }

    System.out.println("Total test_map run: " + totalTest_Map);
    System.out.println("Total test_map passed: " + passedTest_Map);
    System.out.println("Total test_map failed: " + failedTest_Map);
  }

}
