package ukitinu.dox.embed.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    private static final Interval I_0 = new Interval(0, 10);

    private static final Interval I_1 = new Interval(-5, -2);
    private static final Interval I_2 = new Interval(-3, 0);
    private static final Interval I_3 = new Interval(-2, 2);
    private static final Interval I_4 = new Interval(0, 4);
    private static final Interval I_5 = new Interval(1, 5);
    private static final Interval I_6 = new Interval(7, 10);
    private static final Interval I_7 = new Interval(7, 12);
    private static final Interval I_8 = new Interval(10, 12);
    private static final Interval I_9 = new Interval(12, 15);


    @Test
    void newInterval() {
        assertThrows(IllegalArgumentException.class, () -> new Interval(1, 0));
        assertDoesNotThrow(() -> new Interval(0, 0));
        assertDoesNotThrow(() -> new Interval(0, 1));
    }

    @Test
    void size() {
        assertEquals(10, new Interval(0, 10).size());
        assertEquals(9, new Interval(1, 10).size());
    }

    @Test
    void isIn() {
        assertFalse(I_1.isIn(I_0));
        assertFalse(I_2.isIn(I_0));
        assertFalse(I_3.isIn(I_0));
        assertTrue(I_4.isIn(I_0));
        assertTrue(I_5.isIn(I_0));
        assertTrue(I_6.isIn(I_0));
        assertFalse(I_7.isIn(I_0));
        assertFalse(I_8.isIn(I_0));
        assertFalse(I_9.isIn(I_0));
    }

    @Test
    void isOut() {
        assertTrue(I_1.isOut(I_0));
        assertFalse(I_2.isOut(I_0));
        assertFalse(I_3.isOut(I_0));
        assertFalse(I_4.isOut(I_0));
        assertFalse(I_5.isOut(I_0));
        assertFalse(I_6.isOut(I_0));
        assertFalse(I_7.isOut(I_0));
        assertFalse(I_8.isOut(I_0));
        assertTrue(I_9.isOut(I_0));
    }

    @Test
    void isBefore() {
        assertTrue(I_1.isBefore(I_0));
        assertFalse(I_2.isBefore(I_0));
        assertFalse(I_3.isBefore(I_0));
        assertFalse(I_4.isBefore(I_0));
        assertFalse(I_5.isBefore(I_0));
        assertFalse(I_6.isBefore(I_0));
        assertFalse(I_7.isBefore(I_0));
        assertFalse(I_8.isBefore(I_0));
        assertFalse(I_9.isBefore(I_0));
    }

    @Test
    void isAfter() {
        assertFalse(I_1.isAfter(I_0));
        assertFalse(I_2.isAfter(I_0));
        assertFalse(I_3.isAfter(I_0));
        assertFalse(I_4.isAfter(I_0));
        assertFalse(I_5.isAfter(I_0));
        assertFalse(I_6.isAfter(I_0));
        assertFalse(I_7.isAfter(I_0));
        assertFalse(I_8.isAfter(I_0));
        assertTrue(I_9.isAfter(I_0));
    }

    @Test
    void union_2() {
        List<Interval> union01 = Interval.union(I_0, I_1);
        assertEquals(2, union01.size());
        assertTrue(union01.contains(I_0) && union01.contains(I_1));

        List<Interval> union02 = Interval.union(I_0, I_2);
        assertEquals(1, union02.size());
        assertEquals(new Interval(-3, 10), union02.get(0));

        List<Interval> union03 = Interval.union(I_0, I_3);
        assertEquals(1, union03.size());
        assertEquals(new Interval(-2, 10), union03.get(0));

        List<Interval> union04 = Interval.union(I_0, I_4);
        assertEquals(1, union04.size());
        assertEquals(I_0, union04.get(0));

        List<Interval> union05 = Interval.union(I_0, I_5);
        assertEquals(1, union05.size());
        assertEquals(I_0, union05.get(0));

        List<Interval> union07 = Interval.union(I_0, I_7);
        assertEquals(1, union07.size());
        assertEquals(new Interval(0, 12), union07.get(0));

        List<Interval> union08 = Interval.union(I_0, I_8);
        assertEquals(1, union08.size());
        assertEquals(new Interval(0, 12), union08.get(0));

        List<Interval> union09 = Interval.union(I_0, I_9);
        assertEquals(2, union09.size());
        assertTrue(union09.contains(I_0) && union09.contains(I_9));
    }

    @Test
    void union_multi() {
        List<Interval> union0 = Interval.union(List.of(I_0, I_1, I_2, I_3, I_4, I_5, I_6, I_7, I_8, I_9));
        assertEquals(1, union0.size());
        assertEquals(new Interval(-5, 15), union0.get(0));

        List<Interval> union1 = Interval.union(List.of(I_0, I_1, I_4, I_5, I_6, I_9));
        assertEquals(3, union1.size());
        assertTrue(union1.contains(I_1) && union1.contains(I_0) && union1.contains(I_9));

        List<Interval> union2 = Interval.union(List.of(I_0, I_2, I_3, I_4, I_5, I_6, I_7, I_8));
        assertEquals(1, union2.size());
        assertEquals(new Interval(-3, 12), union2.get(0));

        List<Interval> union3 = Interval.union(List.of(I_1, I_3, I_7, I_8));
        assertEquals(2, union3.size());
        assertTrue(union3.contains(new Interval(-5, 2)) && union3.contains(new Interval(7, 12)));
    }

    @Test
    void testToString() {
        Interval i1 = new Interval(0, 2);
        Interval i2 = new Interval(-1, 10);
        assertEquals("[0, 2]", i1.toString());
        assertEquals("[-1, 10]", i2.toString());
    }

    @Test
    void testEquals() {
        Interval i1 = new Interval(0, 1);
        Interval i2 = new Interval(0, 1);
        Interval i3 = new Interval(-1, 1);
        assertEquals(i1, i2);
        assertEquals(i2, i1);
        assertNotEquals(i1, i3);
        assertNotEquals(i3, i2);
    }
}