package org.apache.mahout.hbase;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.io.BatchUpdate;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Imports seinfeld episodes into HBase
 *
 * @author Frank Scholten
 */
public class HBaseSeinfeldEpisodeImporter extends Configured implements Tool {

  static final String EPISODES_TABLE        = "episodes";
  static String EPISODES_TITLE_COLUMN       = "cf:title";
  static String EPISODES_DESCRIPTION_COLUMN = "cf:description";

  static class HBaseSeinfeldEpisodeMapper<K, V> extends MapReduceBase implements Mapper<Text, Text, K, V> {

    static HTable table;

    @Override
    public void configure(JobConf jc) {
      try {
       table = new HTable(new HBaseConfiguration(), EPISODES_TABLE);
      } catch (IOException e) {
        throw new RuntimeException("Could not initialize HTable '" + EPISODES_TABLE + "'");
      }
    }

    @Override
    public void map(Text key, Text value, OutputCollector<K, V> kvOutputCollector, Reporter reporter) throws IOException {
      BatchUpdate update = new BatchUpdate();
      update.put(EPISODES_TITLE_COLUMN, key.getBytes());
      update.put(EPISODES_DESCRIPTION_COLUMN, value.getBytes());
      table.commit(update);
    }
  }

  @Override
  public int run(String[] args) throws Exception {
    if (args.length  != 1) {
      System.out.println("Usage: " + this.getClass().getSimpleName() + " <clusteredPointsFile>");
    }

    JobConf jc = new JobConf(getConf(), getClass());
    jc.setMapperClass(HBaseSeinfeldEpisodeMapper.class);
    jc.setNumReduceTasks(0);
    jc.setOutputFormat(NullOutputFormat.class);

    FileInputFormat.addInputPath(jc, new Path(args[0]));

    JobClient.runJob(jc);
    return 0;
  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new HBaseConfiguration(), new HBaseSeinfeldEpisodeImporter(), args);
    System.exit(exitCode);
  }
}