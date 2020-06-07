package PairApproach;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class ReducerClass extends Reducer<CustomPairWritable, IntWritable, CustomPairWritable, Text>{
    private int sum;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        this.sum = 0;
    }

    public void reduce(CustomPairWritable pair, Iterable<IntWritable> values, Context context)throws IOException, InterruptedException {
        int s = 0;
        for (IntWritable val : values) {
            s += val.get();
        }
        if (pair.getValue().toString().equals("*")) sum = s;
        else {
//	            Double relativeFreq = s / sum;
            context.write(pair, new Text(s + "/" + sum));

        }

    }

}

