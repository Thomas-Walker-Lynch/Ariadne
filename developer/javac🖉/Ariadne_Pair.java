package com.ReasoningTechnology.Ariadne;

public class Ariadne_Pair<K ,V> {
  private final K key;
  private final V value;

  public Ariadne_Pair(K key ,V value){
    this.key = key;
    this.value = value;
  }

  public K key(){
    return key;
  }

  public V value(){
    return value;
  }

  @Override public boolean equals(Object o){
    if( this == o ) return true;
    if( o == null || getClass() != o.getClass() ) return false;
    Ariadne_Pair<?,?> pair = (Ariadne_Pair<?,?>)o;
    return key.equals(pair.key) && value.equals(pair.value);
  }

  @Override public int hashCode(){
    return 31 * key.hashCode() + value.hashCode();
  }

  @Override public String toString(){
    return "( " + key + " , " + value + " )";
  }

  public static <K ,V> Ariadne_Pair<K ,V> make(K key ,V value){
    return new Ariadne_Pair<>(key ,value);
  }
}
