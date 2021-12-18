package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day18 extends AdventOfCodeRunner {

    private static final Pattern pattern = Pattern.compile("\\[(\\d+),(\\d+)]");
    private static final Pattern twoOrMoreDigits = Pattern.compile("(\\d\\d+)");
    private static final Pattern oneOrMoreDigits = Pattern.compile("(\\d+)");
    private static NumberFormat nf = DecimalFormat.getIntegerInstance();


    @Override
    public void run(List<String> stringInput) {

        String snailfischSum = null;
        for (String s : stringInput) {
            if (snailfischSum == null) {
                snailfischSum = s;
            } else {
                snailfischSum = add(snailfischSum, s);
                snailfischSum = reduceAndSplit(snailfischSum);
            }
        }

        long mag = magnitude(snailfischSum);

        System.out.println("Result Part 1: "+mag);


        long maxMag = 0;
        for (String left : stringInput) {
            for (String right : stringInput) {
                snailfischSum = add(left, right);
                snailfischSum = reduceAndSplit(snailfischSum);
                maxMag = Math.max(maxMag, magnitude(snailfischSum));
                
                snailfischSum = add(right, left);
                snailfischSum = reduceAndSplit(snailfischSum);
                maxMag = Math.max(maxMag, magnitude(snailfischSum));
            }
        }
        
        System.out.println("Result Part 2: "+maxMag);
    }

    private String reduceAndSplit(String snailfischSum) {
        while (true) {
            snailfischSum = reduce(snailfischSum);
            if (snailfischSum.matches(".*\\d\\d.*")) {
                snailfischSum = split(snailfischSum);
            } else {
                break;
            }
        }
        return snailfischSum;
    }

    private static String add(String snailNrLeft, String snailNrRight) {
        return "["+snailNrLeft+","+snailNrRight+"]";
    }

    private static long magnitude(String snailfischNr) {

        if (snailfischNr.length() == 1) {
            return Long.parseLong(snailfischNr);
        }

        snailfischNr = snailfischNr.substring(1, snailfischNr.length()-1);
        int index = findSeparator(snailfischNr);
        String left = snailfischNr.substring(0, index);
        String right = snailfischNr.substring(index+1);

        long leftMag = 3 * magnitude(left);
        long rightMag = 2 * magnitude(right);

        return leftMag + rightMag;
    }

    private static int findSeparator(String snailfischNr) {
        int depth = 0;
        int foundIndex = -1;
        for (int i = 0; i < snailfischNr.length(); i++) {
            switch (snailfischNr.charAt(i)) {
                case '[': depth++; break;
                case ']': depth--; break;
                case ',':
                    if (depth == 0) {
                        foundIndex = i;
                    }
            }
        }

        return foundIndex;
    }

    private static Integer findPairWithDepth(int depth, StringBuilder sb) {
        Matcher matcher = pattern.matcher(sb);

        Integer pos = null;
        while (matcher.find()) {
            String pair = matcher.group(0);

            pos = matcher.toMatchResult().start();

            if (getDepthOfPair(sb.toString(), pair, pos) >= depth) {
                break;
            } else {
                pos = null;
            }
        }

        return pos;
    }

    private static int getDepthOfPair(String sb, String pair, int fromIndex) {
        final int indexOfPair = sb.indexOf(pair, fromIndex);
        int depth = 0;
        for (int i = 0; i < indexOfPair; i++) {
            switch (sb.charAt(i)) {
                case '[': depth++; break;
                case ']': depth--; break;
            }
        }

        return depth;
    }

    private static String reduce(String snailfishNumber) {

        Integer pos;
        StringBuilder sb = new StringBuilder(snailfishNumber);
        while ((pos = findPairWithDepth(4, sb)) != null) {

            String pair = sb.substring(pos, sb.indexOf("]", pos)+1);

            Matcher matcher = pattern.matcher(pair);
            matcher.find();

            final int left = Integer.parseInt(matcher.group(1));
            final int right = Integer.parseInt(matcher.group(2));

            int leftPos = sb.indexOf(pair, pos);
            int rightPos = leftPos + pair.length();

            int i;

            i = lastIndexOf(sb.substring(0, leftPos), oneOrMoreDigits);
            if (i >= 0) {
                ParsePosition pp = new ParsePosition(i);
                int x = nf.parse(sb.toString(), pp).intValue() + left;
                String str = Integer.toString(x, 10);
                sb.replace(i, pp.getIndex(), str);
                leftPos  += str.length() - (pp.getIndex() - i);
                rightPos += str.length() - (pp.getIndex() - i);
            }

            sb.replace(leftPos, rightPos, "0");
            rightPos -= pair.length() - 1;

            final int i1 = indexOf(sb.substring(rightPos), oneOrMoreDigits);
            i = indexOf(sb.substring(rightPos), oneOrMoreDigits);
            if (i >= 0) {
                ParsePosition pp = new ParsePosition(rightPos + i);
                int x = nf.parse(sb.toString(), pp).intValue() + right;
                String str = Integer.toString(x, 10);
                sb.replace(rightPos + i, pp.getIndex(), str);
            }
        }

        return sb.toString();
    }

    private static String split(String snailfishNumber) {

        final Matcher matcher = twoOrMoreDigits.matcher(snailfishNumber);

        StringBuilder sb = new StringBuilder(snailfishNumber);

        while (matcher.find()) {
            final MatchResult matchResult = matcher.toMatchResult();

            int x = Integer.parseInt(matchResult.group(1));
            String repl = "["+(x/2)+","+(x/2+x%2)+"]";

            sb.replace(matchResult.start(), matcher.end(), repl);
            break;
        }

        return sb.toString();
    }

    private static int indexOf(String s, Pattern pattern) {
        final Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.toMatchResult().start() : -1;
    }

    private static int lastIndexOf(String s, Pattern pattern) {
        final Matcher matcher = pattern.matcher(StringUtils.reverse(s));
        return matcher.find() ? s.length() - matcher.toMatchResult().end() : -1;
    }

}
