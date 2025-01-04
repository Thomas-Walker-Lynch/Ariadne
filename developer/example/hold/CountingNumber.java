import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRM<BigInteger> {

  private static final Ariadne_Test test = Ariadne_Test.make( "Ariadne_SRM<BigInteger>::" );

  public static CountingNumber make(){
    return new CountingNumber( null );
  }

  public static CountingNumber make( BigInteger maximum ){
    return new CountingNumber( maximum );
  }

  private final Topology _topology;
  private BigInteger i;
  private BigInteger maximum;
  private Location location;
  private Runnable step_behavior;

  protected CountingNumber( BigInteger maximum ){
    this.maximum = maximum;
    this.i = BigInteger.ONE;
    this.location = Location.LEFTMOST;

    if( maximum == null ){
      _topology = Topology.INFINITE_RIGHT;
      step_behavior = this::step_infinite_right;
    } else {
      _topology = Topology.SEGMENT;
      step_behavior = this::step_segment;
    }

    test.print( "CountingNumber initialized with topology: " + _topology + ", initial value: " + i );
  }

  @Override
  public Topology topology(){
    return _topology;
  }

  @Override
  public Location location(){
    return location;
  }

  @Override
  public BigInteger read(){
    return i;
  }

  @Override
  public void step(){
    step_behavior.run();
  }

  private void step_segment(){
    i = i.add( BigInteger.ONE );
    if( i.equals( maximum ) ){
      location = Location.RIGHTMOST;
      step_behavior = this::step_from_rightmost;
    }else if( location == Location.LEFTMOST ){
      location = Location.INTERIM;
    }
    test.print( " after step_segment, new read() value: " + i );
  }

  private void step_infinite_right(){
    i = i.add( BigInteger.ONE );
    test.print( " after step_infinite_right, new read() value: " + i );
  }

  private void step_from_rightmost(){
    throw new UnsupportedOperationException( "CountingNumber::step can not step from RIGHTMOST." );
  }

}
