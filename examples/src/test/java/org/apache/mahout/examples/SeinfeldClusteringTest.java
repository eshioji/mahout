package org.apache.mahout.examples;

import org.apache.mahout.driver.MahoutDriver;

/**
 * @author Frank Scholten
 */
public class SeinfeldClusteringTest {

  public static void main(String[] args) throws Throwable {
    String params = "org.apache.mahout.utils.vectors.lucene.ClusterLabels " +
            "        --dir           src/main/resources/seinfeld-scripts-index  " +
            "        --pointsDir     out/seinfeld-scripts/clusters/clusteredPoints           " +
            "        --seqFileDir    out/seinfeld-scripts/seqfiles                           " +
            "        --idField       id                                         " +
            "        --field         content                                    " +
            "        --output        out/seinfeld-scripts/cluster-labels.txt                 ";
    MahoutDriver.main(params.split(""));
  }
}
