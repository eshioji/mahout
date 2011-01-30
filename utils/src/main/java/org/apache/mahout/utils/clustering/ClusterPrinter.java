package org.apache.mahout.utils.clustering;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.WeightedVectorWritable;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Prints a list of clusters with their points and top terms.
 *
 * @author Frank Scholten
 */
public class ClusterPrinter {

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage ClusterPrinter <clusterPointsFile>");
    }

    Path clusteredPointsPath = new Path(args[0]);

    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);

    SequenceFile.Reader reader = new SequenceFile.Reader(fs, clusteredPointsPath, conf);

    IntWritable key = new IntWritable();
    WeightedVectorWritable value = new WeightedVectorWritable();
    Multimap<Integer, String> clusterMap = HashMultimap.create();

    while (reader.next(key, value)) {
      clusterMap.put(key.get(), ((NamedVector) value.getVector()).getName());
    }

    reader.close();

    for (Integer clusterId : clusterMap.keySet()) {
      Collection<String> points = clusterMap.get(clusterId);
      System.out.println("Cluster\t" + clusterId + "\t(" + points.size() + ")");
      for (String point : points) {
        System.out.println("\t" + point);
      }
    }
  }
}