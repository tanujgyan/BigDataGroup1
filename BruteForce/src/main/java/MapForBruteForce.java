import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MapForBruteForce extends Mapper<Object, Text, Text, Text>{
	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException
	{
		String line = value.toString();
		String[] words=line.split(",");
		StringBuilder val = new StringBuilder();
		Driver.checkVal=line;
		Configuration conf = context.getConfiguration();
		String param = conf.get("sendValFromDriver");
		String[] paramArray=param.split("-");
		if(words.length==6)
		{
			key =words[0];
			for(int i=0;i<paramArray.length;i++)
			{
				//index starts from 1 and not zero
				val = val.append(words[Integer.parseInt(paramArray[i])+1]);
				//don't append comma for last element
				if(i!=paramArray.length-1)
				{
					val=val.append(", ");
				}
				Driver.checkVal="Hello I went inside Mapper 1";
			}
			Driver.checkVal="Hello I went inside Mapper";
			String str=val.toString();

			context.write(new Text((String) key), new Text(str));
		}
	}
}
