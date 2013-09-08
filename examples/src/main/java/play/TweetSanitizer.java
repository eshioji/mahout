package play;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;


/**
 * @author Enno Shioji ()
 */
public class TweetSanitizer  {
    private static final Pattern URL = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    private static final Pattern AT = Pattern.compile("@[A-Za-z0-9_]+");
    private static final Pattern HASH = Pattern.compile("#");
    private static final Pattern LINEBREAKS = Pattern.compile("\\r\\n|\\r|\\n");

    private static final CharMatcher NOT_ASCII = new CharMatcher() {
        @Override
        public boolean matches(char c) {
            boolean isAscii = CharMatcher.ASCII.matches(c);
            return !isAscii;
        }
    };

    public String sanitize(String input, boolean removeUrls, boolean removeMentions, boolean removeHashes) throws Exception {
        if (StringUtils.isEmpty(input)) {
            return null;
        }

        String sanitized = input;


        // Remove URL & at mentions to avoid over fitting
        if (removeUrls) {
            sanitized = URL.matcher(sanitized).replaceAll(" ");
        }
        if (removeMentions) {
            sanitized = AT.matcher(sanitized).replaceAll(" ");
        }

        // Remove the hash character so that Lucene's StandardAnalyzer will keep the hashtags
        if (removeHashes) {
            sanitized = HASH.matcher(sanitized).replaceAll(" ");
        }

        // Normalise representation
        sanitized = Normalizer.normalize(sanitized, Normalizer.Form.NFC);

        // Remove newlines
        sanitized = LINEBREAKS.matcher(sanitized).replaceAll(" ");

        // Trim whitespace
        sanitized = CharMatcher.WHITESPACE.trimAndCollapseFrom(sanitized,' ');


        // Remove non ascii chars
        return NOT_ASCII.replaceFrom(sanitized, ' ');
    }

}
