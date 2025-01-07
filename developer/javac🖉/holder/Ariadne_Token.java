package com.ReasoningTechnology.Ariadne;

/*
An error token.

*/
public class Ariadne_Token {
  private final String value;

  public Ariadne_Token(String value){
    this.value = value;
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
    if(this == o) return true;  // No padding, not nested
    if( o == null || getClass() != o.getClass() ) return false;  // Padded, because it's nested
    Ariadne_Token token = (Ariadne_Token)o;
    return value.equals( token.value );
  }

  @Override
  public int hashCode(){
    return value.hashCode();
  }
}
