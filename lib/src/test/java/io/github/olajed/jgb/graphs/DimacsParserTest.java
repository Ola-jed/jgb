package io.github.olajed.jgb.graphs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DimacsParserTest {
    @TempDir
    Path tempDir;

    private File createTempDimacsFile(String content) throws IOException {
        File tempFile = tempDir.resolve("graph.dimacs").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        return tempFile;
    }

    @Test
    void testParseValidGraph() throws IOException {
        String content = """
            c This is a comment
            p edge 4 3
            e 1 2
            e 2 3
            e 4 1
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());
        Graph graph = parser.parse();

        assertEquals(4, graph.getVertexCount());

        List<Set<Integer>> edges = graph.getEdges();
        assertEquals(Set.of(2), edges.get(0));
        assertEquals(Set.of(3), edges.get(1));
        assertNull(edges.get(2));  // node 3 has no outgoing edges
        assertEquals(Set.of(1), edges.get(3));
    }

    @Test
    void testParseWithBlankAndCommentLinesOnly() throws IOException {
        String content = """
            c Only comments and blanks
            
             
            c Another comment
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());
        Graph graph = parser.parse();

        assertEquals(0, graph.getVertexCount());
        assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    void testGraphWithNoEdges() throws IOException {
        String content = """
            p edge 5 0
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());
        Graph graph = parser.parse();

        assertEquals(5, graph.getVertexCount());
        List<Set<Integer>> edges = graph.getEdges();
        assertEquals(5, edges.size());
        for (Set<Integer> set : edges) {
            assertNull(set);  // No outgoing edges
        }
    }

    @Test
    void testMultipleEdgesFromSameNode() throws IOException {
        String content = """
            p edge 3 3
            e 1 2
            e 1 3
            e 1 2
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());
        Graph graph = parser.parse();

        assertEquals(3, graph.getVertexCount());
        List<Set<Integer>> edges = graph.getEdges();
        assertEquals(Set.of(2, 3), edges.get(0));
        assertNull(edges.get(1));
        assertNull(edges.get(2));
    }

    @Test
    void testMissingProblemLine() throws IOException {
        String content = """
            e 1 2
            e 2 3
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());
        // Should result in vertexCount 0, and an IndexOutOfBoundsException may happen
        // But the code doesn't throw — instead, it may crash on edges.get(node1 - 1)
        // So we can expect an exception due to accessing before list init
        assertThrows(IndexOutOfBoundsException.class, parser::parse);
    }

    @Test
    void testMalformedEdgeLine() throws IOException {
        String content = """
            p edge 3 2
            e 1
            e 2 3
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());

        assertThrows(ArrayIndexOutOfBoundsException.class, parser::parse);
    }

    @Test
    void testInvalidVertexNumber() throws IOException {
        String content = """
            p edge 2 1
            e 3 1
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());

        assertThrows(IndexOutOfBoundsException.class, parser::parse);
    }

    @Test
    void testInvalidFilePath() {
        DimacsParser parser = new DimacsParser("nonexistent_file.txt");

        assertThrows(IOException.class, parser::parse);
    }

    @Test
    void testEmptyFile() throws IOException {
        File file = createTempDimacsFile("");
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());

        Graph graph = parser.parse();

        assertEquals(0, graph.getVertexCount());
        assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    void testMultipleProblemLinesOnlyLastUsed() throws IOException {
        String content = """
            p edge 2 1
            p edge 4 2
            e 1 2
            e 3 4
            """;
        File file = createTempDimacsFile(content);
        DimacsParser parser = new DimacsParser(file.getAbsolutePath());

        Graph graph = parser.parse();

        assertEquals(4, graph.getVertexCount());
        List<Set<Integer>> edges = graph.getEdges();
        assertEquals(Set.of(2), edges.get(0));
        assertNull(edges.get(1));
        assertEquals(Set.of(4), edges.get(2));
        assertNull(edges.get(3));
    }
}



