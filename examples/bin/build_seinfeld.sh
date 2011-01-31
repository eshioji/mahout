#!/bin/sh

INPUT=src/main/resources/seinfeld-scripts-preprocessed
OUTPUT=out/seinfeld-scripts

#mahout seqdirectory --input             $INPUT \
#                    --output            $OUTPUT/seqfiles \
#                    --charset           utf-8

#wait

mahout seq2sparse   --input             $OUTPUT/seqfiles    \
                    --output            $OUTPUT/vectors     \
                    --maxNGramSize      2                   \
                    --namedVector                           \
                    --minDF             4                   \
                    --maxDFPercent      50                  \
                    --weight            TFIDF               \
                    --norm              2                   \
                    --analyzerName      org.apache.mahout.analysis.SeinfeldScriptAnalyzer

wait

mahout kmeans       --input            $OUTPUT/vectors/tfidf-vectors \
                    --output           $OUTPUT/clusters \
                    --clusters         $OUTPUT/initialclusters \
                    --distanceMeasure
                    --maxIter          10 \
                    --numClusters      100 \
                    --clustering
                    --overwrite

#mahout fkmeans      --input             $OUTPUT/vectors/tfidf-vectors \
#                    --output            $OUTPUT/clusters \
#                    --clusters          $OUTPUT/initialclusters \
#                    --distanceMeasure   org.apache.mahout.common.distance.CosineDistanceMeasure \
#                    --maxIter           10 \
#                    --numClusters       100 \
#                    --clustering            \
#                    --m                 2   \
#                    --overwrite

wait

#mahout clusterdump  --seqFileDir        $OUTPUT/clusters/clusters-1 \
#                    --pointsDir         $OUTPUT/clusters/clusteredPoints \
#                    --numWords          5 \
#                    --dictionary        $OUTPUT/vectors/dictionary.file-0 \
#                    --dictionaryType    sequencefile