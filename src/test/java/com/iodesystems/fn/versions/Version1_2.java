package com.iodesystems.fn.versions;

import static org.junit.Assert.assertEquals;

import com.iodesystems.fn.Fn;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class Version1_2 {

  @Test
  public void each() {
    List<Integer> result = new ArrayList<>();
    Fn<Integer> each = Fn.of(1, 2, 3).each(result::add);

    // Without consuming
    assertEquals(Fn.list(), result);

    // Consuming once
    each.consume();
    assertEquals(Fn.list(1, 2, 3), result);

    // Consuming twice
    each.consume();
    assertEquals(Fn.list(1, 2, 3, 1, 2, 3), result);
  }
}
