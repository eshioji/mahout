#!/bin/bash
#
# Prints clusters in the following format
#
# 1 'Latvian Orthodox' => 0.23, 'Festivus' => 0.21, 'Kramerica' => 0.19
# 'The Conversion'
# 'The Dinner Party'
# 'The Marine Biologist'
#
# 2 'Junior Mint' => 0.43, 'Calzone' => 0.34
# 'The Fusilli Jerry'
# 'The Yankees'
# 'The Calzone'

INPUT=src/main/resources/seinfeld-scripts
OUTPUT=out/seinfeld-scripts

mahout clusterdump -s $OUTPUT/clusters/clusters-1/ \
                   -p $OUTPUT/clusters/clusteredPoints \
                   -n 5 \
                   -dt sequencefile \
                   -d $OUTPUT/vectors/dictionary.file-0 | grep -vi hadoop > $OUTPUT/dump

wait

mahout clusterdump -s $OUTPUT/clusters/clusters-2/ \
                   -p $OUTPUT/clusters/clusteredPoints \
                   -n 5 \
                   -dt sequencefile \
                   -d $OUTPUT/vectors/dictionary.file-0 | grep -vi hadoop >> $OUTPUT/dump

wait

cat $OUTPUT/dump | grep -A5 'Top Terms' | grep -v '\-\-' > $OUTPUT/topterms.txt

cat $OUTPUT/dump | cut -d ' ' -f 1 | grep '^CL' | sed s/\{/\\t/ | sed s/CL-// | sort -g > $OUTPUT/clusters.txt

#mahout org.apache.mahout.utils.vectors.lucene.ClusterLabels         \
#        --dir           src/main/resources/seinfeld-scripts-index   \
#        --pointsDir     $OUTPUT/clusters/clusteredPoints            \
#        --seqFileDir    $OUTPUT/seqfiles                            \
#        --idField       id                                          \
#        --field         content                                     \
#        --output        $OUTPUT/cluster-labels.txt                  \

#mahout org.apache.mahout.vectorizer.collocations.llr.CollocDriver       \
#    --input $OUTPUT/seqfiles                                            \
#    --output $OUTPUT/collocations                                       \
#    --maxNGramSize 2                                                    \
#    --minLLR 1000                                                       \
#    --analyzerName org.apache.mahout.analysis.SeinfeldScriptAnalyzer    \
#    --preprocess                                                        \
#    --maxRed 1                                                          \
#    --overwrite
