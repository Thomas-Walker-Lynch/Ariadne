package com.ReasoningTechnology.Ariadne;
import java.util.List; 

public class Util{
  static boolean debug = false;

  public static void print_list(String prefix ,List<?> item_list){
    if( item_list == null || item_list.isEmpty() ){
      return;
    }
    if( prefix != null && !prefix.isEmpty() ){
      System.out.print(prefix);
    }
    int i = 0;
    int n = item_list.size() - 1;
    System.out.print( " '" + item_list.get(i) + "'" );
    do{
      i++;
      if( i > n ) break;
      System.out.print(", " +  "'" + item_list.get(i) + "'");
    }while( true );
    System.out.println(".");
  }

}
