/*
Ariadne_SRM with index

*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRMI<TElement> extends Ariadne_SRM<TElement>{

  public static <TElement> Ariadne_SRMI<TElement> make(){
    return new Ariadne_SRMI<TElement>();
  }
  protected Ariadne_SRMI(){
  }

  public int index(){
    throw new UnsupportedOperationException("Ariadne_SRMI::index not implemented.");
  }

  public int leftmost_index(){
    throw new UnsupportedOperationException("Ariadne_SRMI::leftmost_index not implemented.");
  }
  public int rightmost_index(){
    throw new UnsupportedOperationException("Ariadne_SRMI::rightmost_index not implemented.");
  }

  void seek(int i){
    throw new UnsupportedOperationException("Ariadne_SRMI::seek not implemented.");
  }

}
