package play;


import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author Enno Shioji ()
 */
public class TweetAnalyzer extends Analyzer {
    private static final String separator = "q6c8zrN4xC";
    private static final Joiner joiner = Joiner.on(separator);
    private final TweetSanitizer sanitizer = new TweetSanitizer();

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        CharArraySet stopWords = new CharArraySet(Version.LUCENE_43, EnglishStopWords.stopwords, true);

        // TODO I know this is ridiculous, this is a hack because {@link Twokenize} can't work with streams
        try {
            String tweets = CharStreams.toString(reader);
            tweets = sanitizer.sanitize(tweets, true, false, false);
            List<String> tokenized = Twokenize.tokenizeRawTweetText(tweets);
            Reader fakeUntokenized = new StringReader(joiner.join(tokenized));
            PatternTokenizer patternTokenizer = new PatternTokenizer(fakeUntokenized, Pattern.compile(separator), -1);
            TokenStream result;
            result = new LengthFilter(false,patternTokenizer,3,40);
            result = new StandardFilter(Version.LUCENE_43, result);
            result = new EnglishPossessiveFilter(Version.LUCENE_43, result);
            result = new StopFilter(Version.LUCENE_43, result, stopWords);
            result = new LowerCaseFilter(Version.LUCENE_43, result);

            // Monogram for now
            //result = new ShingleFilter(result, 2, 2);
            return new TokenStreamComponents(patternTokenizer, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
