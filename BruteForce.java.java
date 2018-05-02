import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;


public class Driver {
	static	ArrayList<ArrayList<Integer>> combosingles= new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<ArrayList<Integer>>> mainlist=new ArrayList<ArrayList<ArrayList<Integer>>>();
	public static ArrayList<Integer> temp=new ArrayList<Integer>();
	static int count=0;
	static Path out;
	static 	Job bruteForceJob;
	static String checkVal="Hello";
	static String checkVal1="Hello";
	static String checkVal2="Hello";
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException 
	{
		//create mainlist set
		CreateAllSets();
		System.out.println("mainlist-->"+mainlist);
		out= new Path(args[1]);
		for(int j=0;j<mainlist.size();j++)
		{

			for(int l=0;l<mainlist.get(j).size();l++)
			{
				temp.clear();
				//temp will contain the columns that needs to be kept
				for(int i=0;i<5;i++)
				{
					if(mainlist.get(j).get(l).contains(i))
					{
						temp.add(i);
					}
				}
				System.out.println("temp-->"+temp);
				StartBruteForce(args);
				count++;
				System.out.println(count);
			}
		}
	
	System.exit(0);

}
public static void StartBruteForce(String[] args) throws IOException, ClassNotFoundException, InterruptedException 
{
	Configuration c=new Configuration();
	StringBuilder sendVals=new StringBuilder();
    for(int i=0;i<temp.size();i++)
    {
    	sendVals.append(Integer.toString(temp.get(i)));
    	if(i!=temp.size()-1)
    	{
    	sendVals.append("-");
    	}
    }
    System.out.println(sendVals.toString());
    c.set("sendValFromDriver", sendVals.toString());
	bruteForceJob = Job.getInstance(c, "Brute Force Algorithm");
	
	//set jar, mapper and reducer
	bruteForceJob.setJarByClass(Driver.class);
	bruteForceJob.setMapperClass(MapForBruteForce.class);
	bruteForceJob.setReducerClass(ReduceForBruteForce.class);
	//set input and ouptut class
	bruteForceJob.setOutputKeyClass(Text.class);
	bruteForceJob.setOutputValueClass(Text.class);
	//set file input and output paths
	FileInputFormat.addInputPath(bruteForceJob, new Path(args[0]));
	FileOutputFormat.setOutputPath(bruteForceJob, new Path(out,Integer.toString(count)));
	bruteForceJob.waitForCompletion(true);
	System.out.println(checkVal);
	System.out.println(checkVal1);
	System.out.println(checkVal2);
	
}
public static void CreateAllSets()
{
	ArrayList<ArrayList<Integer>> comb2= new ArrayList<ArrayList<Integer>>();
	//to store elements from 3 onwards
	//this will create single element arraylist
	//change the length accordingly
	for(int i=0;i<760;i++)
	{
		ArrayList<Integer> temp=new ArrayList<Integer>();
		temp.add(i);
		combosingles.add(temp);
	}
	System.out.println(combosingles);
	//2 elememts list
	for(int i=0;i<5;i++)
	{

		for(int j=i+1;j<760;j++)
		{
			ArrayList<Integer> temp=new ArrayList<Integer>();
			for(Integer k:combosingles.get(i))
			{
				temp.add(k);
			}
			for(Integer k:combosingles.get(j))
			{
				temp.add(k);
			}
			comb2.add(temp);
		}

	}
	//System.out.println(comb2);

	mainlist.add(comb2);
	for(int index=0;index<combosingles.size()-2;index++)
	{
		mainlist.add(CreateSets(mainlist.get(index),index+3));
	}
	mainlist.add(0, combosingles);

}
public static ArrayList<ArrayList<Integer>> CreateSets(ArrayList<ArrayList<Integer>> comb, int numberOfElements)
{
	ArrayList<ArrayList<Integer>> tempcomb=new ArrayList<ArrayList<Integer>>();
	//4 elements list
	for(int i=0;i<comb.size();i++)
	{
		ArrayList<Integer> temp=new ArrayList<Integer>();
		for(Integer k:comb.get(i))
		{
			temp.add(k);
		}
		for(int j=0;j<combosingles.size();j++)
		{

			for(Integer k:combosingles.get(j))
			{
				if(!temp.contains(k))
				{
					temp.add(k);
				}
			}
			//remove duplicates from temp
			Set<Integer> hs1 = new HashSet<>();
			hs1.addAll(temp);
			temp.clear();
			temp.addAll(hs1);
			if(temp.size()==numberOfElements)
			{
				tempcomb.add(temp);
				temp=new ArrayList<Integer>();
				for(Integer k:comb.get(i))
				{
					temp.add(k);
				}
			}
		}

	}
	//clean 3 element set
	Set<ArrayList<Integer>> hs = new HashSet<ArrayList<Integer>>();
	hs.addAll(tempcomb);
	tempcomb.clear();
	tempcomb.addAll(hs);
	//System.out.println(tempcomb);
	return tempcomb;
}

public static class MapForBruteForce extends Mapper<Object, Text, Text, Text>{
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

public static class ReduceForBruteForce extends Reducer<Text, Text, Text, Text>{
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

}
