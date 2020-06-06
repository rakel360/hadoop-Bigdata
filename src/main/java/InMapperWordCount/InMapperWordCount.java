package InMapperWordCount;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;


public class InMapperWordCount extends Configured implements Tool {

    private final String jobName;
    public InMapperWordCount() {
        String className = this.getClass().getSimpleName();
        this.jobName = className.toLowerCase();
    }

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private HashMap<String, Integer> associativeArray;

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, IntWritable>. Context context) throws IOException, InterruptedException {
            associativeArray = new HashMap<String, Integer>();
        }

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().toLowerCase();
            line = line.replace("\"", "").replace("'", "");
            for (String s : line.split("\\s+|-")) {
                if (s.matches("[A-Za-z]+[\\,]?")) {
                    s = s.replace("\"", "").replace("'", "");
                    int count = associativeArray.containsKey(s) ? associativeArray.get(s) + 1 : 1;
                    associativeArray.put(s, count);
                }

            }

        }

        @Override
        protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>. Context context) throws IOException, InterruptedException {
            for(Entry<String, Integer> e : associativeArray.entrySet()) {
                Text key = new Text(e.getKey());
                IntWritable value = new IntWritable(e.getValue());
                context.write(key, value);

            }
            super.cleanup(context);
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public int run(String[] args) throws Exception {
        Configuration conf = new Configuration();

        @SuppressWarnings("deprecation")
        Job job = new Job(conf, "word1.InMapperWordCount");
        job.setJarByClass(InMapperWordCount.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0] + "/" + jobName));
        FileOutputFormat.setOutputPath(job, new Path(args[1] + "/" + jobName));


        return job.waitForCompletion(true) ? 0 : 1;

    }

}

