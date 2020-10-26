import java.util.*;
import java.io.IOException; import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration; import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable; import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Log {

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>{
		String month_sub(String time){
			 Map<String, String> month_map = new HashMap<String, String>() {{
		        put("Jan", "01");
		        put("Feb","02");
				put("Mar","03");
				put("Apr","04");
				put("May","05");
				put("Jun","06");
				put("Jul","07");
				put("Aug","08");
				put("Sep","09");
				put("Oct","10");
				put("Nov","11");
				put("Dec","12");}};

			for (Iterator it = month_map.entrySet().iterator(); it.hasNext();) {
				Map.Entry mapEntry = (Map.Entry) it.next();

				if(time.contains((String)mapEntry.getKey())){
					time = time.replace((String)mapEntry.getKey(), (String)mapEntry.getValue());
					return time;
				}
			}
			return time;
		}

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			//parsing start
			String time = value.toString().split("\\[|\\]")[1];
			String[] tokens = time.split(":");
			time = tokens[0] + ":" +tokens[1];
			time = month_sub(time);
			tokens = time.split("\\/|\\:");
			time = String.format("%s-%s-%s T %s:00:00.000",tokens[2],tokens[0],tokens[1],tokens[3]);
			//parsing end
			word.set(time);
			context.write(word, one);
		}
	}

	public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum); context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "log count");
		job.setJarByClass(Log.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}