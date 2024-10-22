package com.ReasoningTechnology.Ariadne;

/*
  A value for the node.label property.

  This is a wrapper for a String. We can't instead use an alias by extending
  String, because String is a JavaScript 'final' type.

*/
public class Label {
  private final String value;

  public Label(String value){
    this.value = value;
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }

  public String get(){
    return value;
  }

  @Override
  public String toString(){
    return value;
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;
    if( o == null || getClass() != o.getClass() ) return false;
    Label label = (Label)o;
    return value.equals( label.value );
  }

  @Override
  public int hashCode(){
    return value.hashCode();
  }
}
