package zapsdtest.testframework.containers.sequences;

import apsd.interfaces.containers.sequences.Vector;
import apsd.classes.utilities.Natural;

import zapsdtest.testframework.containers.base.ReallocableContainerTest;

import static org.junit.jupiter.api.Assertions.*;

public interface VectorTest<Data, Con extends Vector<Data>> extends MutableSequenceTest<Data, Con>, ReallocableContainerTest<Con> {

  default void TestShiftLeft(Natural position, Natural number) {
    BeginTest("ShiftLeft");
    long initialSize = ThisContainer().Size().ToLong();
    if (position.ToLong() >= initialSize) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftLeft(position, number),
      "ShiftLeft should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftLeft(position, number);
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftLeft");
      Natural current = Natural.Of(initialSize - 1);
      for(long num = 0; num < number.ToLong(); num++, current = current.Decrement()) {
        assertNull(ThisContainer().GetAt(current), "Position " + current + " should be null");
      }
    }
    EndTest();
  }

  default void TestShiftLeft(Natural position) {
    BeginTest("ShiftLeft");
    long initialSize = ThisContainer().Size().ToLong();
    if (position.ToLong() >= initialSize) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftLeft(position),
      "ShiftLeft should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftLeft(position);
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftLeft");
      assertNull(ThisContainer().GetLast(), "Last position should be null");

    }
    EndTest();
  }

  default void TestShiftFirstLeft() {
    BeginTest("ShiftFirstLeft");
    long initialSize = ThisContainer().Size().ToLong();
    if (initialSize == 0) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftFirstLeft(),
      "ShiftFirstLeft should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftFirstLeft();
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftFirstLeft");
      assertNull(ThisContainer().GetLast(), "Last position should be null");
    }
    EndTest();
  }

  default void TestShiftLastLeft() {
    BeginTest("ShiftLastLeft");
    long initialSize = ThisContainer().Size().ToLong();
    if (initialSize == 0) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftLastLeft(),
      "ShiftLastLeft should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftLastLeft();
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftLastLeft");
      assertNull(ThisContainer().GetLast(), "Last position should be null");
    }
    EndTest();
  }

  default void TestShiftRight(Natural position, Natural number) {
    BeginTest("ShiftRight");
    long initialSize = ThisContainer().Size().ToLong();
    if (position.ToLong() >= initialSize) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftRight(position, number),
      "ShiftRight should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftRight(position, number);
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftRight");
      Natural current = position;
      for(long num = 0; num < number.ToLong(); num++, current = current.Increment()) {
        assertNull(ThisContainer().GetAt(current), "Position " + current + " should be null");
      }
    }
    EndTest();
  }

  default void TestShiftRight(Natural position) {
    BeginTest("ShiftRight");
    long initialSize = ThisContainer().Size().ToLong();
    if (position.ToLong() >= initialSize) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftRight(position),
      "ShiftRight should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftRight(position);
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftRight");
      assertNull(ThisContainer().GetAt(position), "Position " + position + " should be null");
    }
    EndTest();
  }

  default void TestShiftFirstRight() {
    BeginTest("ShiftFirstRight");
    long initialSize = ThisContainer().Size().ToLong();
    if (initialSize == 0) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftFirstRight(),
      "ShiftFirstRight should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftFirstRight();
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftFirstRight");
      assertNull(ThisContainer().GetFirst(), "First position should be null");
    }
    EndTest();
  }

  default void TestShiftLastRight() {
    BeginTest("ShiftLastRight");
    long initialSize = ThisContainer().Size().ToLong();
    if (initialSize == 0) {
      assertThrows(IndexOutOfBoundsException.class, () -> ThisContainer().ShiftLastRight(),
      "ShiftLastRight should throw exception when sequence is empty");
    } else {
      ThisContainer().ShiftLastRight();
      assertEquals(initialSize, ThisContainer().Size().ToLong(),
      "Size should be preserved after ShiftLastRight");
      assertNull(ThisContainer().GetLast(), "Last position should be null");
    }
    EndTest();
  }

}
