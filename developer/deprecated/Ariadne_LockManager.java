/*


*/


package com.ReasoningTechnology.Ariadne;

public interface Ariadne_LockManager{
  Ariadne_LockManagerDelegate<T>  single_thread(Object resource);
  Ariadne_LockManagerDelegate<T>  multiple_thread(Object resource);
}
