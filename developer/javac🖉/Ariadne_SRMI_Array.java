package com.ReasoningTechnology.Ariadne;
import java.util.List;

public class Ariadne_SRMI_Array<TElement> extends Ariadne_SRMI<TElement>{

  public static <T> Ariadne_SRMI_Array<T> make(List<T> array){
    return new Ariadne_SRMI_Array<>(array);
  }

  private List<TElement> _array;
  private int _index;
  private Topology _topology;
  private Location _location;

  protected Ariadne_SRMI_Array(List<TElement> array) {
    _array = array;

    if( _array == null || _array.isEmpty() ) 
      _topology = Topology.NO_CELLS;
    else if( _array.size() == 1 ) 
      _topology = Topology.SINGLETON;
    else
      _topology = Topology.SEGMENT;

    if(_topology == Topology.SEGMENT)
      _location = Location.LEFTMOST;
    else
      _location = Location.OTHER;

    _index = 0;
  }

  @Override
  public Topology topology(){
    return _topology;
  }

  @Override
  public Location location(){
    return _location;
  }

  @Override
  public TElement access(){
    if( can_read() ) return _array.get( _index );
    throw new UnsupportedOperationException("Ariadne_SRMI_Array::read can not read tape.");
  }

  @Override
  public void step(){
    if( can_step() ){
      _index++;
      if( _index == _array.size() - 1 ) _location = Location.RIGHTMOST;
      return;
    }
    throw new UnsupportedOperationException("Ariadne_SRMI_Array::step can not step.");
  }

  @Override
  public int leftmost_index(){
    return 0;
  }

  @Override
  public int rightmost_index(){
    if( can_read() ) return _array.size() - 1;
    throw new UnsupportedOperationException("Ariadne_SRMI_Array::rightmost_index can not read array.");
  }

}
