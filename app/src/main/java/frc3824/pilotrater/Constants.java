package frc3824.pilotrater;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public interface Constants {

    /**
     Version number changing rules
     - right most number get changed for major changes (after alpha version is finished)
     - middle number is changed after events
     - left most number is changed after the season
     */
    String VERSION = "2.3.1";
    String MATCH_NUMBER = "match_number";

    String EVENT_KEY = "event_key";
    String APP_DATA = "appData";

    String[] SUPER_SCOUTS_LIST = {
            "Steven Busby",
            "Philip Hicks",
            "Abigail Bradfield"
    };

    String FIVE = "5 - Hauling ass";
    String FOUR = "4 - Pretty good";
    String THREE = "3 - Competent";
    String TWO = "2 - Wouldn't pick them as pilot for us";
    String ONE = "1 - Not paying attention/Slow/Clumsy";
    String ZERO = "0 - Not a pilot in this match";

    String[] RATING_OPTIONS = {ZERO, ONE, TWO, THREE, FOUR, FIVE};
}
