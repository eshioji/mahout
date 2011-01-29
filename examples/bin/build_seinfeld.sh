#!/bin/sh

INPUT=src/main/resources/seinfeld-scripts
OUTPUT=out/seinfeld-scripts

mahout seqdirectory --input     $INPUT \
                    --output    $OUTPUT/seqfiles \
                    --charset    utf-8

wait

mahout seq2sparse   --input           $OUTPUT/seqfiles    \
                    --output          $OUTPUT/vectors     \
                    --maxNGramSize    2                   \
                    --namedVector                         \
                    --minDF           4                   \
                    --maxDFPercent   50                   \
                    --weight          TFIDF               \
                    --norm            2                   \
                    --analyzerName   org.apache.mahout.analysis.SeinfeldScriptAnalyzer

wait

mahout kmeans       --input         $OUTPUT/vectors/tfidf-vectors \
                    --output        $OUTPUT/clusters \
                    --clusters      $OUTPUT/initialclusters \
                    --maxIter       25 \
                    --numClusters   25 \
                    --clustering