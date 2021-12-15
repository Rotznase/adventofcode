package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;


public class Day15 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int maxX = stringInput.get(0).length();
        int maxY = stringInput.size();

        Coords[][] riskMap_1 = new Coords[maxY][];

        for (int row = 0; row < maxY; row++) {
            riskMap_1[row] = new Coords[maxX];
            for (int col = 0; col < maxX; col++) {
                int risk = Character.digit(stringInput.get(row).toCharArray()[col], 10);
                riskMap_1[row][col] = new Coords(row, col, risk);
            }
        }


        Node end;

        end = xxx(maxX, maxY, riskMap_1);
        System.out.println("Result Part 1 (508): "+end.getDistance());

        int N = 5;
        Coords[][] riskMap_2 = new Coords[N*maxY][];

        for (int row = 0; row < riskMap_2.length; row++) {
            riskMap_2[row] = new Coords[N*maxX];
            for (int col = 0; col < riskMap_2.length; col++) {
                Coords old = riskMap_1[row % maxY][col % maxX];
                int risk = (old.risk + (col / maxX) + (row / maxY)) % 9;
                if (risk == 0) {
                    risk = 9;
                }
                riskMap_2[row][col] = new Coords(row, col, risk);
            }
        }


        end = xxx(N*maxX, N*maxY, riskMap_2);
        System.out.println("Result Part 2 (2872): "+end.getDistance());
    }

    private Node xxx(int maxX, int maxY, Coords[][] riskMap) {
        Graph graph = new Graph();

        Node[][] x = new Node[maxY][];
        for (int row = 0; row < maxY; row++) {
            x[row] = new Node[maxX];
            for (int col = 0; col < maxX; col++) {
                Node node = new Node(row+","+col);
                graph.addNode(node);
                x[row][col] = node;
            }
        }

        for (int row = 0; row < maxY; row++) {
            for (int col = 0; col < maxX; col++) {
                Node node = x[row][col];
                for (Coords coords : proximity(riskMap[row][col], riskMap)) {
                    final Node node1 = x[coords.row][coords.col];
                    node.addDestination(node1, coords.risk);
                }
            }
        }


        Node start = graph.nodes.stream()
                .filter(node -> node.name.equals(0 + "," + 0))
                .findFirst()
                .orElse(null);

        graph = calculateShortestPathFromSource(graph, start);

        final Node end = graph.nodes.stream()
                .filter(n -> n.name.equals((maxY -1) + "," + (maxX -1)))
                .findFirst()
                .orElse(null);
        return end;
    }


    private static List<Coords> proximity(Coords location, Coords[][] riskMap) {

        Coords south = null;
        if (location.row + 1 < riskMap.length) {
            south = riskMap[location.row+1][location.col];
        }

        Coords east = null;
        if (location.col + 1 < riskMap[0].length) {
            east = riskMap[location.row][location.col+1];
        }

        List<Coords> proximity;

        if (east != null && south != null) {
            if (east.getRisk() < south.getRisk()) {
                proximity = Arrays.asList(east, south);
            } else {
                proximity = Arrays.asList(south, east);
            }
        } else if (east != null) {
            proximity = Collections.singletonList(east);
        } else if (south != null) {
            proximity = Collections.singletonList(south);
        } else {
            proximity = Collections.emptyList();
        }

        return proximity;
    }
    
    private static class Coords {
        int row;
        int col;
        int risk;

        public Coords(int row, int col, int risk) {
            this.row = row;
            this.col = col;
            this.risk = risk;
        }

        public int getRisk() {
            return risk;
        }

        @Override
        public String toString() {
            return "" + row + "," + col + "," + risk;
        }
    }


    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {

        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeigh = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static class Graph {

        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

        public Set<Node> getNodes() {
            return nodes;
        }

        public void setNodes(Set<Node> nodes) {
            this.nodes = nodes;
        }
    }


    private static class Node {

        private String name;

        private LinkedList<Node> shortestPath = new LinkedList<>();

        private Integer distance = Integer.MAX_VALUE;

        private Map<Node, Integer> adjacentNodes = new HashMap<>();

        public Node(String name) {
            this.name = name;
        }

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }

        public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(LinkedList<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
