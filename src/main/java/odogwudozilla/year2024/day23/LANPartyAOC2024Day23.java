package odogwudozilla.year2024.day23;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * LAN Party (Advent of Code 2024 Day 23)
 *
 * As The Historians wander around a secure area at Easter Bunny HQ, you come across posters for a LAN party scheduled for today! Maybe you can find it; you connect to a nearby datalink port and download a map of the local network (your puzzle input).
 *
 * The network map provides a list of every connection between two computers. Each line represents a connection between two computers (undirected). The task is to find all sets of three computers where each is connected to the other two (triangles), and count how many of these sets contain at least one computer whose name starts with 't'.
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/23
 */
public class LANPartyAOC2024Day23 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2024/day23/day23_puzzle_data.txt"));
        Map<String, Set<String>> graph = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split("-");
            String a = parts[0];
            String b = parts[1];
            graph.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            graph.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }
        int trianglesWithT = solvePartOne(graph);
        System.out.println("Part 1: " + trianglesWithT);
        String lanPartyPassword = solvePartTwo(graph);
        System.out.println("Part 2: " + lanPartyPassword);
    }

    /**
     * Finds all triangles (sets of three mutually connected computers) and counts those with at least one name starting with 't'.
     * @param graph The undirected network graph
     * @return The number of triangles containing at least one computer whose name starts with 't'
     */
    public static int solvePartOne(Map<String, Set<String>> graph) {
        Set<Set<String>> triangles = new HashSet<>();
        List<String> nodes = new ArrayList<>(graph.keySet());
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                for (int k = j + 1; k < nodes.size(); k++) {
                    String a = nodes.get(i);
                    String b = nodes.get(j);
                    String c = nodes.get(k);
                    if (graph.get(a).contains(b) && graph.get(a).contains(c) && graph.get(b).contains(c)) {
                        Set<String> triangle = new TreeSet<>(Arrays.asList(a, b, c));
                        triangles.add(triangle);
                    }
                }
            }
        }
        int count = 0;
        for (Set<String> triangle : triangles) {
            if (triangle.stream().anyMatch(name -> name.startsWith("t"))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Finds the largest clique (set of mutually connected computers) and returns the LAN party password.
     * @param graph The undirected network graph
     * @return The password: sorted, comma-joined names of the largest clique
     */
    public static String solvePartTwo(Map<String, Set<String>> graph) {
        List<String> nodes = new ArrayList<>(graph.keySet());
        List<String> maxClique = new ArrayList<>();
        bronKerbosch(new ArrayList<>(), nodes, new ArrayList<>(), graph, maxClique);
        Collections.sort(maxClique);
        return String.join(",", maxClique);
    }

    /**
     * Bron–Kerbosch algorithm to find the largest clique in an undirected graph.
     * @param r Current clique
     * @param p Candidates
     * @param x Already processed
     * @param graph The graph
     * @param maxClique Reference to the largest clique found
     */
    private static void bronKerbosch(List<String> r, List<String> p, List<String> x, Map<String, Set<String>> graph, List<String> maxClique) {
        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > maxClique.size()) {
                maxClique.clear();
                maxClique.addAll(r);
            }
            return;
        }
        List<String> pCopy = new ArrayList<>(p);
        for (String v : pCopy) {
            List<String> rNew = new ArrayList<>(r);
            rNew.add(v);
            List<String> pNew = new ArrayList<>();
            for (String u : p) {
                if (graph.get(v).contains(u)) {
                    pNew.add(u);
                }
            }
            List<String> xNew = new ArrayList<>();
            for (String u : x) {
                if (graph.get(v).contains(u)) {
                    xNew.add(u);
                }
            }
            bronKerbosch(rNew, pNew, xNew, graph, maxClique);
            p.remove(v);
            x.add(v);
        }
    }
}
