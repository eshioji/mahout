#!/bin/bash

bin/mahout seqdirectory --input             examples/src/main/resources/seinfeld-scripts-preprocessed \
                        --output            out-seinfeld-seqfiles \
                        --charset           utf-8

wait

bin/mahout seq2sparse   --input             out-seinfeld-seqfiles    \
                        --output            out-seinfeld-vectors     \
                        --maxNGramSize      2                        \
                        --namedVector                                \
                        --minDF             4                        \
                        --maxDFPercent      75                       \
                        --weight            TFIDF                    \
                        --norm              2                        \
                        --analyzerName      org.apache.mahout.analysis.SeinfeldScriptAnalyzer
