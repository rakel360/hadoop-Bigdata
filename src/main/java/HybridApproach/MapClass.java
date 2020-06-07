package HybridApproach;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapClass extends Mapper<LongWritable,Text,CustomPairWritable,IntWritable> {

    private HashMap<CustomPairWritable, Integer> hashMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        hashMap = new HashMap<>();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        List<String> record = Arrays.asList(value.toString().split("\\s+"));
        int i = 1;
        for(String term : record) {
            for(String v : record.subList(i,record.size())) {
                if(term.equals(v)) {
                    break;
                }
                CustomPairWritable pair = new CustomPairWritable(new Text(term), new Text(v));
                hashMap.put(pair, hashMap.containsKey(pair) ? hashMap.get(pair) + 1 : 1);
            }
            i++;
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for(Map.Entry entry : hashMap.entrySet()) {
            context.write((CustomPairWritable) entry.getKey(), new IntWritable((Integer) entry.getValue()));
        }
    }
}