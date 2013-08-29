package org.apache.mahout.analysis;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;

/**
 * Filters mute subtitles in the format '[ At the comedy club ]'
 *
 * @author Frank Scholten
 */
public class MuteSubtitleTokenFilter extends TokenFilter {

  /**
   * Construct a token stream filtering the given input.
   */
  protected MuteSubtitleTokenFilter(TokenStream input) {
    super(input);
  }

  @Override
  public boolean incrementToken() throws IOException {


    return false;
  }
}
