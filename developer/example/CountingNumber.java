import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRM<BigInteger>{

  private static final Ariadne_Test test = Ariadne_Test.make("Ariadne_SRM<BigInteger>::");

  public static CountingNumber make(){
    return new CountingNumber();
  }

  private BigInteger i;

  protected CountingNumber(){
    super();
    i = BigInteger.ZERO;
    test.print("CountingNumber read() value initialized to: " + i);
  }

  @Override
  public Topology topology(){
    return Topology.INFINITE_RIGHT; // leftmost, no rightmost
  }

  @Override
  public Status status(){
    if( i.equals(BigInteger.ZERO) ) return Status.AT_LEFTMOST;
    else return Status.AT_MIDWAY;
  }
  
  @Override
  public BigInteger read(){
    return i;  // note that BigInteger is immutable
  }

  @Override
  public boolean step(){
    i = i.add(BigInteger.ONE);
    test.print(" after step right new read() value: " + i);
    return true;
  }

}

