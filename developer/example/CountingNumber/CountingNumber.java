import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRTM{

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

  private final TopoIface topo_null = new Topo_Null();
  private final TopoIface topo_segment = new Topo_Segment();
  private final TopoIface topo_rightmost = new Topo_Rightmost();
  private final TopoIface topo_infinite = new Topo_Infinite();

  // Constructor(s)
  //

  public CountingNumber(){
    this.i = BigInteger.ONE;
    this.maximum = maximum;
    set_topology(topo_infinite);
  }

  public CountingNumber(BigInteger maximum){
    this.i = BigInteger.ONE;
    this.maximum = maximum;

    if( maximum.compareTo(BigInteger.ZERO) <= 0 ){
      set_topology( topo_null );
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

  private class Topo_Null implements TopoIface{
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
      throw new UnsupportedOperationException( "Cannot step over NULL topology." );
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
  }

  private class Topo_Segment implements TopoIface{
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
      i = i.add( BigInteger.ONE );
      if( i.equals( maximum ) ){
        set_topology( topo_rightmost );
      }
    }
    @Override public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  private class Topo_Rightmost implements TopoIface{
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
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST." );
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }

  private class Topo_Infinite implements TopoIface{
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
      i = i.add( BigInteger.ONE );
    }
    @Override public Topology topology(){
      return Topology.INFINITE;
    }
  }
}
