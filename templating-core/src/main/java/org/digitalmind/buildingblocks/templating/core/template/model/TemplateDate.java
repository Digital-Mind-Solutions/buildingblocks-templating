package org.digitalmind.buildingblocks.templating.core.template.model;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateDate extends Date {
    private static ConcurrentHashMap<String, SimpleDateFormat> formatMap = new ConcurrentHashMap<>();

    public TemplateDate() {
    }

    public TemplateDate(long date) {
        super(date);
    }

    public TemplateDate(Date date) {
        super(date != null ? date.getTime() : System.currentTimeMillis());
    }

    public String format(String format) {
        return this.format(format, Locale.getDefault(Locale.Category.FORMAT));
    }

    public String format(String format, Locale locale) {
        SimpleDateFormat sdf = null;
        String key = format + "-" + locale.toString();
        sdf = formatMap.get(key);
        if (sdf == null) {
            sdf = new SimpleDateFormat(format, locale);
            formatMap.put(key, sdf);
        }
        return sdf.format(this);
    }

    public boolean isSameDay(final Date date) {
        return DateUtils.isSameDay(this, date);
    }

    public boolean isSameInstant(final Date date) {
        return DateUtils.isSameInstant(this, date);
    }

    public TemplateDate addYears(int offset) {
        return new TemplateDate(DateUtils.addYears(this, offset));
    }

    public TemplateDate addMonths(int offset) {
        return new TemplateDate(DateUtils.addMonths(this, offset));
    }

    public TemplateDate addWeeks(int offset) {
        return new TemplateDate(DateUtils.addWeeks(this, offset));
    }

    public TemplateDate addDays(int offset) {
        return new TemplateDate(DateUtils.addDays(this, offset));
    }

    public TemplateDate addHours(int offset) {
        return new TemplateDate(DateUtils.addHours(this, offset));
    }

    public TemplateDate addMinutes(int offset) {
        return new TemplateDate(DateUtils.addMinutes(this, offset));
    }

    public TemplateDate addSeconds(int offset) {
        return new TemplateDate(DateUtils.addSeconds(this, offset));
    }

    public TemplateDate addMilliseconds(int offset) {
        return new TemplateDate(DateUtils.addMilliseconds(this, offset));
    }


    public TemplateDate years(int offset) {
        return new TemplateDate(DateUtils.setYears(this, offset));
    }

    public TemplateDate months(int offset) {
        return new TemplateDate(DateUtils.setMonths(this, offset));
    }

    public TemplateDate days(int offset) {
        return new TemplateDate(DateUtils.setDays(this, offset));
    }

    public TemplateDate hours(int offset) {
        return new TemplateDate(DateUtils.setHours(this, offset));
    }

    public TemplateDate minutes(int offset) {
        return new TemplateDate(DateUtils.setMinutes(this, offset));
    }

    public TemplateDate seconds(int offset) {
        return new TemplateDate(DateUtils.setSeconds(this, offset));
    }

    public TemplateDate milliseconds(int offset) {
        return new TemplateDate(DateUtils.setMilliseconds(this, offset));
    }


    public TemplateDate truncateYears() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.YEAR));
    }

    public TemplateDate truncateMonths() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.MONTH));
    }

    public TemplateDate truncateDays() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.DATE));
    }

    public TemplateDate truncateHours() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.HOUR));
    }

    public TemplateDate truncateMinutes() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.MINUTE));
    }

    public TemplateDate truncateSeconds() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.SECOND));
    }

    public TemplateDate truncateMilliseconds() {
        return new TemplateDate(DateUtils.truncate(this, Calendar.MILLISECOND));
    }


    public TemplateDate ceilingYears() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.YEAR));
    }

    public TemplateDate ceilingMonths() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.MONTH));
    }

    public TemplateDate ceilingDays() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.DATE));
    }

    public TemplateDate ceilingHours() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.HOUR));
    }

    public TemplateDate ceilingMinutes() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.MINUTE));
    }

    public TemplateDate ceilingSeconds() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.SECOND));
    }

    public TemplateDate ceilingMilliseconds() {
        return new TemplateDate(DateUtils.ceiling(this, Calendar.MILLISECOND));
    }

}
