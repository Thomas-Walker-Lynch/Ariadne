/*
  IndexTree_SRTM_Diagonal

  An index tree is infinite.

  A tree diagonal consists of:
  a) a node descending from each child discovered thus far
  b) a node extending each child list discovered thus far.

  Hence, each diagonal extends the tree down one and over one.
*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import com.ReasoningTechnology.Ariadne.IndexTree_Node;

public class IndexTree_SRTM_Diagonal extends Ariadne_SRTM_Label {

  // Static

  public static IndexTree_SRTM_Diagonal make(){
    return new IndexTree_SRTM_Diagonal();
  }

  // Instance Data

  private final List<Ariadne_Label> list_of__unopened_node;
  private final List<List<Ariadne_Label>> list_of__opened_incomplete_child_list;
  private final List<Ariadne_Label> read_list;
  private final Ariadne_SRTM_Label breadth_srm;

  // Constructor

  protected IndexTree_SRTM_Diagonal(){
    list_of__unopened_node = new ArrayList<>();
    list_of__opened_incomplete_child_list = new ArrayList<>();
    read_list = new ArrayList<>();
    breadth_srm = Ariadne_SRTM_Label.make();
    enqueue_root();
  }

  // Instance Methods

  private void enqueue_root(){
    IndexTree_Label root_label = IndexTree_Label.root();
    read_list.add( root_label );

    IndexTree_Node root_node = lookup( root_label );
    breadth_srm.mount( root_node.neighbor() );

    if( breadth_srm.can_read() ){
      list_of__unopened_node.add( root_label );
    }
  }

  private IndexTree_Node lookup( Ariadne_Label label ){
    return IndexTree_Node.make( (IndexTree_Label)label );
  }

  @Override
  public List<Ariadne_Label> read(){
    return read_list;
  }

  @Override
  public void step(){
    read_list.clear();

    // Process unopened nodes
    while( !list_of__unopened_node.isEmpty() ){
      Ariadne_Label label = list_of__unopened_node.remove( 0 );

      // Retrieve the node using lookup
      IndexTree_Node node = lookup( label );

      // Mount a new breadth-first SRTM for children
      breadth_srm.mount( node.neighbor() );

      if( breadth_srm.can_read() ){
        do{
          Ariadne_Label child_label = breadth_srm.read();
          list_of__unopened_node.add( child_label );
          list_of__opened_incomplete_child_list.add( new ArrayList<>( List.of( child_label ) ) );

          breadth_srm.step();
        }while( breadth_srm.can_step() );
      }
    }

    // Process incomplete child lists
    while( !list_of__opened_incomplete_child_list.isEmpty() ){
      List<Ariadne_Label> child_list = list_of__opened_incomplete_child_list.remove( 0 );
      if( !child_list.isEmpty() ){
        Ariadne_Label label = child_list.remove( 0 );
        read_list.add( label );

        IndexTree_Node node = lookup( label );
        breadth_srm.mount( node.neighbor() );

        if( breadth_srm.can_read() ){
          list_of__unopened_node.add( label );
        }
      }
    }
  }
}
