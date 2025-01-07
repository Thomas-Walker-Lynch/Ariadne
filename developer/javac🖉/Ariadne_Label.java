package com.ReasoningTechnology.Ariadne;



/*
  A value for the node.label property.

  This is a wrapper for a String. We can't instead use an alias by extending
  String, because String is a JavaScript 'final' type.

*/
public class Ariadne_Label{

  // owned by class


  // data owned by instance

    private final String value;

  // constructors


  private Ariadne_Label(String s){
    this.value = s;
  }

  Ariadne_Label make(String s){
    return  new Ariadne_Label(s);
  }

  public boolean isEmpty(){
    return value.isEmpty();
  }

  @Override
  public String toString(){
    return value;
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;
    if( o == null || getClass() != o.getClass() ) return false;
    Ariadne_Label label = (Ariadne_Label)o;
    return value.equals( label.value );
  }

  @Override
  public int hashCode(){
    return value.hashCode();
  }

}
