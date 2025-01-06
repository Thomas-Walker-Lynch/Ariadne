/*
  Step Right Machine

  This is a mostly abstract base class.

  This is for single-threaded execution. The multi-threaded model
  uses `mount` and `dismount` to lock the resources being iterated on.
*/

public abstract class Ariadne_SRM<T>{

  public enum MachineState{
    NULL
    ,CYCLIC
    ,SEGMENT
    ,RIGHTMOST
    ,INFINITE
    ;
  }

  protected abstract class State{
    abstract boolean can_read();
    abstract boolean can_step();
    abstract void step();
    abstract MachineState state();
  }

  private State current_state;

  protected void set_state(State new_state){
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
  public MachineState state(){
    return current_state.state();
  }

  public abstract T read();
}
 
