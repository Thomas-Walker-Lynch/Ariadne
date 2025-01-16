/*
Example of the TM_SR_ND's toString function.

*/

import java.util.Arrays;
import java.util.List;

import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND_List;

public class TM_SR_ND_Print_CLI{

  public static void main(String[] args){
    List<Object> data = Arrays.asList(42 ,null ,"" ,"World" ,1000);
    Ariadne_TM_SR_ND tm = Ariadne_TM_SR_ND_List.make(data);
    System.out.println(tm.toString());
    tm.step();
    System.out.println(tm.toString());
    tm.step();
    System.out.println(tm.toString());
    tm.step();
    System.out.println(tm.toString());
    tm.step();
    System.out.println(tm.toString());
  }
}
