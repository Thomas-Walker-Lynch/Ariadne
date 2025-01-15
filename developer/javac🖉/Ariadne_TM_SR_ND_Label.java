/*
Graph nodes are referenced by their labels.

*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_TM_SR_ND_Label extends Ariadne_TM_SR_ND {

  public static Ariadne_TM_SR_ND_Label make(){
    return new Ariadne_TM_SR_ND_Label();
  }

  @Override public Ariadne_Label read(){
    return (Ariadne_Label)super.read();
  }

}
