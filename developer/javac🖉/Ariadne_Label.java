
/*
  Base interface for labels used in the Ariadne library.
*/
package com.ReasoningTechnology.Ariadne;
public interface Ariadne_Label {
  boolean isEmpty();
  Ariadne_Label copy();
  @Override String toString();
}
