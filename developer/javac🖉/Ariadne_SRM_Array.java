/*
Suitable for attaching to containers that have efficient random access.

*/

package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_SRMI_Array<T> extends Ariadne_SRMI<T> {

  private final List<T> list;

  // Factory method to attach SRMI to a list
  public static <T> Ariadne_SRMI_List<T> attach( List<T> list ){
    return new Ariadne_SRMI_List<>( list );
  }

  // Private constructor
  private Ariadne_SRMI_List( List<T> list ){
    if( list == null ){
      throw new IllegalArgumentException( "Ariadne_SRMI_List::list cannot be null" );
    }
    this.list = list;
  }

  @Override
  public Ariadne_SRM.Topology topology(){
    if( list.isEmpty() ){
      return Ariadne_SRM.Topology.NO_CELLS;
    }
    return Ariadne_SRM.Topology.SEGMENT;
  }

  @Override
  public Ariadne_SRM.Status status(){
    if( list.isEmpty() ){
      return Ariadne_SRM.Status.TAPE_NOT_MOUNTED;
    }
    if( index().equals( BigInteger.ZERO ) ){
      return Ariadne_SRM.Status.LEFTMOST;
    }
    if( index().equals( rightmost_index() ) ){
      return Ariadne_SRM.Status.RIGHTMOST;
    }
    return Ariadne_SRM.Status.INTERIM;
  }

  @Override
  public T read(){
    if( list.isEmpty() || index().compareTo( rightmost_index().add( BigInteger.ONE ) ) >= 0 ){
      throw new UnsupportedOperationException( "Ariadne_SRMI_List::read, out of bounds." );
    }
    return list.get( index().intValue() );
  }

  @Override
  public boolean step(){
    if( index().compareTo( rightmost_index() ) < 0 ){
      BigInteger new_index = index().add( BigInteger.ONE );
      set_index( new_index );
      return true;
    }
    return false; // Reached the end of the list
  }

  @Override
  public BigInteger rightmost_index(){
    return BigInteger.valueOf( list.size() - 1 );
  }

  // Optional: Provide access to the underlying list for inspection
  public List<T> getList(){
    return list;
  }
}
