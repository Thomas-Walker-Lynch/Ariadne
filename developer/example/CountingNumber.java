import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import java.math.BigInteger;

public class CountingNumber extends Ariadne_SRM<BigInteger>{

  private BigInteger i;
  private BigInteger maximum;

  private final State state_null = new State_Null();
  private final State state_segment = new State_Segment();
  private final State state_rightmost = new State_Rightmost();
  private final State state_infinite = new State_Infinite();

  private CountingNumber(BigInteger maximum){
    this.i = BigInteger.ONE;
    this.maximum = maximum;

    if( maximum == null ){
      set_state(state_infinite);
    }else if( maximum.compareTo(BigInteger.ZERO) <= 0 ){
      set_state(state_null);
    }else if( maximum.equals(BigInteger.ONE) ){
      set_state(state_rightmost);
    }else{
      set_state(state_segment);
    }
  }

  public static CountingNumber make(BigInteger maximum){
    return new CountingNumber(maximum);
  }

  public static CountingNumber make(){
    return new CountingNumber(null);
  }

  @Override
  public BigInteger read(){
    return i;
  }

  private class State_Null extends State{
    @Override
    boolean can_read(){
      return false;
    }
    @Override
    boolean can_step(){
      return false;
    }
    @Override
    void step(){
      throw new UnsupportedOperationException("Cannot step from NULL state.");
    }
    @Override
    MachineState state(){
      return MachineState.NULL;
    }
  }

  private class State_Segment extends State{
    @Override
    boolean can_read(){
      return true;
    }
    @Override
    boolean can_step(){
      return true;
    }
    @Override
    void step(){
      i = i.add(BigInteger.ONE);
      if( i.equals(maximum) ){
        set_state(state_rightmost);
      }
    }
    @Override
    MachineState state(){
      return MachineState.SEGMENT;
    }
  }

  private class State_Rightmost extends State{
    @Override
    boolean can_read(){
      return true;
    }
    @Override
    boolean can_step(){
      return false;
    }
    @Override
    void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST.");
    }
    @Override
    MachineState state(){
      return MachineState.RIGHTMOST;
    }
  }

  private class State_Infinite extends State{
    @Override
    boolean can_read(){
      return true;
    }
    @Override
    boolean can_step(){
      return true;
    }
    @Override
    void step(){
      i = i.add(BigInteger.ONE);
    }
    @Override
    MachineState state(){
      return MachineState.INFINITE;
    }
  }

}
