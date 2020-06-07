package PairApproach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapClass extends Mapper<LongWritable, Text, CustomPairWritable, IntWritable>{


    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        IntWritable one = new IntWritable(1);

        List<String> words = Arrays.asList(value.toString().split("\\s+"));
        int size = words.size();
        for (int i = 0; i < size; i++) {
            Text u = new Text(words.get(i));
            for (String v : windowOfWord(words.get(i), words.subList(i, size))) {
                CustomPairWritable p1 = new CustomPairWritable(u, new Text(v));
                context.write(p1, one);

                CustomPairWritable p2 = new CustomPairWritable(u, new Text("*"));
                context.write(p2, one);
            }
        }
    }

    List<String> windowOfWord(String u, List<String> words) {
        List<String> window = new ArrayList<String>();
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).equals(u)) return window;
            else window.add(words.get(i));
        }
        return window;
    }

}

