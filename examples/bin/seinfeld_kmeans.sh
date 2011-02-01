#!/bin/bash

mahout kmeans       --input            out-seinfeld-vectors/tfidf-vectors \
                    --output           out-seinfeld-kmeans/clusters \
                    --clusters         out-seinfeld-kmeans/initialclusters \
                    --maxIter          10 \
                    --numClusters      100 \
                    --clustering       \
                    --overwrite
wait

mahout clusterdump  --seqFileDir        out-seinfeld-kmeans/clusters/clusters-1 \
                    --pointsDir         out-seinfeld-kmeans/clusters/clusteredPoints \
                    --numWords          5 \
                    --dictionary        out-seinfeld-vectors/dictionary.file-0 \
                    --dictionaryType    sequencefile
