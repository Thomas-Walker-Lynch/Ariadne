package com.ReasoningTechnology.Ariadne;

public class Ariadne_Test {

  private boolean test = false;
  private String prefix = "";

  public static Ariadne_Test make(String prefix){
    Ariadne_Test instance = new Ariadne_Test();
    instance.prefix = prefix;
    return instance;
  }

  protected Ariadne_Test(){
  }

  public void switch_test(boolean enable){
    if( test && !enable ){
      print("test messages off");
    }
    if( !test && enable ){
      print("test messages on");
    }
    test = enable;
  }

  public void print(String message){
    if( test ){
      System.out.println(prefix + message);
    }
  }
}
