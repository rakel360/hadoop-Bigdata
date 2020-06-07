import Average.Average;
import HybridApproach.HybridApproach;
import InMapperAverage.InMapperAverage;
import InMapperWordCount.InMapperWordCount;
import PairApproach.PairApproach;
import StripeApproach.StripeApproach;
import WordCount.WordCount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new Average(), args);
        ToolRunner.run(new Configuration(), new InMapperAverage(), args);
        ToolRunner.run(new Configuration(), new InMapperWordCount(), args);
        ToolRunner.run(new Configuration(), new WordCount(), args);
        ToolRunner.run(new Configuration(), new HybridApproach(), args);
        ToolRunner.run(new Configuration(), new PairApproach(), args);
        ToolRunner.run(new Configuration(), new StripeApproach(), args);
    }
}
