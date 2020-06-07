package HybridApproach;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReducerClass extends Reducer<CustomPairWritable, IntWritable, Text, CustomMapWritable> {

    private String prevTerm;
    private HashMap<String, Integer> hashMap;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        prevTerm = null;
        hashMap = new HashMap<>();
    }

    @Override
    protected void reduce(CustomPairWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        if (!key.getKey().toString().equals(prevTerm) && prevTerm != null) {
            emit(context);
            hashMap.clear();
        }
        hashMap.put(key.getValue().toString(), sum(values));
        prevTerm = key.getKey().toString();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        emit(context);
    }

    private int sum(Iterable<IntWritable> values) {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        return sum;
    }

    private int total() {
        int sum = 0;
        for (Map.Entry entry : hashMap.entrySet()) {
            sum += (Integer) entry.getValue();
        }
        return sum;
    }

    private void emit(Context context) throws IOException, InterruptedException {
        int total = total();
        CustomMapWritable result = new CustomMapWritable();
        for (Map.Entry entry : hashMap.entrySet()) {
            result.put(new Text((String) entry.getKey()), new Text(entry.getValue() + "/" + total));
        }
        context.write(new Text(prevTerm), result);
    }
}

