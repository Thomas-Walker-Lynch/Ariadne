
/*
  Base interface for labels used in the Ariadne library.
*/
package com.ReasoningTechnology.Ariadne;
public interface Ariadne_Label{
  boolean is_null();
  Ariadne_Label copy();
  @Override String toString();
  @Override public boolean equals(Object o);
}
