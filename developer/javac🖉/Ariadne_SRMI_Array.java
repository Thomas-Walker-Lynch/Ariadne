package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_SRMI_Array<TElement> extends Ariadne_SRMI<TElement> {

  private final List<TElement> array;

  public Ariadne_SRMI_Array(List<TElement> array) {
    super(BigInteger.ZERO, array == null || array.isEmpty() ? BigInteger.ZERO : BigInteger.valueOf(array.size() - 1));

    if (array == null || array.isEmpty()) {
      set_state(state_null);
    } else if (array.size() == 1) {
      set_state(state_rightmost);
    } else {
      set_state(state_segment);
    }

    this.array = array;
  }

  @Override
  public TElement read() {
    if (!can_read()) {
      throw new UnsupportedOperationException("Cannot read from the current state.");
    }
    return array.get(index().intValueExact());
  }

  private final State state_null = new State() {
    @Override
    boolean can_read() {
      return false;
    }
    @Override
    boolean can_step() {
      return false;
    }
    @Override
    void step() {
      throw new UnsupportedOperationException("Cannot step from NULL state.");
    }
    @Override
    MachineState state() {
      return MachineState.NULL;
    }
  };

  private final State state_segment = new State() {
    @Override
    boolean can_read() {
      return true;
    }
    @Override
    boolean can_step() {
      return index().compareTo(rightmost_index().subtract(BigInteger.ONE)) < 0;
    }
    @Override
    void step() {
      if (can_step()) {
        seek(index().add(BigInteger.ONE));
      } else {
        set_state(state_rightmost);
      }
    }
    @Override
    MachineState state() {
      return MachineState.SEGMENT;
    }
  };

  private final State state_rightmost = new State() {
    @Override
    boolean can_read() {
      return true;
    }
    @Override
    boolean can_step() {
      return false;
    }
    @Override
    void step() {
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST state.");
    }
    @Override
    MachineState state() {
      return MachineState.RIGHTMOST;
    }
  };
}
