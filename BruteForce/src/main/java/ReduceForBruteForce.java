import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class ReduceForBruteForce extends Reducer<Text, Text, Text, Text>{
	@Override
	public void reduce(Text word, Iterable<Text> values, Context con) throws IOException, InterruptedException
	{
		for(Text val:values)
		{
			con.write(new Text(""), val);
			Driver.checkVal2=val.toString();
		}
	}
}
