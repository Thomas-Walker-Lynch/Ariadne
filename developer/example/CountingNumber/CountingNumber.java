import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRTM<BigInteger>{

  public static CountingNumber make(BigInteger maximum){
    return new CountingNumber(maximum);
  }

  public static CountingNumber make(){
    return new CountingNumber();
  }

  private BigInteger i;
  private BigInteger maximum;

  private final TopoIface<BigInteger> state_null = new ASRTM_Null();
  private final TopoIface<BigInteger> state_segment = new ASRTM_Segment();
  private final TopoIface<BigInteger> state_rightmost = new ASRTM_Rightmost();
  private final TopoIface<BigInteger> state_infinite = new ASRTM_Infinite();

  public CountingNumber(){
    this.i = BigInteger.ONE;
    this.maximum = maximum;
    set_topology(state_infinite);
  }

  public CountingNumber(BigInteger maximum){
    this.i = BigInteger.ONE;
    this.maximum = maximum;

    if( maximum.compareTo(BigInteger.ZERO) <= 0 ){
      set_topology( state_null );
      return;
    }

    if( maximum.equals(BigInteger.ONE) ){
      set_topology(state_rightmost);
      return;
    }

    set_topology(state_segment);
  }


  private class ASRTM_Null implements TopoIface<BigInteger>{
    @Override
    public boolean can_read(){
      return false;
    }
    @Override
    public BigInteger read(){
      return i;
    }
    @Override
    public boolean can_step(){
      return false;
    }
    @Override
    public void step(){
      throw new UnsupportedOperationException( "Cannot step from NULL state." );
    }
    @Override
    public Topology topology(){
      return Topology.NULL;
    }
  }

  private class ASRTM_Segment implements TopoIface<BigInteger>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public BigInteger read(){
      return i;
    }
    @Override
    public boolean can_step(){
      return true;
    }
    @Override
    public void step(){
      i = i.add( BigInteger.ONE );
      if( i.equals( maximum ) ){
        set_topology( state_rightmost );
      }
    }
    @Override
    public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  private class ASRTM_Rightmost implements TopoIface<BigInteger>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public BigInteger read(){
      return i;
    }
    @Override
    public boolean can_step(){
      return false;
    }
    @Override
    public void step(){
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST." );
    }
    @Override
    public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }

  private class ASRTM_Infinite implements TopoIface<BigInteger>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public BigInteger read(){
      return i;
    }
    @Override
    public boolean can_step(){
      return true;
    }
    @Override
    public void step(){
      i = i.add( BigInteger.ONE );
    }
    @Override
    public Topology topology(){
      return Topology.INFINITE;
    }
  }
}
