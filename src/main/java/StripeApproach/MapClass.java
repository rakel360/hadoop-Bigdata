package StripeApproach;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapClass extends Mapper<LongWritable, Text, Text, CustomMapWritable> {

    private Logger logger = Logger.getLogger(MapClass.class);

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        List<String> words = Arrays.asList(value.toString().split("\\s+"));

        int size = words.size();
        for (int i = 0; i < size; i++) {
            String u = words.get(i);
            CustomMapWritable stripe = new CustomMapWritable();
            for (String v : window(u, words.subList(i, size))) {
                Text kv = new Text(v);
                if (stripe.containsKey(kv)) {
                    Integer val = Integer.parseInt(stripe.remove(kv).toString()) + 1;
                    stripe.put(kv, new IntWritable(val));
                } else stripe.put(kv, new IntWritable(1));
            }
            Text k = new Text(u);
            context.write(k, stripe);
            logger.info("(" + k + ", " + stripe + ")");
        }
    }

    List<String> window(String u, List<String> words) {
        List<String> window = new ArrayList<String>();
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i).equals(u)) return window;
            else window.add(words.get(i));
        }
        return window;
    }
}

