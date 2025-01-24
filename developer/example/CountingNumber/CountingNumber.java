import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_TM_SR_NX<BigInteger>{

  // Static
  //
  public static CountingNumber make(BigInteger maximum){
    return new CountingNumber(maximum);
  }
  public static CountingNumber make(){
    return new CountingNumber();
  }

  // Instance data
  //
  private BigInteger i;
  private BigInteger maximum;
  private final TopoIface<BigInteger> topo_null = new TopoNull();
  private final TopoIface<BigInteger> topo_segment = new TopoSegment();
  private final TopoIface<BigInteger> topo_rightmost = new TopoRightmost();
  private final TopoIface<BigInteger> topo_infinite = new TopoInfinite();

  // Constructor(s)
  //
  public CountingNumber(){
    this.i = BigInteger.ONE;
    this.maximum = null;
    set_topology(topo_infinite);
  }
  public CountingNumber(BigInteger maximum){
    this.i = BigInteger.ONE;
    this.maximum = maximum;

    if( maximum.compareTo(BigInteger.ZERO) <= 0 ){
      set_topology(topo_null);
      return;
    }
    if( maximum.equals(BigInteger.ONE) ){
      set_topology(topo_rightmost);
      return;
    }
    set_topology(topo_segment);
  }

  // Instance interface implementation
  //  

  private class TopoNull implements TopoIface<BigInteger>{
    @Override public boolean can_read(){
      return false;
    }
    @Override public BigInteger read(){
      return i;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step over NULL topology.");
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
  }
  private class TopoSegment implements TopoIface<BigInteger>{
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger read(){
      return i;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      i = i.add(BigInteger.ONE);
      if( i.equals(maximum) ){
        set_topology(topo_rightmost);
      }
    }
    @Override public Topology topology(){
      return Topology.SEGMENT;
    }
  }
  private class TopoRightmost implements TopoIface<BigInteger>{
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger read(){
      return i;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST.");
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
  private class TopoInfinite implements TopoIface<BigInteger>{
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger read(){
      return i;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      i = i.add(BigInteger.ONE);
    }
    @Override public Topology topology(){
      return Topology.INFINITE;
    }
  }
}
