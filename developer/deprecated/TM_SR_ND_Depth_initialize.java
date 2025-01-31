  // Given a context_path_tm and path_member_set.
  // Initializes the path_member_set. Leaves head on last cell of context_path_tm.
  // Returns a successful initlaization flag and possibly not null cycle_node_label
  public class initialize{
    static public initialize f(context_path_tm ,path_member_set){
      initialize instance = new initialize();
      instance.g(context_path_tm ,path_member_set);
      return instance;
    }
    public Label first_in_path_cycle_node_label = null;
    public boolean success = false;

    protected void g(context_path_tm ,path_member_set){

      if( context_path.isEmpty() ){
        System.out.println("TM_SR_NX_Depth::initialize required context_path is empty.");
        success = false;
        return;
      }

      boolean is_cycle_node = false;
      Ariadne_TM_SR_NX sibling_tm = null;
      Ariadne_Label path_node_label = null;

      do{
        sibling_tm = context_path_tm.read();
        path_node_label = sibling_tm.read();
        if(path_node_label == null){
          System.out.println("TM_SR_NX_Depth::complete_context_path hit null path label on path");
          success = false;
          return;
        }

        is_cycle_node = path_member_set.contains(path_node_label);
        if( is_cycle_node ) break;

        path_member_set.add( path_node_label );

        if( !context_path_tm.can_step() ) break;
        context_path_tm.step();

      }while(true);

      if( is_cycle_node && context_path_tm.can_step() ){
        System.out.println
          (
           "TM_SR_NX_Depth::initialize_path_member_set: cycle found in initial context_path."
           );
        first_in_path_cycle_node_label = path_node_label;
        success = false;
        return;
      }

      success = true;
      return;
    }
  }
