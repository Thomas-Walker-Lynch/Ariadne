package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

public class Ariadne_IndexTree_Child_SRM extends Ariadne_SRMI<BigInteger[]>{

  // Static
  public static Ariadne_IndexTree_Child_SRM make(BigInteger[] initial_label){
    return new Ariadne_IndexTree_Child_SRM( initial_label );
  }

  // Instance data
  private BigInteger[] label;

  // Constructor
  protected Ariadne_IndexTree_Child_SRM(BigInteger[] initial_label){
    super();

    if( initial_label == null || initial_label.length == 0 ){
      throw new IllegalArgumentException( "Initial label must not be null or empty." );
    }

    this.label = initial_label;
    set_topology( topo_infinite_right );
  }

  // Infinite right topology
  private final TopoIface<BigInteger[]> topo_infinite_right = new TopoIface<BigInteger[]>(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger[] read(){
      return label;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      increment();
      label[label.length - 1] = index();
    }
    @Override public Topology topology(){
      return Topology.INFINITE;
    }
  };
}

