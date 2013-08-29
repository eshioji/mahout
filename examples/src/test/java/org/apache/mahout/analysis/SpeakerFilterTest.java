package org.apache.mahout.analysis;

import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Frank Scholten
 */
public class SpeakerFilterTest {
  @Test
  public void testIncrementToken() throws Exception {
    String text = "JERRY: Hello, Newman...";

    TokenStream tokenStream = new LetterTokenizer(new StringReader(text));
    TokenStream input = new SpeakerFilter(tokenStream);

    TermAttribute termAttribute = input.addAttribute(TermAttribute.class);

    List<String> terms = new ArrayList<String>();

    while (input.incrementToken()) {
      String term = termAttribute.term();
      terms.add(term);
    }

    assertEquals("Hello", terms.get(0));
    assertEquals("Newman", terms.get(1));
    assertEquals(2, terms.size());
  }
}
