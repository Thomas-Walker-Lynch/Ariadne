/*
  Implementation of Ariadne_Label for string-based labels.
*/
package com.ReasoningTechnology.Ariadne;

public class Ariadne_Label_String implements Ariadne_Label {

  // Owned by class
  public static Ariadne_Label_String make(String s) {
    return new Ariadne_Label_String(s);
  }

  // Instance data
  private final String value;

  // Constructor
  private Ariadne_Label_String(String s) {
    this.value = s;
  }

  // Instance interface implementation
  @Override public boolean isEmpty() {
    return value.isEmpty();
  }

  @Override public String toString() {
    return value;
  }

  @Override public Ariadne_Label copy() {
    return new Ariadne_Label_String(value);
  }

  // Good object citizenship
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ariadne_Label_String that = (Ariadne_Label_String) o;
    return value.equals(that.value);
  }

  @Override public int hashCode() {
    return value.hashCode();
  }
}
