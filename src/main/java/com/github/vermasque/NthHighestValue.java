package com.github.vermasque;

import java.util.Objects;
import java.util.Optional;

/**
 * Tracks the nth-highest value in a potentially infinite sequence of values.
 *
 * <p>The nth-highest value of a sequence of values is the same as the lowest
 * value in the top n values of the sequence.  A min heap is used internally
 * to track the top n values and then provide the nth-highest value efficiently
 * by simply asking the min heap for its min value.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Binary_heap">How heaps work</a>
 */
public class NthHighestValue<T extends Comparable<T>> {

  private static final int NO_INDEX = -1;

  /**
   * Can't create generic arrays so forced to use non-generic type instead of T[].
   * At runtime, types are checked to prevent the wrong type of value from being
   * set on the array.  Can't do that with generic arrays likely due to type erasure.
   *
   * @see <a href="https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createArrays">Restrictions on Generics</a>
   */
  private final Comparable[] minHeap;
  private int heapSize = 0;

  /**
   * Create a new NthHighestValue instance based on the value of n.
   *
   * @param capacity maximum count of elements stored (the n in nth-highest)
   */
  public NthHighestValue(final int capacity) {
    this.minHeap = new Comparable[capacity];
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
   * <p>Null values are not supported because the comparison
   * is odd (null is less than a non-null?).  Also, Comparable
   * requires an exception to be thrown when comparing to null.
   * 
   * <p>An update operates in constant time in the best case
   * and logarithmic time (relative to n) in the worst case.
   */
  public void update(final T newValue) {
    Objects.requireNonNull(newValue);

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
  private T getMinValue() {
    return (T)this.minHeap[0]; 
  }
  
  private boolean isHeapFull() {
    return this.minHeap.length == this.heapSize;
  }

  private boolean isGreaterThanMinValue(final T newValue) {
    return newValue.compareTo(getMinValue()) > 0;
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

    if (childIndex == NO_INDEX) {
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
      return NO_INDEX;
    } else if (childIndex2 >= this.minHeap.length) {
      return childIndex1;
    } else {
      return compare(childIndex1, childIndex2) < 0 ? childIndex1 : childIndex2;
    }   
  }

  private void swap(final int index1, final int index2) {
    final Comparable value1 = this.minHeap[index1];
    this.minHeap[index1] = this.minHeap[index2];
    this.minHeap[index2] = value1;
  }

  private int compare(final int index1, final int index2) {
    final Comparable value1 = this.minHeap[index1];
    final Comparable value2 = this.minHeap[index2];

    return value1.compareTo(value2);
  }
}
