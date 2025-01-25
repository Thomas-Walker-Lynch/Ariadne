/*
Tape is finite, `_F`.

Append

  Append to rightmost is not considered 'destructive' because the system, including any entangled machines, remains sound after the operation. This stand in contrast to
  the destructive operation of `delete` where one machine could delete a cell that another
  machine has its head on, then suddenly the other machine can no longer function.

  Still communication between entangled machines becomes possible with the addition of an append operation due to `can_step` changing value though no operation on a given machine causes that.

  Communication between entangled machines is already possible due to one machine writing data that is then read by another machine, so `append` becomes another means for implementing communications. If append is to be used, the programmer needs to be sure that all
  algorithms run on all entangled machines will continue to function as intended.

toString

   Having a finite tape machine makes it possible to implement a `toString` operation.
   In contrast to the infinite tape machine which can not iterate over the tape.
  
*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

// RT == read type
public class Ariadne_TM_SR_NX_F<RT> extends Ariadne_TM_SR_NX<RT> {

  // static
  //
  public static Ariadne_TM_SR_NX_F make(){
    return new Ariadne_TM_SR_NX_F();
  }

  // instance data
  //

  // constructor(s)
  //

  protected Ariadne_TM_SR_NX_F(){
  }

  // Implementation of instance interface.
  //

  public void append(RT x){
    throw new UnsupportedOperationException("Ariadne_TM_SR_NX_F::entangle not implemented.");
  }

  @Override public Ariadne_TM_SR_NX_F<RT> entangle(){
    throw new UnsupportedOperationException("Ariadne_TM_SR_NX::entangle not implemented.");
  }

  // good citizen
  //

  public String to_string_annotated(){

    test.print("::to_string_annotated");

    if(!is_mounted()) return "TM_SR_NX(NotMounted)";
    if(!can_read()) return "TM_SR_NX(Null)";

    // output takes two lines, starting from the left column on each
    StringBuilder data_channel = new StringBuilder("\n");
    StringBuilder control_channel = new StringBuilder("");
    
    data_channel.append( "TM_SR_NX(" ).append( topology().name()).append("(" );
    control_channel.append( " ".repeat(data_channel.length()-1) );

    String element = null;
    Ariadne_TM_SR_NX_F<RT> copy = this.entangle();
    if( copy.can_rewind() ) copy.rewind();

    Object o = null;
    do{

      o = copy.read();

      if(o == null){
        data_channel.append( " ".repeat(3) );
        if( head_on_same_cell(copy) ){
          control_channel.append("<->");
        }else{
          control_channel.append("|-|");
        }
      }else if(o.toString().isEmpty()){
        data_channel.append( " ".repeat(3) );
        if( head_on_same_cell(copy) ){
          control_channel.append("<e>");
        }else{
          control_channel.append("|e|");
        }
      }else{
        element = o.toString();
        data_channel.append(" ").append(element).append(" ");
        if(head_on_same_cell(copy)){
          control_channel
            .append("<")
            .append("d".repeat(element.length()))
            .append(">");
        }else{
          control_channel
            .append("|")
            .append("d".repeat(element.length()))
            .append("|");
        }
      }

      if( !copy.can_step() ) break;
      copy.step();

    }while(true);

    data_channel.append(" )");
    control_channel.append("  ");

    return 
      data_channel
      .append("\n")
      .append(control_channel)
      .toString();
  }

  // RT code format style comma separated list
  @Override public String toString(){

    Ariadne_TM_SR_NX_F<RT> tm = this.entangle();
    if( tm.can_rewind() ) tm.rewind();

    if( !tm.can_read() ) return "";

    StringBuilder output = new StringBuilder();
    do{

      if( tm.head_on_same_cell(this) ) output.append("<");
      RT x = tm.read();
      if( x != null ) output.append(x);
      if( tm.head_on_same_cell(this) ) output.append(">");

      if( !tm.can_step() ) break;

      tm.step();
      if( x == null )
        output.append(",");
      else
        output.append(" ,");

    }while(true);

    return output.toString();
  }


}
