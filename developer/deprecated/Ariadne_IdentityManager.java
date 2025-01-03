/*
General purpose identity manager. Gives names to things.


*/
package com.ReasoningTechnology.Ariadne;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// General-purpose Identity Manager
public class IdentityManager<T> implements Ariadne_ResourceIdentity<T>{

  private final String namespace;
  private final AtomicInteger counter;
  private final ConcurrentHashMap<String ,ResourceIdentity> registry;

  public IdentityManager( String namespace ){
    if( namespace == null || namespace.isEmpty() ){
      throw new IllegalArgumentException( "IdentityManager::namespace cannot be null or empty." );
    }
    this.namespace = namespace;
    this.counter = new AtomicInteger(0);
    this.registry = new ConcurrentHashMap<>();
  }

  public synchronized String generate_name(){
    return namespace + "_" + counter.incrementAndGet();
  }

  public synchronized void register( ResourceIdentity resource ){
    if( resource == null ){
      throw new IllegalArgumentException( "IdentityManager::resource cannot be null." );
    }

    String name = resource.get_name();
    if( name == null ){
      name = generate_name();
      resource.set_name( name );
    }
    resource.lock_name();

    if( registry.containsKey( name ) ){
      throw new IllegalArgumentException( "IdentityManager::name already exists: " + name );
    }

    registry.put( name ,resource );
  }

  public synchronized ResourceIdentity get_resource( String name ){
    return registry.get( name );
  }

  public synchronized void unregister( String name ){
    registry.remove( name );
  }

  @Override
  public String toString(){
    return "IdentityManager{namespace='" + namespace + "' ,registeredResources=" + registry.keySet() + "}";
  }

}
