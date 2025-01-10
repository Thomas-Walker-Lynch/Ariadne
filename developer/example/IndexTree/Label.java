/*
  Implementation of Ariadne_Label for BigInteger array-based labels.
*/
import java.math.BigInteger;
import java.util.Arrays;

import com.ReasoningTechnology.Ariadne.Ariadne_Label;


public class Label implements Ariadne_Label{

  // Owned by class
  //

  public static Label make(BigInteger[] array){
    return new Label(array);
  }

  public static Label root(){
    return new Label(new BigInteger[0]);
  }

  // Instance data
  //

  private BigInteger[] value;

  // Constructor
  //

  private Label(BigInteger[] array){
    this.value = array.clone();
  }

  // Instance interface implementation
  //

  @Override public boolean isEmpty(){
    return value == null;
  }

  @Override public String toString(){
    return Arrays.toString(value);
  }

  @Override public Label copy(){
    return new Label(value);
  }

  // Increment last element by one, modifying in place
  public void inc_across(){
    if(value == null || value.length == 0){
      throw new UnsupportedOperationException("Cannot increment across an empty array.");
    }
    value[value.length - 1] = value[value.length - 1].add(BigInteger.ONE);
  }

  // Append a zero element, modifying in place
  public void inc_down(){
    if(value == null){
      throw new UnsupportedOperationException("Cannot append to a null array.");
    }
    BigInteger[] newValue = Arrays.copyOf(value, value.length + 1);
    newValue[newValue.length - 1] = BigInteger.ZERO;
    value = newValue;
  }

  // Good object citizenship
  //
  
  @Override public boolean equals(Object o){
    if(this == o) return true;
    if( o == null || getClass() != o.getClass() ) return false;
    Label that = (Label) o;
    return Arrays.equals(value, that.value);
  }

  @Override public int hashCode(){
    return Arrays.hashCode(value);
  }
}
