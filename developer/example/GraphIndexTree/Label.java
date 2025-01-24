/*
  Implementation of Ariadne_Label for BigInteger array-based labels.
*/
import java.math.BigInteger;
import java.util.Arrays;

import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX_List;


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

  @Override public boolean is_null(){
    return value == null;
  }
  public int length(){
    return value.length;
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
  
  @Override public String toString(){
    if(is_null()) return "Label()";
    if(length() == 0) return "Label([])";

    StringBuilder formatted = new StringBuilder("Label([");

    // Use precise loop with TM_SR_NX_List to iterate
    Ariadne_TM_SR_NX_List<BigInteger> value_srtm = Ariadne_TM_SR_NX_List.make(Arrays.asList(value));
    if(value_srtm.can_read()){
      do{
        formatted.append(value_srtm.read().toString());
        if( !value_srtm.can_step() ) break;
        value_srtm.step();
        formatted.append(" ,");
      }while(true);
    }

    formatted.append("])");
    return formatted.toString();
  }

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
