package org.mifos.sdk.internal;

import com.google.common.base.Preconditions;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateParser {

    public static void checkForValidDate(final String date,
                                         final String dateFormat,
                                         final String lang) {
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(dateFormat);
        Preconditions.checkNotNull(lang);

        final Locale locale = new Locale(lang);
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat).withLocale(locale);
        formatter.parseLocalDate(date);
    }

    public static LocalDate parseDate(final String date,
                                      final String dateFormat,
                                      final String lang) {
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(dateFormat);
        Preconditions.checkNotNull(lang);

        final Locale locale = new Locale(lang);
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat).withLocale(locale);
        final LocalDate localDate = formatter.parseLocalDate(date);

        return localDate;
    }

    public static List<Integer> parseToList(final LocalDate localDate) {
        Preconditions.checkNotNull(localDate);

        List<Integer> list = new ArrayList<>();
        list.add(localDate.getYear());
        list.add(localDate.getMonthOfYear());
        list.add(localDate.getDayOfMonth());

        return list;
    }

    public static LocalDate parseFromList(final List<String> list) {
        Preconditions.checkNotNull(list);

        return new LocalDate(Integer.valueOf(list.get(0)), Integer.valueOf(list.get(1)),
                Integer.valueOf(list.get(2)));
    }

}
