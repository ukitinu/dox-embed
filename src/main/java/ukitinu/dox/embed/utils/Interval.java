package ukitinu.dox.embed.utils;

import java.util.*;
import java.util.stream.Collectors;

public class Interval {
    private final int lower;
    private final int upper;

    public Interval(int lower, int upper) {
        if (upper < lower) throw new IllegalArgumentException("Invalid endpoints: " + upper + " < " + lower);
        this.lower = lower;
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    public int size() {
        return upper - lower;
    }

    public boolean isIn(Interval interval) {
        return interval.lower <= this.lower && this.upper <= interval.upper;
    }

    public boolean isOut(Interval interval) {
        return this.upper < interval.lower || interval.upper < this.lower;
    }

    public boolean isBefore(Interval interval) {
        return this.upper < interval.lower;
    }

    public boolean isAfter(Interval interval) {
        return interval.upper < this.lower;
    }

    public static List<Interval> union(Interval i, Interval j) {
        Interval a = i.lower <= j.lower ? i : j;
        Interval b = i.lower <= j.lower ? j : i;
        if (a.upper <= b.lower) {
            if (a.upper < b.lower) return new LinkedList<>(List.of(a, b));
            return new LinkedList<>(List.of(new Interval(a.lower, b.upper)));
        } else { //b.low < a.up
            if (a.upper <= b.upper) return new LinkedList<>(List.of(new Interval(a.lower, b.upper)));
            return new LinkedList<>(List.of(a));
        }
    }

    public static List<Interval> union(List<? extends Interval> intervals) {
        if (intervals == null || intervals.isEmpty()) return Collections.emptyList();
        if (intervals.size() == 1) return Collections.singletonList(intervals.get(0));
        if (intervals.size() == 2) return union(intervals.get(0), intervals.get(1));

        List<Interval> ordered = intervals.stream().sorted(Comparator.comparingInt(Interval::getLower)).collect(Collectors.toList());
        List<Interval> union01 = new LinkedList<>(union(ordered.get(0), ordered.get(1)));

        if (union01.size() == 1) {
            union01.addAll(ordered.subList(2, ordered.size()));
            return union(union01);
        } else {
            List<Interval> union = union(ordered.subList(1, ordered.size()));
            union.add(0, ordered.get(0));
            return union;
        }
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", lower, upper);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;
        Interval interval = (Interval) o;
        return lower == interval.lower &&
                upper == interval.upper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
