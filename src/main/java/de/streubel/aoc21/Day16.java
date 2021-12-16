package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.leftPad;


public class Day16 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        String transmission = stringInput.get(0);

        StringBuilder sb = new StringBuilder();
        for (char hexDigit : transmission.toCharArray()) {
            final int x = Integer.parseInt("" + hexDigit, 16);
            sb.append(leftPad(Integer.toBinaryString(x), 4, '0'));
        }

        Packet packet = Packet
                .parse(new Bits(sb.toString(), 0), null,true)
                .get(0);

        List<Packet> allPackets = new ArrayList<>();
        packet.collectPackets(allPackets);

        int sum = allPackets.stream().mapToInt(Packet::getVersion).sum();
        System.out.println("Result Part 1: "+sum);


        long result = packet.getValue();
        System.out.println("Result Part 2: "+result);

    }

    private static class Bits {
        private String s;
        private int pos;

        public Bits(String s, int pos) {
            this.s = s;
            this.pos = pos;
        }

        public int length() {
            return s.length();
        }

        public String substring(int length) {
            pos += length;
            return s.substring(pos-length, pos);
        }

        public char charAt() {
            pos += 1;
            return s.charAt(pos-1);
        }

        public void skip(int length) {
            pos += (pos / 4 + length) * 4;
        }

        public boolean hasValues() {
            return pos < s.length();
        }
    }

    private static class Packet {
        int version;
        int type;
        Object payload;

        public Packet(int version, int type, Object payload) {
            this.version = version;
            this.type = type;
            this.payload = payload;
        }

        public int getVersion() {
            return version;
        }

        public int getType() {
            return type;
        }

        public void collectPackets(List<Packet> list) {
            list.add(this);
            if (payload instanceof List) {
                for (Packet p : (List<Packet>) payload) {
                    p.collectPackets(list);
                }
            }
        }

        public long getValue() {
            long result;
            switch (type) {
                case 0: //sum
                    result = 0;
                    for (Packet p : (List<Packet>) payload) {
                        result += p.getValue();
                    }
                    break;
                case 1: // pruduct
                    result = 1;
                    for (Packet p : (List<Packet>) payload) {
                        result *= p.getValue();
                    }
                    break;
                case 2: // minimum
                    result = Long.MAX_VALUE;
                    for (Packet p : (List<Packet>) payload) {
                        result = Math.min(result, p.getValue());
                    }
                    break;
                case 3: // maximum
                    result = Long.MIN_VALUE;
                    for (Packet p : (List<Packet>) payload) {
                        result = Math.max(result, p.getValue());
                    }
                    break;
                case 4: // value
                    result = (long) payload;
                    break;
                case 5: // greater than
                    Packet left = (Packet) ((List<?>) payload).get(0);
                    Packet right = (Packet) ((List<?>) payload).get(1);
                    result = left.getValue() > right.getValue() ? 1 : 0;
                    break;
                case 6: // less than
                    left = (Packet) ((List<?>) payload).get(0);
                    right = (Packet) ((List<?>) payload).get(1);
                    result = left.getValue() < right.getValue() ? 1 : 0;
                    break;
                case 7: // less than
                    left = (Packet) ((List<?>) payload).get(0);
                    right = (Packet) ((List<?>) payload).get(1);
                    result = left.getValue() == right.getValue() ? 1 : 0;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }

            return result;
        }

        static List<Packet> parse(Bits bits, Integer packet, boolean padding) {

            List<Packet> packets = new ArrayList<>();


            while (bits.hasValues() && (packet == null || packet > 0)) {

                // Header 1/2
                int version = Integer.parseInt(bits.substring(3), 2);

                // Header 2/2
                int type = Integer.parseInt(bits.substring(3), 2);

                // Content
                long literal;
                switch (type) {
                    case 4: // literal value
                        StringBuilder sb = new StringBuilder();
                        while (bits.charAt() == '1') {
                            sb.append(bits.substring(4));
                        }

                        sb.append(bits.substring(4));
                        literal = Long.parseLong(sb.toString(), 2);

                        packets.add(new Packet(version, type, literal));

                        if (padding) {
                            bits.skip(1);
                        }
                        break;

                    default: // operator
                        int length;
                        List<Packet> operands = new LinkedList<>();

                        if (bits.charAt() == '0') {
                            length = 15;      // 15-bit number represents total length of bits
                            long subPacketLength = Long.parseLong(bits.substring(length), 2);

                            String s = bits.substring((int) subPacketLength);
                            operands.addAll(parse(new Bits(s, 0), null,false));
                        } else {
                            length = 11;      // 11-bit number represents number of sub-packets
                            long nrOfSubPackets = Long.parseLong(bits.substring(length), 2);

                            for (int n = 0; n < nrOfSubPackets; n++) {
                                operands.addAll(parse(bits, 1, false));
                            }
                        }

                        packets.add(new Packet(version, type, operands));

                        if (padding) {
                            bits.skip(2);      // Warum 2?
                        }
                        break;
                }

                if (packet != null) {
                    packet--;
                }
            }

            return packets;
        }
    }

}
