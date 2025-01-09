package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRM_Label extends Ariadne_SRM {

  public static Ariadne_SRM_Label make(){
    return new Ariadne_SRM_Label();
  }

  @Override public Ariadne_Label read(){
    return (Ariadne_Label)super.read();
  }

}
