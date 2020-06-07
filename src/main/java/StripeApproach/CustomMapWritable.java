package StripeApproach;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

import java.util.Set;
import java.util.Map.Entry;


public class CustomMapWritable extends MapWritable {

    @Override
    public String toString() {
        String result = "[";
        boolean first = true;
        for(Entry<Writable, Writable> entry : entrySet()) {
            if(first) {
                result += "(" + entry.getKey() + "," + entry.getValue() + ")";
                first = false;
            }else {
                result += ", (" + entry.getKey() + "," + entry.getValue() + ")";
            }
        }
        result += "]";
        return result;
    }
}

