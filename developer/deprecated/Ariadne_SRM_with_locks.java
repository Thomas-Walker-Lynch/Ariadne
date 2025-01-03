/*
Step Right Machine

This is a mostly abstract base class.

The SRM is for a single traversal through a bound resources.

In Java it is possible to declare a variable of the SRM type long before it is used
for traversal. This complicates managing ownwership of the resource being traversed.

The SRM model is that of 'mount' and 'dismount'.  When the resource is mounted,
the SRM requests ownership of it. When it is dismounted, the SRM relinquishes
ownership.



mount and unmount are used for handling shared memory scenarios.  See
the Ariadne_Access class. For single threaded execution pass in an
Ariadne_Access_Single instance.

*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRM<T>{

  public enum Topology{
    NO_CELLS
    ,SEGMENT
    ,CIRCLE
    ,INFINITE_RIGHT
    ,INFINITE_LEFT
    ,INFINITE
    ,UNKNOWN
    ,UNDEFINED
    ;
  }

  public Topology topology(){
    return Topology.UNDEFINED;
  }

  public enum Status{
    TAPE_NOT_MOUNTED
    ,LEFTMOST
    ,INTERIM
    ,RIGHTMOST
    ;
  }

  public Status status(){
    throw new UnsupportedOperationException("Ariadne_SRM::status not implemented.");
  }

  public boolean can_step(){
    return 
      status() == Status.LEFTMOST
      || status() == Status.INTERIM;
  }

  public boolean can_read(){
    return status() != Status.TAPE_NOT_MOUNTED;
  }

  public static <T> Ariadne_SRM<T> make(LockManagerDelegate<T> delegate){
    if(delegate == null){
      throw new IllegalArgumentException("Ariadne_SRM::make delegate cannot be null.");
    }
    return new Ariadne_SRM<>(delegate);
  }

  private Ariadne_SRM(LockManagerDelegate<T> delegate){
    this.delegate = delegate;
  }

  public void mount(){
    if(status() != Status.TAPE_NOT_MOUNTED){
      throw new IllegalStateException("Ariadne_SRM::mount already mounted.");
    }
    delegate.request(this);
  }
  public void mount_lenient() {
    if(status() != Status.TAPE_NOT_MOUNTED) {
      dismount(); // Ensure the current tape is dismounted first
    }
    mount(); // Proceed to mount the new tape
  }

  public void dismount(){
    if(status() == Status.TAPE_NOT_MOUNTED){
      throw new IllegalStateException("Ariadne_SRM::dismount not mounted.");
    }
    delegate.relinquish(this);
  }
  public void dismount_lenient() {
    if(status() != Status.TAPE_NOT_MOUNTED) {
      dismount(); // Only dismount if a tape is currently mounted
    }
  }

  public T read(){
    throw new UnsupportedOperationException("Ariadne_SRM::read not implemented.");
  }

  public void step(){
    throw new UnsupportedOperationException("Ariadne_SRM::step not implemented.");
  }
}
