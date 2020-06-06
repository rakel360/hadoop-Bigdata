package InMapperAverage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class Pair implements Writable {
    @Override
    public String toString() {
        return "< " + key + ", " + value + " >";
    }

    private IntWritable key;
    private IntWritable value;

    public Pair() {
        key = new IntWritable();
        value = new IntWritable();
    }

    public Pair(IntWritable key, IntWritable value) {
        this.key = key;
        this.value = value;
    }

    public IntWritable getKey() {
        return this.key;
    }

    public IntWritable getValue() {
        return this.value;
    }

    public void setKey(IntWritable key) {
        this.key = key;
    }

    public void setValue(IntWritable value) {
        this.value = value;
    }

    @Override
    public void readFields(DataInput arg0) throws IOException {
        // TODO Auto-generated method stub
        key.readFields(arg0);
        value.readFields(arg0);
    }

    @Override
    public void write(DataOutput arg0) throws IOException {
        // TODO Auto-generated method stub
        key.write(arg0);
        value.write(arg0);
    }

}
