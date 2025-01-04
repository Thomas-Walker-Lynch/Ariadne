import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRM<BigInteger>{

  private static final Ariadne_Test test = Ariadne_Test.make("Ariadne_SRM<BigInteger>::");

  public static CountingNumber make(){
    return new CountingNumber(null);
  }
  public static CountingNumber make(BigInteger maximum){
    return new CountingNumber(maximum);
  }

  private BigInteger i;
  private BigInteger maximum;
  Ariadne_SRM.Location location;

  protected CountingNumber(BigInteger maximum){
    i = BigInteger.ONE;
    this.maximum = maximum;
    this.location = Location.LEFTMOST;
    test.print("CountingNumber read() value initialized to: " + i);
  }

  @Override
  public Topology topology(){
    if(maximum == null) return Topology.INFINITE_RIGHT;
    return Topology.SEGMENT;
  }

  @Override
  public Location location(){
    return location;
  }
  
  @Override
  public BigInteger read(){
    return i;  // note that BigInteger is immutable
  }

  @Override
  public void step(){
    super.step();
    i = i.add(BigInteger.ONE);

    if(topology() == Topology.SEGMENT){
      if(i.compareTo(maximum) == 0){
        location = Location.RIGHTMOST;
      }else if(location() == Location.LEFTMOST){
        location = Location.INTERIM;
      }
    }
      
    test.print(" after step, new read() value: " + i);
  }

}

