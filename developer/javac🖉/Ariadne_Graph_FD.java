/*
Parent type for finite deterministic graphs. Children types must define the
`node_db`, `edge_db`, and `start_list`, as well as any unique behavior.

*/

package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.HashMap;

// LT == Label Type, NT == Node Type
public class Ariadne_Graph_FD<LT extends Ariadne_Label ,NT extends Ariadne_Node<LT>> extends Ariadne_Graph<LT ,NT>{

  // Static
  //

  public static <T extends Ariadne_Label ,N extends Ariadne_Node<T>> Ariadne_Graph_FD<T ,N> make(){
    return new Ariadne_Graph_FD<>();
  }

  public static <T extends Ariadne_Label ,N extends Ariadne_Node<T>> Ariadne_Graph_FD<T ,N> make(String name){
    return new Ariadne_Graph_FD<>(name);
  }

  // Instance data
  //

  private final HashMap<LT ,NT> node_db = new HashMap<>();
  private final HashMap< Ariadne_Pair<LT ,LT> ,Ariadne_Edge<LT> > edge_db = new HashMap<>();
  private final ArrayList<LT> start_list = new ArrayList<>();

  // Constructor(s)
  //

  protected Ariadne_Graph_FD(){
    super();
  }

  protected Ariadne_Graph_FD(String name){
    super(name);
  }

  // Implementation of the instance interface
  //

  @Override public boolean is_wellformed(){
    boolean distinct_flag = true;
    boolean start_flag = true;

    // Nodes are distinct due to the use of the HashMap

    // Check that start nodes are in the graph
    Ariadne_TM_SR_NX_Array<LT> tm = Ariadne_TM_SR_NX_Array.make(start_list);
    if( tm.can_read() ){
      do{
        start_flag = this.node( tm.read() ) != null;
        if(!start_flag) break;
        if( !tm.can_step() ) break;
        tm.step();
      }while(true);
    }

    return distinct_flag && start_flag;
  }

  @Override public Ariadne_TM_SR_NX<LT> start(){
    return Ariadne_TM_SR_NX_Array.make(start_list);
  }

  // Lookup a node on the graph by label
  @Override public NT node(LT label){
    return node_db.get(label);
  }

  // A tm of all nodes
  public Ariadne_TM_SR_NX<NT> node(){
    return Ariadne_TM_SR_NX_Array.make( new ArrayList<>(node_db.values()) );
  }

  // Lookup an edge by two labels. For a directed graph, label0 is the origin node label.
  @Override public Ariadne_Edge<LT> edge(LT label0 ,LT label1){
    return edge_db.get( Ariadne_Pair.make(label0 ,label1) );
  }

  // A tm of all edges
  public Ariadne_TM_SR_NX<Ariadne_Edge<LT>> edge(){
    return Ariadne_TM_SR_NX_Array.make( new ArrayList<>(edge_db.values()) );
  }

  // Object interface
  //

  @Override public String toString(){
    StringBuilder output = new StringBuilder();
    output.append("Graph_FD( ");

    {
      Ariadne_TM_SR_NX<NT> tm = this.node();
      if( !tm.can_read() ){
        output.append( "Node_list()" );
      }else{
        output.append( "Node_list( " );
        output.append(tm);
        output.append( " )" );
      }
    }

    {
      Ariadne_TM_SR_NX<Ariadne_Edge<LT>> tm = this.edge();
      if( !tm.can_read() ){
        output.append( "Edge_list()" );
      }else{
        output.append( "Edge_list( " );
        output.append(tm);
        output.append( " )" );
      }
    }
    
    output.append(" )");
    return output.toString();
  }
}
