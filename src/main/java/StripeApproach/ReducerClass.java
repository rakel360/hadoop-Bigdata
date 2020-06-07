package StripeApproach;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReducerClass extends Reducer<Text, CustomMapWritable, Text, CustomMapWritable> {

    private Logger logger = Logger.getLogger(ReducerClass.class);
    private int sum;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        this.sum = 0;
    }

    public void reduce(Text key, Iterable<CustomMapWritable> values, Context context)
            throws IOException, InterruptedException {
        HashMap<String, Integer> common = new HashMap<String, Integer>();
        for (CustomMapWritable val : values) {
            for (Map.Entry e: val.entrySet()) {
                String v = e.getKey().toString();
                Integer vVal = Integer.parseInt(e.getValue().toString());
                if (common.containsKey(v)) common.put(v, common.get(v) + vVal);
                else common.put(v, vVal);
            }
        }
        int sum = 0;
        for (Map.Entry e: common.entrySet()) {
            sum += Integer.parseInt(e.getValue().toString());
        }
        CustomMapWritable stripe = new CustomMapWritable();
        for (Map.Entry e: common.entrySet()) {
            String v = (String) e.getKey();
            Integer vVal = (Integer) e.getValue();

            stripe.put(new Text(v), new Text(vVal + "/" + sum));
        }
        logger.info("(" + key + ", " + stripe + ")");
        context.write(key, stripe);
    }
}

