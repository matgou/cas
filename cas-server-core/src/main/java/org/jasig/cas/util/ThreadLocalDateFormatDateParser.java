/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.util;

import org.jasig.cas.server.util.DateParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Simple, performant, implementation that keeps one simple date format instance per thread per date format type.
 * <p>
 * Classes may want to extend this to provide a default formatting.
 * <p>
 * The SimpleDateFormat's used in this are automatically set to the UTC time zone.  This puts everything in the format
 * that Java started with.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class ThreadLocalDateFormatDateParser implements DateParser {

    private final ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal;

    public ThreadLocalDateFormatDateParser(final String dateFormat) {
        this.simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            return simpleDateFormat;
        }
    };

    }

    public final Date parse(final String dateAsString) {
        try {
            return this.simpleDateFormatThreadLocal.get().parse(dateAsString);
        } catch (final ParseException e) {
            return null;
        }
    }

    public final String format(final Date date) {
        return this.simpleDateFormatThreadLocal.get().format(date);
    }
}