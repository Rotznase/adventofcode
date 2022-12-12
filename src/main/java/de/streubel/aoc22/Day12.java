package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class Day12 extends AdventOfCodeRunner {

    /*
        Der "Kürzester Pfad"-Algorithmus stammt on https://www.baeldung.com/java-a-star-pathfinding.
     */

    @Override
    public void run(List<String> stringInput) {

        Node S = null;
        Node E = null;
        Set<Node> nodes = new HashSet<>();

        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < stringInput.size(); y++) {
            maxY = max(maxY, y);
            char[] row = stringInput.get(y).toCharArray();
            for (int x = 0; x < row.length; x++) {
                maxX = max(maxX, x);
                char elevation = row[x];

                final Node node;

                if (elevation == 'S') {
                    elevation = 'a';
                    S = new Node(elevation, x, y);
                    node = S;
                } else if (elevation == 'E') {
                    elevation = 'z';
                    E = new Node(elevation, x, y);
                    node = E;
                } else {
                    node = new Node(elevation, x, y);
                }

                nodes.add(node);
            }
        }

        final Map<String, Set<String>> connections = new HashMap<>();
        for (Node node : nodes) {
            for (String neigbourId : node.getNeigbourhood().stream().map(Node::getId).toList()) {
                Node neigbour = nodes.stream().filter(node1 -> node1.getId().equals(neigbourId)).findFirst().orElse(null);
                if (neigbour != null &&
                    neigbour.getX() >= 0 && neigbour.getX() <= maxX &&
                    neigbour.getY() >= 0 && neigbour.getY() <= maxY &&
                    neigbour.getElevation() - node.getElevation() >= 0 &&
                    neigbour.getElevation() - node.getElevation() <= 1
                ) {
                    connections.computeIfAbsent(node.getId(), s -> new HashSet<>()).add(neigbour.getId());
                }
            }
        }

        final Graph<Node> graph = new Graph<>(nodes, connections);
        final Scorer<Node> nextNodeScorer = (from, to) -> Math.sqrt((from.x - to.x) * (from.x - to.x) + (from.y - to.y) * (from.y - to.y));
        final Scorer<Node> targetScorer = (from, to) -> Math.sqrt((from.x - to.x) * (from.x - to.x) + (from.y - to.y) * (from.y - to.y));
        final RouteFinder<Node> routeFinder = new RouteFinder<>(graph, nextNodeScorer, targetScorer);


        final List<Node> route1 = routeFinder.findRoute(S, E);
        int resultPart1 = route1.size() - 1;
        System.out.println("Result Part 1 (490): " + resultPart1);


        final Node targetNode = E;
        final List<Node> route2 = nodes.stream()
                .filter(node -> node.getElevation() == 'a')
                .map(startNode -> routeFinder.findRoute(startNode, targetNode))
                .min(Comparator.comparingInt(List::size))
                .get();
        int resultPart2 = route2.size() - 1;
        System.out.println("Result Part 2 (488): " + resultPart2);
    }


    private static class Node implements GraphNode {
        private final char elevation;
        int x, y;

        public Node(char elevation, int x, int y) {
            this.elevation = elevation;
            this.x = x;
            this.y = y;
        }

        public char getElevation() {
            return elevation;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Node getUp() {
            return new Node(elevation, x, y-1);
        }

        public Node getDown() {
            return new Node(elevation, x, y+1);
        }

        public Node getRight() {
            return new Node(elevation, x+1, y);
        }

        public Node getLeft() {
            return new Node(elevation, x-1, y);
        }

        public List<Node> getNeigbourhood() {
            return Arrays.asList(
                    getUp(), getDown(), getRight(), getLeft()
            );
        }

        @Override
        public String getId() {
            return "["+x+","+y+"]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Node position = (Node) o;

            return new EqualsBuilder().append(x, position.x).append(y, position.y).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(x).append(y).toHashCode();
        }

        @Override
        public String toString() {
            return getId()+" "+elevation;
        }
    }



    private static class Graph<T extends GraphNode> {
        private final Set<T> nodes;

        private final Map<String, Set<String>> connections;

        public Graph(Set<T> nodes, Map<String, Set<String>> connections) {
            this.nodes = nodes;
            this.connections = connections;
        }

        public T getNode(String id) {
            return nodes.stream()
                    .filter(node -> node.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
        }

        public Set<T> getConnections(T node) {
            return connections.get(node.getId()).stream()
                    .map(this::getNode)
                    .collect(Collectors.toSet());
        }
    }

    public interface GraphNode {
        String getId();
    }


    private static class RouteFinder<T extends GraphNode> {
        private final Graph<T> graph;
        private final Scorer<T> nextNodeScorer;
        private final Scorer<T> targetScorer;

        public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
            this.graph = graph;
            this.nextNodeScorer = nextNodeScorer;
            this.targetScorer = targetScorer;
        }

        public List<T> findRoute(T from, T to) {
            Map<T, RouteNode<T>> allNodes = new HashMap<>();
            Queue<RouteNode<T>> openSet = new PriorityQueue<>();

            RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
            allNodes.put(from, start);
            openSet.add(start);

            while (!openSet.isEmpty()) {
                RouteNode<T> next = openSet.poll();
                if (next.getCurrent().equals(to)) {

                    List<T> route = new ArrayList<>();
                    RouteNode<T> current = next;
                    do {
                        route.add(0, current.getCurrent());
                        current = allNodes.get(current.getPrevious());
                    } while (current != null);

                    return route;
                }

                graph.getConnections(next.getCurrent()).forEach(connection -> {
                    double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
                    RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                    allNodes.put(connection, nextNode);

                    if (nextNode.getRouteScore() > newScore) {
                        nextNode.setPrevious(next.getCurrent());
                        nextNode.setRouteScore(newScore);
                        nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                        openSet.add(nextNode);
                    }
                });
            }

            throw new IllegalStateException("No route found");
        }

    }

    private  static class RouteNode<T extends GraphNode> implements Comparable<RouteNode<T>> {
        private final T current;
        private T previous;
        private double routeScore;
        private double estimatedScore;

        RouteNode(T current) {
            this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        RouteNode(T current, T previous, double routeScore, double estimatedScore) {
            this.current = current;
            this.previous = previous;
            this.routeScore = routeScore;
            this.estimatedScore = estimatedScore;
        }

        T getCurrent() {
            return current;
        }

        T getPrevious() {
            return previous;
        }

        double getRouteScore() {
            return routeScore;
        }

        double getEstimatedScore() {
            return estimatedScore;
        }

        void setPrevious(T previous) {
            this.previous = previous;
        }

        void setRouteScore(double routeScore) {
            this.routeScore = routeScore;
        }

        void setEstimatedScore(double estimatedScore) {
            this.estimatedScore = estimatedScore;
        }

        @Override
        public int compareTo(RouteNode other) {
            if (this.estimatedScore > other.estimatedScore) {
                return 1;
            } else if (this.estimatedScore < other.estimatedScore) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", RouteNode.class.getSimpleName() + "[", "]").add("current=" + current)
                    .add("previous=" + previous).add("routeScore=" + routeScore).add("estimatedScore=" + estimatedScore)
                    .toString();
        }
    }

    public interface Scorer<T extends GraphNode> {
        double computeCost(T from, T to);
    }
}
