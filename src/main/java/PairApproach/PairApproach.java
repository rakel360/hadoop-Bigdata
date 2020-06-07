package PairApproach;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;


public class PairApproach extends Configured implements Tool {
    private final String jobName;
    public PairApproach() {
        String className = this.getClass().getSimpleName();
        this.jobName = className.toLowerCase();
    }


    public int run(String[] args) throws Exception {
        Job job = new Job(getConf());
        job.setJarByClass(PairApproach.class);
        job.setJobName(jobName);

        job.setMapperClass(MapClass.class);
        job.setReducerClass(ReducerClass.class);

        job.setMapOutputKeyClass(CustomPairWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(CustomPairWritable.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0] + "/" + jobName));
        FileOutputFormat.setOutputPath(job, new Path(args[1] + "/" + jobName));


        return job.waitForCompletion(true) ? 0 : 1;
    }

}
