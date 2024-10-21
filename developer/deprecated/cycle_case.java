      //----------------------------------------
      // cycle case
      //
      
      List<Integer>  cycle_index_interval;
      if( cycle_index_interval=path_find_cycle(left_path ,verbose) ){
        ret_value.add( "cycle_found" );

        int cycle_i0 = cycle_index_interval.get(0); // cycle leftmost, inclusive
        int cycle_n = cycle_index_interval.get(1);  // cycle rightmost, inclusive

        if(verbose) Util.print_list(
          "Found cycle:" 
          ,left_path.subList( cycle_i0 ,n +1)
        );

        // mark cycle
        LabelList undefined_node_list;
        int i = cycle_i0;
        do{
          node_label = left_path.get(i);
          node = super.lookup(node_label);
          if(node){
            if( node.get("mark") == null ){
              node.put( "mark" ,new HashTokenSet() );
            }
            ( (TokenSet)node.get("mark") ).add("cycle_member");
          }else{
            undefined_node_list.add(node_label);
            ret_value.add( "undefined_node" );
          }
          i++;
          if( i > cycle_n ) break;
        }while(true);

        if(verbose) Util.print_list(
          "Each undefined node could not be marked as a cycle member:" 
          ,undefined_node_list
        );

        // Reset the graph iterator to immediately top of the cycle, then return
        // as though we had hit a leaf node. (Upon return the the i0 node will
        // be dropped as part of incrementing the iterator.)
        path_stack.subList(cycle_i0 + 1 ,cycle_n + 1).clear();

        return ret_value;
      } 

