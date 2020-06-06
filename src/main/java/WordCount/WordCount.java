package WordCount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

public class WordCount extends Configured implements Tool {

    private final String jobName;
    public WordCount() {
        String className = this.getClass().getSimpleName();
        this.jobName = className.toLowerCase();
    }

public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
    //private static final Pattern WORD_BOUNDARY = Pattern.compile("\\s*\\b\\s*");
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().toLowerCase();
        line = line.replace("\"", "").replace("'", "");
        for (String s : /*WORD_BOUNDARY.split(line)*/line.split("\\s*\\b\\s*")) {
            if (s.matches("[A-Za-z]+[\\,]?")) {
                s = s.replace("\"", "").replace("'", "");
                Text word = new Text(s);
                context.write(word, one);
            }

        }

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
        Job job = new Job(conf, "WordCount");
        job.setJarByClass(WordCount.class);

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