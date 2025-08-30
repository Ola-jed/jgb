package io.github.olajed.jgb.graphs;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    @Test
    void testGraphConstruction() {
        List<Set<Integer>> adjacency = new ArrayList<>();
        adjacency.add(Set.of(2));
        adjacency.add(Set.of(1, 3));
        adjacency.add(Set.of(1));
        adjacency.add(Set.of());
        Graph graph = new Graph(4, adjacency);
        assertEquals(4, graph.getVertexCount());
        assertEquals(adjacency, graph.getEdges());
    }

    @Test
    void testKColoringIdealGeneratorsForTriangle() {
        List<Set<Integer>> adjacency = new ArrayList<>();
        adjacency.add(Set.of(2, 3));
        adjacency.add(Set.of(1, 3));
        adjacency.add(Set.of(1, 2));
        Graph triangle = new Graph(3, adjacency);
        var polynomials = triangle.kColoringIdealGenerators(3);
        assertNotNull(polynomials);
        assertFalse(polynomials.isEmpty());
        int vertexCount = 3;
        int edgeCount = 3;
        int expected = vertexCount + edgeCount;
        assertEquals(expected, polynomials.size());
    }

    @Test
    void testKColoringIdealGeneratorsForEmptyGraph() {
        Graph empty = new Graph(3, List.of(Set.of(), Set.of(), Set.of()));
        var polynomials = empty.kColoringIdealGenerators(2);
        assertNotNull(polynomials);
        assertEquals(3, polynomials.size());
    }

    @Test
    void testKColoringIdealGeneratorsForLineGraph() {
        List<Set<Integer>> adjacency = new ArrayList<>();
        adjacency.add(Set.of(2));
        adjacency.add(Set.of(1, 3));
        adjacency.add(Set.of(2));
        Graph line = new Graph(3, adjacency);
        var polynomials = line.kColoringIdealGenerators(2);
        int expected = 3 + 2;
        assertEquals(expected, polynomials.size());
    }
}
