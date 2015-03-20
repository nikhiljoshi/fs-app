package edu.hm.cs.fs.app.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hugo.weaving.DebugLog;

/**
 * @author Fabio
 *
 */
public final class DataUtils {
    private static final Pattern PATTERN_BOLD = Pattern.compile("\\*([^\\*]+)\\*");
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("(?:#|\\n)");
    private static final Pattern PATTERN_LIST = Pattern.compile("^\\.([^\\.]+)");

    private static final String NEW_LINE_TAG = "<br>";
    private static final String BOLD_OPENING_TAG = "<b>";
    private static final String BOLD_CLOSING_TAG = "</b>";
	private static final String LIST_OPENING_TAG = "<ul>";
	private static final String LIST_CLOSING_TAG = "</ul>";
	private static final String LIST_ITEM_OPENING_TAG = "<li>";
	private static final String LIST_ITEM_CLOSING_TAG = "</li>";

	private DataUtils() {
	}

    @DebugLog
	public static String toHtml(final String content) {
        String result = content;
        result = replaceNewLines(result);
        result = replaceBoldStrings(result);
        return result;
	}

    @DebugLog
    private static String replaceNewLines(String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = PATTERN_NEW_LINE.matcher(raw);
        while(matcher.find()) {
            int indexBegin = matcher.start();

            result.append(raw.substring(lastSubStringEnd, indexBegin));
            result.append(NEW_LINE_TAG);

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if(lastSubStringEnd != raw.length()) {
            result.append(raw.substring(lastSubStringEnd, raw.length()));
        }

        return result.toString();
    }

    @DebugLog
    private static String replaceBoldStrings(String raw) {
        StringBuilder result = new StringBuilder();
        int lastSubStringEnd = 0;

        final Matcher matcher = PATTERN_BOLD.matcher(raw);
        while(matcher.find()) {
            int indexBegin = matcher.start();

            result.append(raw.substring(lastSubStringEnd, indexBegin));
            result.append(BOLD_OPENING_TAG);
            result.append(matcher.group(1));
            result.append(BOLD_CLOSING_TAG);

            lastSubStringEnd = indexBegin + matcher.group().length();
        }

        if(lastSubStringEnd != raw.length()) {
            result.append(raw.substring(lastSubStringEnd, raw.length()));
        }

        return result.toString();
    }

    /*
	private static String insertList(final String string) {
		String result = string;
		final String startString = " .";
		final String endString = " .\n";

		int index = 0;
		while (index > -1) {
			final Matcher matcher = PATTERN_LIST_ITEM.matcher(result);
			if (matcher.find(index)) {
				index = matcher.start();
			} else {
				break;
			}
			String firstPart = result.substring(0, index);
			String secondPart = result.substring(index + startString.length(),
					result.length());
			result = firstPart + LIST_OPENING_TAG + "\n." + secondPart;

			index = result.indexOf(endString, index);
			firstPart = result.substring(0, index);
			secondPart = result.substring(index + endString.length(),
					result.length());
			result = firstPart + "\n" + LIST_CLOSING_TAG + secondPart;
		}

		System.out.println(result);
		return insertListItems(result);
	}

	private static String insertListItems(final String string) {
		String result = string;
		int index = 0;
		while (index > -1) {
			final Matcher matcher = PATTERN_LIST_ITEM.matcher(result);
			if (matcher.find(index)) {
				String group = matcher.group(1);
				index = matcher.start();

				final String firstPart = result.substring(0, index);
				final String secondPart = result.substring(index
						+ matcher.group().length(), result.length());

				group = group.replaceAll("\\.", "");
				result = firstPart + LIST_ITEM_OPENING_TAG + group
						+ LIST_ITEM_CLOSING_TAG + secondPart;
			} else {
				index = -1;
			}
		}
		System.out.println(result);
		return result;
	}

	private static String replaceAll(final String string, final String search,
			final String replacement) {
		String content = string;
		int index;
		boolean open = true;
		while ((index = findNext(content, search, 0)) > -1) {
			final String firstPart = content.substring(0, index);
			final String secondPart = content.substring(index + 1,
					content.length());
			if (open) {
				content = firstPart + "<" + replacement + ">" + secondPart;
				open = false;
			} else {
				content = firstPart + "</" + replacement + ">" + secondPart;
				open = true;
			}
		}
		return content;
	}

	private static int findNext(final String content, final String search,
			final int start) {
		final int indexOf = content.indexOf(search);
		if (content.length() > indexOf && indexOf > 0
				&& content.charAt(indexOf - 1) == '\\') {
			return findNext(content, search, indexOf);
		}
		return indexOf;
	}
	*/

	public static boolean isSameDate(final Date date1, final Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE);
	}

    public static Locale toLocale(final String languageCode) {
        for (final Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equalsIgnoreCase(languageCode)) {
                return locale;
            }
        }
        throw new IllegalArgumentException("Not a valid language code: "
                + languageCode);
    }
}
