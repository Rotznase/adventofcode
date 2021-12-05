package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        Pattern pattern = Pattern.compile("(\\d+):\\s*(\\d+)");
        final Matcher m = pattern.matcher(input[input.length - 1]);
        m.find();

        int maxLayerNr = Integer.parseInt(m.group(1));
        Layer[] layers = new Layer[maxLayerNr+1];

        // Parser
        for (String line: input) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                final int depth = Integer.parseInt(matcher.group(1));
                final int range = Integer.parseInt(matcher.group(2));
                layers[depth]   = new Layer(range);
            }
        }

        int delay = 0;

        // Teil 1
        int severity = calculateSeverity(layers, delay);
        System.out.println(severity);
        reset(layers);


        // Teil 2
        delay = -1;
        while (true) {
            delay++;
            int n;
            for (n = 0; n < layers.length; n++) {
                if (layers[n] != null && (delay+n) % (2 * layers[n].range - 2 ) == 0) {
                    break;
                }
            }
            if (n == layers.length)
                break;
        }
        System.out.println(delay);

        // Gegenprobe
        severity = calculateSeverity(layers, 3921270);
        System.out.println(severity);
    }

    private int calculateSeverity(Layer[] layers, int delay) {

        // Runner
        for (int i=0; i<delay; i++) {
            nextTick(layers);
        }

        int severity = 0;
        for (int n=0; n<layers.length; n++) {
            if (layers[n] != null && layers[n].state == 0) {
                severity += n * layers[n].range;
            }

            nextTick(layers);
        }
        return severity;
    }

    private void nextTick(Layer[] layers) {
        for (Layer layer : layers) {
            if (layer != null) {
                layer.next();
            }
        }
    }

    private void reset(Layer[] layers) {
        for (Layer layer: layers) {
            if (layer != null) {
                layer.reset();
            }
        }
    }

    private class Layer {
        int range;
        int state;
        int dir;

        Layer(int range) {
            this.range = range;
            this.state = 0;
            this.dir   = +1;
        }

        void next() {
            state += dir;
            if (state == range) {
                state -= 2;
                dir = -1;
            } else if (state < 0) {
                state += 2;
                dir = +1;
            }
        }

        void reset() {
            state = 0;
            dir = +1;
        }

        @Override
        public String toString() {
            return "" + state + ", " + dir + ", " + range;
        }
    }

    private String[] input = {
            "0: 3",
            "1: 2",
            "2: 4",
            "4: 4",
            "6: 5",
            "8: 6",
            "10: 8",
            "12: 8",
            "14: 6",
            "16: 6",
            "18: 9",
            "20: 8",
            "22: 6",
            "24: 10",
            "26: 12",
            "28: 8",
            "30: 8",
            "32: 14",
            "34: 12",
            "36: 8",
            "38: 12",
            "40: 12",
            "42: 12",
            "44: 12",
            "46: 12",
            "48: 14",
            "50: 12",
            "52: 12",
            "54: 10",
            "56: 14",
            "58: 12",
            "60: 14",
            "62: 14",
            "64: 14",
            "66: 14",
            "68: 14",
            "70: 14",
            "72: 14",
            "74: 20",
            "78: 14",
            "80: 14",
            "90: 17",
            "96: 18",
//            "0: 3",
//            "1: 2",
//            "4: 4",
//            "6: 4",
    };
}
