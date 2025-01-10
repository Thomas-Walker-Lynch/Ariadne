package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRTM_Label extends Ariadne_SRTM {

  public static Ariadne_SRTM_Label make(){
    return new Ariadne_SRTM_Label();
  }

  @Override public Ariadne_Label read(){
    return (Ariadne_Label)super.read();
  }

}
