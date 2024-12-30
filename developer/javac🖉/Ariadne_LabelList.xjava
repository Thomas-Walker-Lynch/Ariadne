/*
A list of Labels.
*/

package com.ReasoningTechnology.Ariadne;

import java.util.List;
import java.util.ArrayList;

public class Ariadne_LabelList extends ArrayList<Ariadne_Label>{

  // owned by the class
  //
    public static Ariadne_LabelList make(Object...label_list){
      return new Ariadne_LabelList(label_list);
    }

  // data owned by the instance
  //

  // constructors
  //
    public Ariadne_LabelList(Object...label_list){
      super();
      if(label_list != null){
        this.add(List.of(label_list)); // Delegate work to the add method
      }
    }

  // instance interface
  //
    public boolean add(Object...obj_list){
      boolean modified = false;
      if(obj_list != null) for(Object obj:obj_list) modified |= add_one(obj);
      return modified;
    }

  // Object interface
  //
    @Override
    public String toString(){
      return super.toString();
    }

  // private helpers
  //
    private boolean add_one(Object obj){
      if(obj instanceof String){
        return add_one((String) obj);
      }else if(obj instanceof Ariadne_Label){
        return add_one((Ariadne_Label) obj);
      }
      throw new IllegalArgumentException(
        "Ariadne_LabelList::add_one, cannot make label from object of type: " + obj.getClass().getName()
      );
    }

    private boolean add_one(String string){
      return super.add(Ariadne_Label.make(string));
    }

    private boolean add_one(Ariadne_Label label){
      return super.add(label);
    }
}


