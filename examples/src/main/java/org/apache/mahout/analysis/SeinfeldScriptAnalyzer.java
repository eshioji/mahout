package org.apache.mahout.analysis;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.util.Version;

import java.io.IOException;
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
public class SeinfeldScriptAnalyzer extends Analyzer {

  final List<String> stopWords = Arrays.asList(
          "a", "an", "and", "are", "as", "at", "be", "but", "by", "alright", "around", "here", "come", "back",
          "do", "don't", "for", "he", "here", "i", "if", "in", "i'm", "into", "is", "it", "know", "me",
          "no", "not", "oh", "of", "on", "or", "such",
          "get", "gonna", "good", "going", "just", "from", "about",
          "that", "the", "their", "then", "there", "these", "think",
          "they", "this", "to", "what", "was", "we", "will", "with", "you're", "your",
          "jerry", "george", "elaine", "kramer", "want", "yeah", "you", "she", "have", "her", "his", "like", "well", "out"
  );

  public SeinfeldScriptAnalyzer() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public TokenStream tokenStream(String fieldName, Reader reader) {
    TokenStream tokenStream = new LetterTokenizer(reader);
    tokenStream = new SpeakerFilter(tokenStream);
    tokenStream = new StandardFilter(tokenStream);
    tokenStream = new LengthFilter(tokenStream, 4, 20);

    tokenStream = new LowerCaseFilter(tokenStream);

    final Set stopSet = new CharArraySet(stopWords.size(), true);
    stopSet.addAll(stopWords);
    tokenStream = new StopFilter(true, tokenStream, stopSet, true);

    return tokenStream;

    // TODO: Filter mute subtitles
  }
}