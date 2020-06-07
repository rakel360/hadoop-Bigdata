package InMapperAverage;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Average.Average;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

public class InMapperAverage extends Configured implements Tool {

    private final String jobName;
    public InMapperAverage() {
        String className = this.getClass().getSimpleName();
        this.jobName = className.toLowerCase();
    }

    public static class Map extends Mapper<LongWritable, Text, Text, Pair> {

        private HashMap<String, Pair> associativeArray;
        final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
                + " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, Pair>.Context context)
                throws IOException, InterruptedException {
            associativeArray = new HashMap<String, Pair>();

        }

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            IntWritable count = new IntWritable(0);
            IntWritable sum = new IntWritable(0);
            String line = value.toString().toLowerCase();
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                try {
                    String ip = matcher.group(1);
                    String Quantity = matcher.group(9);
                    int quantity = Integer.parseInt(Quantity);
                    if ((ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) && (Quantity != "-" && quantity != 0)) {

                        if (associativeArray.containsKey(ip)) {
                            IntWritable currSum = associativeArray.get(ip).getKey();
                            IntWritable currCount = associativeArray.get(ip).getValue();
                            sum.set(currSum.get() + quantity);
                            count.set(currCount.get() + 1);

                            Pair pair = new Pair(sum, count);
                            associativeArray.put(ip, pair);
                        } else {
                            Pair pair = new Pair(new IntWritable(quantity), new IntWritable(1));
                            associativeArray.put(ip, pair);
                        }

                    }
                } catch (Exception ex) {
                }

            }

        }

        @Override
        protected void cleanup(Mapper<LongWritable, Text, Text, Pair>.Context context)
                throws IOException, InterruptedException {
            for (Entry<String, Pair> e : associativeArray.entrySet()) {
                Text ip = new Text(e.getKey());
                Pair value = new Pair(e.getValue().getKey(), e.getValue().getValue());
                context.write(ip, value);

            }
            super.cleanup(context);
        }
    }

    public static class Reduce extends Reducer<Text, Pair, Text, DoubleWritable> {

        public void reduce(Text ipAddress, Iterable<Pair> quantities, Context context)
                throws IOException, InterruptedException {

            int sum = 0;
            int count = 0;
            for (Pair val : quantities) {
                sum = sum + val.getKey().get();
                count = count + val.getValue().get();
            }
            context.write(ipAddress, new DoubleWritable(sum / count));
        }
    }

    public int run(String[] args) throws Exception {

        Job job = new Job(getConf());
        job.setJarByClass(InMapperAverage.class);
        job.setJobName(jobName);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Pair.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0] + "/" + jobName));
        FileOutputFormat.setOutputPath(job, new Path(args[1] + "/" + jobName));


        return job.waitForCompletion(true) ? 0 : 1;

    }


}
