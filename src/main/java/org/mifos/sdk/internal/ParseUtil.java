/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.internal;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Utility class with many useful parsing methods.
 */
public class ParseUtil {

    /**
     * Parses a date to a particular format and returns the formatted {@link String}.
     * @param date the {@link Date} to parse
     * @param dateFormat the date format
     * @param lang the language/locale
     * @return a date {@link String} formatted according to the date format and locale
     */
    public static String parseDateToString(final Date date,
                                           final String dateFormat,
                                           final String lang) {
        Preconditions.checkNotNull(date);
        Preconditions.checkNotNull(dateFormat);
        Preconditions.checkNotNull(lang);

        final Locale locale = new Locale(lang);
        final SimpleDateFormat format = new SimpleDateFormat(dateFormat, locale);

        return format.format(date);
    }

    /**
     * Parses a {@link JsonArray} to a {@link Date}.
     * @param array the {@link JsonArray} of the date in the form of [year, month, day]
     * @return a {@link Date} parsed in the "dd MMMM yyyy" format
     * @throws ParseException
     */
    public static Date parseDateFromJsonArray(final JsonArray array) throws ParseException {
        Preconditions.checkNotNull(array);

        final GregorianCalendar calendar = new GregorianCalendar(array.get(0).getAsInt(),
                array.get(1).getAsInt() - 1, array.get(2).getAsInt());
        final SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");

        return format.parse(format.format(calendar.getTime()));
    }

}
