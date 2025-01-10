package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRMT_Label extends Ariadne_SRMT {

  public static Ariadne_SRMT_Label make(){
    return new Ariadne_SRMT_Label();
  }

  @Override public Ariadne_Label read(){
    return (Ariadne_Label)super.read();
  }

}
