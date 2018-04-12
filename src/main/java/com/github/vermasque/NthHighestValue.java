package com.github.vermasque;

import java.util.Optional;

/**
 * Tracks the nth-highest value in an infinite sequence of values.
 */
public class NthHighestValue<T extends Comparable<T>> {

  private final Object[] minHeap;  
  private int heapSize = 0;

  /**
   * Create a new NthHighestValue instance based on the value of n.
   *
   * @param capacity maximum count of elements stored (the n in nth-highest)
   */
  public NthHighestValue(final int capacity) {
    this.minHeap = new Object[capacity];
  }

  /** 
   * Might return the nth-highest value if at least 
   * n values have been encountered via calls to the 
   * update method.  Provides this value in constant time.
   */
  public Optional<T> get() {
    return isHeapFull() ? Optional.of(getRoot()) : Optional.empty();
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
    if (this.heapSize == 0) {
      this.minHeap[0] = newValue;
      this.heapSize = 1;
    } else if (!isHeapFull()) {
      final int newValueIndex = this.heapSize;
      this.minHeap[newValueIndex] = newValue;
      this.heapSize++;
      
      heapifyUp(newValueIndex); 
    } else if (isGreaterThanMinValue(newValue)) { 
      this.minHeap[0] = newValue;  
      
      heapifyDown(0);
    }
  }

  @SuppressWarnings("unchecked")
  private T getRoot() {
    return (T)this.minHeap[0]; 
  }
  
  private boolean isHeapFull() {
    return this.minHeap.length == this.heapSize;
  }

  private boolean isGreaterThanMinValue(final T newValue) {
    return newValue.compareTo(getRoot()) > 0;
  }

  private void heapifyUp(final int childIndex) {
    final int parentIndex = (childIndex - 1) / 2;
    
    if (compare(childIndex, parentIndex) < 0) {
      swap(childIndex, parentIndex);  
      heapifyUp(parentIndex);       
    }
  }

  private void heapifyDown(final int parentIndex) {
    final int childIndex = getIndexOfSmallestChild(parentIndex);

    if (childIndex == -1) {
      return;
    }
    
    if (compare(childIndex, parentIndex) < 0) {
      swap(childIndex, parentIndex);  
      heapifyDown(childIndex);
    }
  }

  private int getIndexOfSmallestChild(final int parentIndex) {
    final int childIndex1 = 2 * parentIndex + 1;
    final int childIndex2 = 2 * parentIndex + 2;

    if (childIndex1 >= this.minHeap.length) {
      return -1;
    } else if (childIndex2 >= this.minHeap.length) {
      return childIndex1;
    } else {
      return compare(childIndex1, childIndex2) < 0 ? childIndex1 : childIndex2;
    }   
  }

  private void swap(final int index1, final int index2) {
    final Object value1 = this.minHeap[index1];
    this.minHeap[index1] = this.minHeap[index2];
    this.minHeap[index2] = value1;
  }

  @SuppressWarnings("unchecked")
  private int compare(final int index1, final int index2) {
    final Comparable value1 = (Comparable)this.minHeap[index1];
    final Comparable value2 = (Comparable)this.minHeap[index2];

    return value1.compareTo(value2);
  }
}
