package StripeApproach;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class CustomPairWritable implements WritableComparable<CustomPairWritable> {
    private Text key;
    private Text value;

    public CustomPairWritable() {
        this.key = new Text();
        this.value = new Text();
    }

    public CustomPairWritable(Text key, Text value) {
        this.key = key;
        this.value = value;
    }

    public Text getKey() {
        return key;
    }

    public void setKey(Text key) {
        this.key = key;
    }

    public Text getValue() {
        return value;
    }

    public void setValue(Text value) {
        this.value = value;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        key.readFields(in);
        value.readFields(in);

    }

    @Override
    public void write(DataOutput out) throws IOException {
        key.write(out);
        value.write(out);
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + key.toString() + ", " + value.toString() + ")";
    }

    @Override
    public int compareTo(CustomPairWritable o) {
        int k = this.key.compareTo(o.key);
        if (k != 0) return k;
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        CustomPairWritable object = (CustomPairWritable) obj;
        if (object.getKey().equals(this.getKey())) {
            return object.getValue().equals(this.getValue());
        }
        return false;
    }

}
