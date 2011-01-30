package org.apache.mahout.analysis;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;
import java.lang.Override;import java.lang.String;import java.lang.SuppressWarnings;import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Analyzes Seinfeld scripts by removing numbers, names of characters that are speaking, and mute subtitles and some
 * stop words.
 *
 * @author Frank Scholten
 */
public class SeinfeldScriptAnalyzer extends StandardAnalyzer {

  final List<String> stopWords = Arrays.asList(
          "a", "an", "and", "are", "as", "at", "be", "but", "by",
          "do", "don't", "for", "he", "i", "if", "in", "i'm", "into", "is", "it", "know", "me",
          "no", "not", "oh", "of", "on", "or", "such",
          "get", "just", "from", "about",
          "that", "the", "their", "then", "there", "these",
          "they", "this", "to", "what", "was", "we", "will", "with", "you're",
          "jerry", "george", "elaine", "kramer", "yeah", "you", "she", "have", "her", "his", "like", "well", "out"
  );

  public SeinfeldScriptAnalyzer() {
    super(Version.LUCENE_30);
  }

  @SuppressWarnings("unchecked")
  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    TokenStream tokenStream = super.tokenStream(fieldName, reader);

    final Set stopSet = new CharArraySet(stopWords.size(), true);
    stopSet.addAll(stopWords);

    tokenStream = new StopFilter(true, tokenStream, stopSet, true);

    // TODO: Filter numbers

    // TODO: Filter mute subtitles

    return new LengthFilter(tokenStream, 4, 20);
  }
}