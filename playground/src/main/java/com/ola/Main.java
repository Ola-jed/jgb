package com.ola;

import com.ola.graphs.GraphColoringProblem;

public class Main {
    public static void main(String[] args) {
        try {
            var g = new GraphColoringProblem("C:\\Users\\olabi\\Projects\\IdeaProjects\\jgb\\examples\\graphs\\simple");
            System.out.println(g.isKColorable(3));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}