package com.github.vermasque;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

public class NthHighestValueTest {

  @Test
  public void sequenceLargerThanN() {
    final var thirdHighestValueTracker = new NthHighestValue<Integer>(3);

    Stream
      .of(2, 1, 7, 8, 4)
      .forEach(thirdHighestValueTracker::update);

    final Optional<Integer> thirdHighestValue = thirdHighestValueTracker.get(); 

    assertTrue(thirdHighestValue.isPresent());
    assertEquals(4, thirdHighestValue.get().intValue());
  }

  @Test(expected = NullPointerException.class)
  public void rejectNull() {
    final var secondHighestValueTracker = new NthHighestValue<Integer>(2);

    secondHighestValueTracker.update(null);
  }
}
