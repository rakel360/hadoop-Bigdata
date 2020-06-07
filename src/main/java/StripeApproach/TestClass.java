package StripeApproach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;

public class TestClass {

    public static void main(String[] args) {


        List<String> words = Arrays.asList("B11", "C31", "A10", "D76", "A12", "B12", "C31", "D76", "C31", "A10", "B12", "D76", "C31", "B11");
        int size = words.size();
        for (int i = 0; i < size; i++) {
            Text u = new Text(words.get(i));
            for (String v : windowOfWord(words.get(i), words.subList(i, size))) {
                CustomPairWritable p1 = new CustomPairWritable(u, new Text(v));
                System.out.println("value of u " + i + "value of v " + v + " Pair " + p1 + " " + 1);


                CustomPairWritable p2 = new CustomPairWritable(u, new Text("*"));
                System.out.println("value of u " + i + "value of v " + v + " Star Pair " + p2  + " " + 1);
            }
        }
    }

    static List<String> windowOfWord(String u, List<String> words) {
        List<String> window = new ArrayList<String>();
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).equals(u)) return window;
            else window.add(words.get(i));
        }
        return window;
    }

}
