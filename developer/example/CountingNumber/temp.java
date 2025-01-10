  // Implementation of instance interface.
  //

  @Override public List<BigInteger[]> read(){
    return diagonal;
  }

  @Override public void step(){
    diagonal.clear();

    // Process unopened nodes
    while( !list_of__unopened_node.isEmpty() ){
      BigInteger[] label = list_of__unopened_node.remove(0);

      // Retrieve the node using lookup
      Ariadne_Node node = lookup(label);

      // Descend by getting neighbors
      List<BigInteger[]> child_labels = fetch_child_labels(node);
      if( !child_labels.isEmpty() ){
        list_of__opened_incomplete_child_list.add(child_labels);
      }
    }

    // Process incomplete child lists
    while( !list_of__opened_incomplete_child_list.isEmpty() ){
      List<BigInteger[]> child_labels = list_of__opened_incomplete_child_list.remove(0);
      if( !child_labels.isEmpty() ){
        BigInteger[] label = child_labels.remove(0);
        diagonal.add(label);

        // Retrieve node and check its neighbors
        Ariadne_Node node = lookup(label);
        if( !fetch_child_labels(node).isEmpty() ){
          list_of__unopened_node.add(label);
        }
      }
    }
  }

  private Ariadne_Node lookup(BigInteger[] label){
    // Perform a lookup to retrieve the node corresponding to the label
    return Ariadne_Node.make(label);
  }
