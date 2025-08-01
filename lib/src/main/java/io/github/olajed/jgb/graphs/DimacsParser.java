package io.github.olajed.jgb.graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Provides functionality to parse graph data from
 * files formatted in the DIMACS standard.
 *
 * <p>The expected DIMACS format for graphs includes:
 * <ul>
 *   <li>Comment lines starting with 'c'</li>
 *   <li>A problem line starting with 'p', typically formatted as {@code p edge <num_vertices> <num_edges>}</li>
 *   <li>Edge lines starting with 'e', typically formatted as {@code e <from_node> <to_node>}</li>
 * </ul>
 *
 * <p>Example :
 * <pre>
 * c This is a comment
 * p edge 4 3
 * e 1 2
 * e 2 3
 * e 4 1
 * </pre>
 */
public class DimacsParser {
    private final String fileName;

    /**
     * Constructs a {@code DimacsParser} for reading a DIMACS-formatted graph file.
     *
     * @param filename the path to the DIMACS file to parse
     */
    public DimacsParser(String filename) {
        this.fileName = filename;
    }

    /**
     * Parses the DIMACS-formatted file and constructs a {@link Graph} representation.
     *
     * @return the parsed {@link Graph} object
     * @throws IOException if an I/O error occurs while reading the file
     */
    public Graph parse() throws IOException {
        var vertexCount = 0;
        List<Set<Integer>> edges = List.of();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.isBlank() || line.charAt(0) == 'c') {
                    // Nothing, we ignore the line, it is either empty or a comment
                } else if (line.charAt(0) == 'p') {
                    var components = line.split("\\s+");
                    if (components[1].equals("edge")) {
                        vertexCount = Integer.parseInt(components[2]);
                        edges = new ArrayList<>(Collections.nCopies(vertexCount, null));
                    }
                } else if (line.charAt(0) == 'e') {
                    var components = line.split("\\s+");
                    var node1 = Integer.parseInt(components[1]);
                    var node2 = Integer.parseInt(components[2]);
                    if (edges.get(node1 - 1) == null) {
                        var set = new HashSet<Integer>();
                        set.add(node2);
                        edges.set(node1 - 1, set);
                    } else {
                        edges.get(node1 - 1).add(node2);
                    }
                }
            }
        }

        return new Graph(vertexCount, edges);
    }
}
