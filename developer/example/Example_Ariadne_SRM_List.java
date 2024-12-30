package com.ReasoningTechnology.Ariadne;

import java.util.List;

public class Main {
  public static void main( String[] args ) {
    // Create a linked list
    List<String> labels = new List<>();
    labels.add( "A" );
    labels.add( "B" );
    labels.add( "C" );

    // Attach SRMI to the linked list
    Ariadne_SRMI_List<String> srm = Ariadne_SRMI_List.attach( labels );

    // Use the SRMI
    System.out.println( "Topology: " + srm.topology() );
    System.out.println( "Status: " + srm.status() );

    // Traverse the list
    while( srm.status() != Ariadne_SRM.Status.RIGHTMOST ) {
      System.out.println( "Reading: " + srm.read() );
      srm.step();
    }

    // Final item
    System.out.println( "Reading: " + srm.read() );
    System.out.println( "Status: " + srm.status() );

    // Reset the SRM and traverse again
    srm.reset();
    System.out.println( "After reset: " + srm.read() );
  }
}
