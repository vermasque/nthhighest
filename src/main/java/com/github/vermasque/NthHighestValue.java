package com.github.vermasque;

import java.util.Optional;

/**
 * Tracks the nth-highest value in an infinite sequence of values.
 */
public class NthHighestValue<T extends Comparable> {

  private static final int INDEX_UNDEFINED = -1;

  private final Object[] maxHeap;  
  private int heapSize = 0;
  private int minValueIndex = INDEX_UNDEFINED;

  /**
   * Create a new NthHighestValue instance based on the value of n.
   *
   * @param capacity maximum count of elements stored (the n in nth-highest)
   */
  public NthHighestValue(final int capacity) {
    this.maxHeap = new Object[capacity];
  }

  /** 
   * Might return the nth-highest value if at least 
   * n values have been encountered via calls to the 
   * update method.  Provides this value in constant time.
   */
  public Optional<T> get() {
    return isHeapFull() ? Optional.of(getMinValue()) : Optional.empty();
  }

  /**
   * Potentially update this instance with a new value
   * that would fall in the nth-highest values.  If n
   * values have already been encountered but this new
   * value should fall in the nth-highest values, it 
   * will replace one of the values that have already been
   * encountered.  
   * 
   * <p>An update operates in constant time in the best case
   * and logarthmic time (relative to n) in the worst case.
   */
  public void update(final T newValue) {
    int newValueIndex = INDEX_UNDEFINED; 

    if (this.heapSize == 0) {
      newValueIndex = 0;
      this.maxHeap[newValueIndex] = newValue;
      this.heapSize = 1;
    } else if (this.heapSize < maxHeap.length) {
      newValueIndex = heapSize;
      this.maxHeap[newValueIndex] = newValue;
      this.heapSize++;
      
      heapifyUp(newValueIndex); 
    } else if (isGreaterThanNthValue(newValue)) { // heap full
      
       
    } 

    // if new value put in the heap (added or replaced)
    if (newValueIndex != INDEX_UNDEFINED) {
      final T currentMinValue = getMinValue();
      
      if (currentMinValue.compareTo(newValue) > 0) {
        this.minValueIndex = newValueIndex;
      }
    }
  }
  
  private boolean isHeapFull() {
    return this.maxHeap.length == this.heapSize;
  }

  @SuppressWarnings("unchecked")
  private T getMinValue() {
    return (T)this.maxHeap[this.minValueIndex];
  }

  private void heapifyUp(final int childIndex) {
  
  }

  private boolean isGreaterThanNthValue(T obj) {
    return false; 
  }
}
