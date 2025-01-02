import com.ReasoningTechnology.Ariadne.Ariadne_IdentityManager;


// Example Resource Class
class ExampleResource{

  private final String name;

  public ExampleResource( String name ){
    this.name = name;
  }

  public String get_name(){
    return name;
  }

  @Override
  public String toString(){
    return "ExampleResource{name='" + name + "'}";
  }

}

class Example_IdentityManager_0 {

  public static void main( String[] args ){
    IdentityManager<ExampleResource> manager = new IdentityManager<>( "ResourceNamespace" );

    // Create resources and register them
    String name1 = manager.generate_name();
    ExampleResource resource1 = new ExampleResource( name1 );
    manager.register( name1 ,resource1 );

    String name2 = manager.generate_name();
    ExampleResource resource2 = new ExampleResource( name2 );
    manager.register( name2 ,resource2 );

    // Access resources
    System.out.println( "Resource 1: " + manager.get_resource( name1 ) );
    System.out.println( "Resource 2: " + manager.get_resource( name2 ) );

    // Print all registered resources
    System.out.println( manager );

    // Unregister a resource
    manager.unregister( name1 );
    System.out.println( "After unregistration: " + manager );
  }

}
