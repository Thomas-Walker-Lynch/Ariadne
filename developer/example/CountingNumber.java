import java.math.BigInteger;

public class CountingNumber {

  private BigInteger i;
  private BigInteger maximum;
  private State current_state;

  public static CountingNumber make( BigInteger maximum ){
    return new CountingNumber( maximum );
  }

  public static CountingNumber make(){
    return new CountingNumber( null );
  }

  private CountingNumber( BigInteger maximum ){
    this.i = BigInteger.ONE;
    this.maximum = maximum;
    this.current_state = ( maximum == null ) ? new State_InfiniteRight() : new State_Leftmost();
  }

  private void set_state( State new_state ){
    this.current_state = new_state;
  }

  public boolean can_read(){
    return current_state.can_read();
  }

  public boolean can_step(){
    return current_state.can_step();
  }

  public void step(){
    current_state.step();
  }

  public BigInteger read(){
    return i;
  }

  // --- State Interface ---
  private abstract class State {
    abstract boolean can_read();
    abstract boolean can_step();
    abstract void step();
  }

  // --- State_Leftmost ---
  private class State_Leftmost extends State {
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
      i = i.add( BigInteger.ONE );
      if( i.equals( maximum ) ){
        set_state( new State_Rightmost() );
      }else{
        set_state( new State_InterimSegment() );
      }
    }
  }

  // --- State_InterimSegment ---
  private class State_InterimSegment extends State {
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
      i = i.add( BigInteger.ONE );
      if( i.equals( maximum ) ){
        set_state( new State_Rightmost() );
      }
    }
  }

  // --- State_InfiniteRight ---
  private class State_InfiniteRight extends State {
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
      i = i.add( BigInteger.ONE );
    }
  }

  // --- State_Rightmost ---
  private class State_Rightmost extends State {
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
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST." );
    }
  }
}
