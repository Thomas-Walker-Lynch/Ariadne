/*
Graph nodes are referenced by their labels.

*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_ND_SR_TM_Label extends Ariadne_ND_SR_TM {

  public static Ariadne_ND_SR_TM_Label make(){
    return new Ariadne_ND_SR_TM_Label();
  }

  @Override public Ariadne_Label read(){
    return (Ariadne_Label)super.read();
  }

}
