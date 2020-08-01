package ukitinu.dox.embed.docxml;

import ukitinu.dox.embed.utils.Interval;
import ukitinu.dox.embed.utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class XmlExplorer {
    private XmlExplorer() {
        throw new IllegalStateException("Utility class");
    }

    private static final char NUL = 0;
    private static final char QD = 34; //double quote "
    private static final char QS = 39; //single quote '
    private static final char SLASH = 47; //slash /

    static List<XmlElement> findTag(String contentXml, String tag) {
        XmlTag xmlTag = new XmlTag(tag);
        return findTag(contentXml, xmlTag);
    }

    static List<XmlElement> findTag(String contentXml, XmlTag tag) {
        String open = tag.getOpening();
        String close = tag.getClosing();
        return findTag(contentXml, tag, open, close);
    }

    static String removeTag(String contentXml, String tag) {
        return removeTag(contentXml, new XmlTag(tag));
    }

    static String removeTag(String contentXml, XmlTag tag) {
        List<Interval> toRemove = findTag(contentXml, tag).stream().map(XmlElement::getInterval).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int start = 0;
        for (Interval interval : Interval.union(toRemove)) {
            sb.append(contentXml, start, interval.getLower());
            start = interval.getUpper();
        }
        sb.append(contentXml, start, contentXml.length());
        return sb.toString();
    }

    static void removeSubs(List<XmlElement> subList, List<XmlElement> supList) {
        var subIter = subList.listIterator();
        var supIter = supList.listIterator();
        if (supIter.hasNext()) {
            XmlElement sup = supIter.next();
            while (subIter.hasNext()) {
                XmlElement sub = subIter.next();
                sup = nextSup(sub, sup, supIter);
                if (sup == null) break;
                if (isInRemove(sub, sup)) subIter.remove();
            }
        }
    }

    static int findOuterCharIndex(String contentXml, char c, int start) {
        int i = start;
        if (i >= contentXml.length()) return -1;

        char current = contentXml.charAt(i);
        char quote = NUL;
        while (current != c || quote != NUL) {
            i++;
            if (i >= contentXml.length()) return -1;

            current = contentXml.charAt(i);
            if (current == QD || current == QS) {
                if (quote == NUL) {
                    quote = current;
                } else if (quote == current) {
                    quote = NUL;
                }
            }
        }
        return i;
    }

    //region find_tag
    private static List<XmlElement> findTag(String contentXml, XmlTag tag, String open, String close) {
        List<XmlElement> tags = new LinkedList<>();
        Pattern openTag = Pattern.compile(open + "\\b");
        Matcher matcher = openTag.matcher(contentXml);

        while (matcher.find()) {
            int start = matcher.start();
            int end = findEnding(contentXml, start, open, close);

            String line = contentXml.substring(start, end);
            int closingIndex = findOuterCharIndex(line, '>', 0) + 1;
            XmlElement newTag = new XmlElement(line, tag, closingIndex, start, end);
            tags.add(newTag);
        }

        return tags;
    }

    private static int findEnding(String contentXml, int start, String open, String close) {
        int end = findSimpleEnding(contentXml, start, open);
        if (end != -1) return end;
        end = contentXml.indexOf(close, start) + close.length();
        return findEnding(contentXml, start, end, open, close);
    }

    private static int findSimpleEnding(String contentXml, int start, String open) {
        String cut = contentXml.substring(start);
        if (cut.charAt(open.length()) == '>') return -1;
        int i = findOuterCharIndex(cut, '>', open.length());
        return cut.charAt(i - 1) == SLASH ? start + i + 1 : -1;
    }

    private static int findEnding(String contentXml, int start, int end, String open, String close) {
        String section = contentXml.substring(start, end);
        int delta = countDelta(section, open, close);
        if (delta == 0) {
            return end;
        } else {
            end = findNewClose(contentXml, close, end, delta);
            return findEnding(contentXml, start, end, open, close);
        }
    }

    private static int findNewClose(String contentXml, String close, int end, int delta) {
        while (delta > 0) {
            end = contentXml.indexOf(close, end) + close.length();
            delta--;
        }
        return end;
    }

    private static int countDelta(String section, String open, String close) {
        int openings = countOpenings(section, open);
        int closings = Utils.countSubstring(section, close);
        return openings - closings;
    }

    private static int countOpenings(String section, String open) {
        Pattern pattern = Pattern.compile(open + "\\b");
        Matcher matcher = pattern.matcher(section);
        long val = matcher.results().filter(r -> findSimpleEnding(section, r.start(), open) == -1).count();
        return (int) val;
    }
    //endregion

    //region remove_subs
    private static XmlElement nextSup(XmlElement sub, XmlElement sup, ListIterator<XmlElement> supIter) {
        Interval interval = sub.getInterval();
        while (interval.isAfter(sup.getInterval())) {
            if (!supIter.hasNext()) return null;
            sup = supIter.next();
        }
        return sup;
    }

    private static boolean isInRemove(XmlElement sub, XmlElement sup) {
        Interval subInt = sub.getInterval();
        Interval supInt = sup.getInterval();
        return !subInt.isBefore(supInt) && subInt.isIn(supInt);
    }
    //endregion
}
