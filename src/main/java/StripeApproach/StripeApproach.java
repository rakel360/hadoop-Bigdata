package StripeApproach;

import Average.Average;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;


public class StripeApproach extends Configured implements Tool {
    private final String jobName;
    public StripeApproach() {
        String className = this.getClass().getSimpleName();
        this.jobName = className.toLowerCase();
    }


    public int run(String[] args) throws Exception {
        Job job = new Job(getConf());
        job.setJarByClass(StripeApproach.class);
        job.setJobName(jobName);

        job.setMapperClass(MapClass.class);
        job.setReducerClass(ReducerClass.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CustomMapWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CustomMapWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0] + "/" + jobName));
        FileOutputFormat.setOutputPath(job, new Path(args[1] + "/" + jobName));


        return job.waitForCompletion(true) ? 0 : 1;
    }

}

