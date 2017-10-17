
import android.content.Context;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;



import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class DateUtils {
    // 一天
    private final static long ONE_DAYS = 86400 * 1000;

    private static DateFormat dateFormate_list = null;

    private static DateFormat dateFormate_week = null;

    private static DateFormat dateFormate_month = null;

    private static final String dateFormate_list_PATTEN = "HH:mm";
    private static final String dateFormate_week_PATTEN = "EEE HH:mm";
    private static final String dateFormate_month_PATTEN_EN = "MMM dd HH:mm";
    private static final String dateFormate_week_format = "EEE";
    private static final String dateFormate_month_day = "MMM dd";
    private static final String dateFormate_year_month_day = "yyyy/MM/dd";
    private static final String formate_month_day = "MM/dd";
    // 5.0
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final int ONE_MINUTE_AGO = R.string.minute_ago;
    private static final int ONE_HOUR_AGO = R.string.hour_ago;
    private static final int JUST_NOW = R.string.just_now;

    public static boolean compare_date(String datatime, String Systemdatatime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(datatime);
            Date dt2 = df.parse(Systemdatatime);
            long time = dt2.getTime() - dt1.getTime();
            if (time >= ONE_MINUTE) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * 格式化时间
     *
     * @param timestamp    时间
     * @return 转化为Today, yesterday
     */
    public static String dateChangeStr(long timestamp) {
        try {
            /*当前时间*/
            Calendar today_cal = Calendar.getInstance();
            long today_mills = today_cal.getTimeInMillis();
            int today_week_of_day = today_cal.get(Calendar.DAY_OF_WEEK);
            int today_week_of_month = today_cal.get(Calendar.WEEK_OF_MONTH);
            int today_month = today_cal.get(Calendar.MONTH) + 1;
            int today_year = today_cal.get(Calendar.YEAR);

            Calendar chat_cal = Calendar.getInstance();
            chat_cal.setTimeInMillis(timestamp);
            long chat_mills = chat_cal.getTimeInMillis();
            int chat_week_of_day = chat_cal.get(Calendar.DAY_OF_WEEK);
            int chat_week_of_month = chat_cal.get(Calendar.WEEK_OF_MONTH);
            int chat_month = chat_cal.get(Calendar.MONTH) + 1;
            int chat_year = chat_cal.get(Calendar.YEAR);

            if (Math.abs(today_year - chat_year) > 0) { //相差大于一年
                /*yyyy/mm/dd*/
                return getDateYYYY_MM_dd().format(chat_mills);
            } else if (today_year == chat_year) { //同年
                if (Math.abs(today_month - chat_month) > 0) { //相差大于一月
                    /*yyyy/mm/dd*/
                    return getDateFormate_month().format(chat_mills);
                } else if (today_month == chat_month) { //同月
                    if (Math.abs(today_week_of_month - chat_week_of_month) > 0) { //相差大于一周
                        /*yyyy/mm/dd*/
                        return getDateFormate_month().format(chat_mills);
                    } else if (today_week_of_month == chat_week_of_month) { //同周
                        if (Math.abs(today_week_of_day - chat_week_of_day) > 1) { //相差大于一天，昨天以前
                            /*Thur*/
                            return getDateFormate_week().format(chat_mills);
                        } else if (Math.abs(today_week_of_day - chat_week_of_day) == 1) { //相差一天(昨天)
                             /*Yesterday*/
                            return PalmGroupApplication.getmPalmGroupApplication().getApplicationContext().getString(R.string.yesterday) + " " + getDateFormate_list().format(chat_mills);
                        } else { //小于一天(当天内)
                              /*HH:mm*/
                            return PalmGroupApplication.getmPalmGroupApplication().getApplicationContext().getString(R.string.today) + " " + getDateFormate_list().format(chat_mills);
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化时间
     *
     * @param date    时间
     * @param context
     * @return 转化为Today, yesterday
     */
    public static String dateChangeStrForChats(Date date, Context context) {
        if (null == date) {
            return "";
        }
        try {
            Calendar cal = Calendar.getInstance();
            int week1 = cal.get(Calendar.DAY_OF_WEEK);
            long timeInMillis1 = cal.getTimeInMillis();

            cal.setTime(date);
            long timeInMillis2 = cal.getTimeInMillis();
            int week2 = cal.get(Calendar.DAY_OF_WEEK);

            if (timeInMillis1 < timeInMillis2) {
                if (timeInMillis2 - timeInMillis1 < ONE_DAYS && week1 == week2) {
                    return getDateFormate_list().format(date);
                } else {
                    return getDateFormate_month_day().format(date);
                }
            }
            int day = (int) Math.abs((timeInMillis1 - timeInMillis2) / ONE_DAYS);
            if (day < 1 && week1 == week2) {
                return getDateFormate_list().format(date);
            } else if (day < 2 && (Math.abs(week1 - week2) == 1 || Math.abs(week1 - week2) == 6)) {
                return context.getString(R.string.yesterday);
            } else if (day < 7 && week1 != week2) {
                return getDateFormate_only_week_short().format(date);
            } else {
                return getDateFormate_month_day().format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private synchronized static DateFormat getDateFormate_list() {
        if (dateFormate_list == null) {
            dateFormate_list = new SimpleDateFormat(dateFormate_list_PATTEN);
        }
        return dateFormate_list;
    }

    private synchronized static DateFormat getDateFormate_week() {
        if (dateFormate_week == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddWeekAbbreviations = new String[]{"", context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday)};
            symbols.setShortWeekdays(oddWeekAbbreviations);
            dateFormate_week = new SimpleDateFormat(dateFormate_week_PATTEN, symbols);
        }
        return dateFormate_week;
    }

    private synchronized static DateFormat getDateFormate_only_week_short() {
        if (dateFormate_week == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddWeekAbbreviations = new String[]{"", context.getString(R.string.sunday_short), context.getString(R.string.monday_short), context.getString(R.string.tuesday_short), context.getString(R.string.wednesday_short), context.getString(R.string.thursday_short), context.getString(R.string.friday_short), context.getString(R.string.saturday_short)};
            symbols.setShortWeekdays(oddWeekAbbreviations);
            dateFormate_week = new SimpleDateFormat(dateFormate_week_format, symbols);
        }
        return dateFormate_week;
    }

    private synchronized static DateFormat getDateFormate_week(String template) {
        if (dateFormate_week == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddWeekAbbreviations = new String[]{"", context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wednesday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday)};
            symbols.setShortWeekdays(oddWeekAbbreviations);
            dateFormate_week = new SimpleDateFormat(template, symbols);
        }
        return dateFormate_week;
    }

    private synchronized static DateFormat getDateFormate_month() {
        if (dateFormate_month == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddMonthAbbreviations = new String[]{context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec)};
            symbols.setShortMonths(oddMonthAbbreviations);
            dateFormate_month = new SimpleDateFormat(dateFormate_month_PATTEN_EN, symbols);
        }
        return dateFormate_month;
    }

    private synchronized static DateFormat getDateFormate_month_day() {
        if (dateFormate_month == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddMonthAbbreviations = new String[]{context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec)};
            symbols.setShortMonths(oddMonthAbbreviations);
            dateFormate_month = new SimpleDateFormat(dateFormate_month_day, symbols);
        }
        return dateFormate_month;
    }

    public synchronized static DateFormat getDateFormate_month(String template) {
        if (dateFormate_month == null) {
            DateFormatSymbols symbols = new DateFormatSymbols();
            Context context = PalmGroupApplication.getmPalmGroupApplication();
            String[] oddMonthAbbreviations = new String[]{context.getString(R.string.month_jan), context.getString(R.string.month_feb), context.getString(R.string.month_mar), context.getString(R.string.month_apr), context.getString(R.string.month_may), context.getString(R.string.month_june), context.getString(R.string.month_july), context.getString(R.string.month_aug), context.getString(R.string.month_sept), context.getString(R.string.month_oct), context.getString(R.string.month_nov), context.getString(R.string.month_dec)};
            symbols.setShortMonths(oddMonthAbbreviations);
            dateFormate_month = new SimpleDateFormat(template, symbols);
        }
        return dateFormate_month;
    }

    /**
     * language change(area change area)
     */
    public static void laguageOrTimeZoneChanged() {
        dateFormate_week = null;
        dateFormate_month = null;
        dateFormate_list = null;
    }

    private static final int HOUR_BY_MIN = 60;
    private static final int DAY_BY_MIN = HOUR_BY_MIN * 24;


    /**
     * 格式化时间
     *
     * @param milliseconds
     * @param pattern
     * @return
     */
    public static String getFormatDateTime(long milliseconds, String pattern) {
        Date date = new Date(milliseconds);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String str = sf.format(date);
        return str;
    }

    public static Long getHours(long time) {
        long diff = System.currentTimeMillis() - time;// 这样得到的差值是微秒级别
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        return hours;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static Date getDateTime(String currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getDateTime(Long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date = new Date(currentTime);
        String stringa = formatter.format(date);
        return stringa;
    }
    public static String getTimeMonth(Context mContext, String time) {
        String str = time;
        String sr = str.substring(3, 5);
        String aop = "";
        String times = "";
        if ("01".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_jan);
            times = time.replace("-01", aop);
            return times;
        } else if ("02".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_feb);
            times = time.replace("-02", aop);
            return times;
        } else if ("03".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_mar);
            times = time.replace("-03", aop);
            return times;
        } else if ("04".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_apr);
            times = time.replace("-04", aop);
            return times;
        } else if ("05".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_may);
            times = time.replace("-05", aop);
            return times;
        } else if ("06".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_june);
            times = time.replace("-06", aop);
            return times;
        } else if ("07".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_july);
            times = time.replace("-07", aop);
            return times;
        } else if ("08".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_aug);
            times = time.replace("-08", aop);
            return times;
        } else if ("09".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_sept);
            times = time.replace("-09", aop);
            return times;
        } else if ("10".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_oct);
            times = time.replace("-10", aop);
            return times;
        } else if ("11".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_nov);
            times = time.replace("-11", aop);
            return times;
        } else if ("12".equals(sr)) {
            aop = "-" + mContext.getString(R.string.month_dec);
            times = time.replace("-12", aop);
            return times;
        }
        return null;
    }


    /**
     * 显示时间(xxHxxm)和日期（xxMxxD）
     *
     * @param context
     * @param when
     * @return
     */
    public static String formatTimeStampString(Context context, long when) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();
        String date = "";
        // Basic settings for formatDateTime() we want for all cases.
        int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT |
                android.text.format.DateUtils.FORMAT_ABBREV_ALL |
                android.text.format.DateUtils.FORMAT_CAP_AMPM;

        // If the message is from a different year, show the date and year.
        if (then.year != now.year) {
            format_flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR | android.text.format.DateUtils.FORMAT_SHOW_DATE;
            date = getDateToStringYear(when);
        } else if (then.yearDay != now.yearDay) {
            // If it is from a different day than today, show only the date.
            format_flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
            date = getDateToStringDay(when);
        } else {
            // Otherwise, if the message is from today, show the time.
            format_flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
            date = android.text.format.DateUtils.formatDateTime(context, when, format_flags);
        }
        return date;
    }

    /**
     * 显示时间(xxHxxm)和日期（xxMxxD）
     *
     * @param context
     * @param when
     * @return
     */
    public static String formatTimeStampForString(Context context, long when) {
        Date then = new Date(when);
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        long dayNow = cal.get(Calendar.DAY_OF_YEAR);

        cal.setTime(then);
        int yearThen = cal.get(Calendar.YEAR);
        long dayThen = cal.get(Calendar.DAY_OF_YEAR);


        String date = "";
        // Basic settings for formatDateTime() we want for all cases.
        int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT |
                android.text.format.DateUtils.FORMAT_ABBREV_ALL |
                android.text.format.DateUtils.FORMAT_CAP_AMPM;

        // If the message is from a different year, show the date and year.
        if (yearThen != yearNow) {
            date = getDateToStringYear(when);
        } else if (dayNow != dayThen) {
            // If it is from a different day than today, show only the date.
            date = getDateToStringDay(when);
        } else {
            // Otherwise, if the message is from today, show the time.
            Date dateThen = new Date(when);
            date = getDateFormate_list().format(dateThen);
        }
        return date;
    }

    public static String getDateToStringDay(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd");
        return sf.format(d);
    }

    public static String getDateToStringYear(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        return sf.format(d);
    }

    public static String getFileDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
        return sf.format(d);
    }

    /**
     * 比较时间
     *
     * @param yearText
     * @param monthText
     * @param dayText
     * @param view
     * @return
     */
    public static boolean compareDate(CharSequence yearText, CharSequence monthText, CharSequence dayText, View view) {

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date selected = df.parse(yearText + "-" + monthText + "-" + dayText);
            Date today = new Date();
            if (selected.getTime() - today.getTime() < 0) {
                view.setEnabled(true);
                view.setClickable(true);
                return true;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        view.setEnabled(false);
        view.setClickable(false);
        return false;
    }

    /**
     * 年月日 转日月年
     *
     * @param dateStr
     * @return
     */
    public static String yyyyMMddToddMMyyyy(String dateStr) {
        String resultStr = dateStr;
        try {

            if (!TextUtils.isEmpty(dateStr) && dateStr.contains("-")) {
                String[] str = dateStr.split("-");
                if (str != null && str.length == 3 && str[2].length() == 2 && str[0].length() == 4) {
                    resultStr = str[2] + "-" + str[1] + "-" + str[0];

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    private synchronized static DateFormat getDateYYYY_MM_dd() {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate_year_month_day);
        //sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }


    private synchronized static DateFormat getMM_dd() {
        SimpleDateFormat sdf = new SimpleDateFormat(formate_month_day);
        return sdf;
    }

    /**
     * 格式化chat列表时间
     *
     * @param context
     * @param timestamp 单位为秒
     * @return
     */
    public static String chatFormatTime(Context context, long timestamp) {
//         try {
//             long mills = (long)timestamp * 1000;
//             Date date = new Date(mills);
//
//            Calendar cal = Calendar.getInstance();
//            int week1 = cal.get(Calendar.DAY_OF_WEEK);
//            long timeInMillis1 = cal.getTimeInMillis();
//
//
//            cal.setTime(date);
//            long timeInMillis2 = cal.getTimeInMillis();
//            int week2 = cal.get(Calendar.DAY_OF_WEEK);
//
//
//            if (timeInMillis1 < timeInMillis2) {
//                if (timeInMillis2 - timeInMillis1 < ONE_DAYS  &&  week1 == week2) {
//                    return getDateFormate_list().format(date);
//                } else {
//                    return getDateFormate_month().format(date);
//                }
//            }
//            int day = (int) Math.abs((timeInMillis1 - timeInMillis2) / ONE_DAYS);
//            if (day < 1 && week1 == week2) {
//                return getDateFormate_list().format(date);
//            } else if (day < 2 && (Math.abs(week1 - week2) == 1 || Math.abs(week1 - week2) == 6)) {
//                return context.getString(R.string.yesterday);
//            } else if (day < 7 && week1 != week2) {
//                return getDateYYYY_MM_dd().format(date);
//            } else {
//                return getDateYYYY_MM_dd().format(date);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {


            /*当前时间*/
            Calendar today_cal = Calendar.getInstance();
            long today_mills = today_cal.getTimeInMillis();
            int today_week_of_day = today_cal.get(Calendar.DAY_OF_WEEK);
            int today_week_of_month = today_cal.get(Calendar.WEEK_OF_MONTH);
            int today_month = today_cal.get(Calendar.MONTH) + 1;
            int today_year = today_cal.get(Calendar.YEAR);

            /*chat时间和周*/
            /*先转为毫秒*/
            long chatTime = timestamp;
            Calendar chat_cal = Calendar.getInstance();
            chat_cal.setTimeInMillis(chatTime);
            long chat_mills = chat_cal.getTimeInMillis();
            int chat_week_of_day = chat_cal.get(Calendar.DAY_OF_WEEK);
            int chat_week_of_month = chat_cal.get(Calendar.WEEK_OF_MONTH);
            int chat_month = chat_cal.get(Calendar.MONTH) + 1;
            int chat_year = chat_cal.get(Calendar.YEAR);

            if (Math.abs(today_year - chat_year) > 0) { //相差大于一年
                /*yyyy/mm/dd*/
                return getDateYYYY_MM_dd().format(new Date(chat_mills));
            } else if (today_year == chat_year) { //同年
                if (Math.abs(today_month - chat_month) > 0) { //相差大于一月
                    /*yyyy/mm/dd*/
                    return getDateYYYY_MM_dd().format(new Date(chat_mills));
                } else if (today_month == chat_month) { //同月
                    if (Math.abs(today_week_of_month - chat_week_of_month) > 0) { //相差大于一周
                        /*yyyy/mm/dd*/
                        return getDateYYYY_MM_dd().format(new Date(chat_mills));
                    } else if (today_week_of_month == chat_week_of_month) { //同周
                        if (Math.abs(today_week_of_day - chat_week_of_day) > 1) { //相差大于一天，昨天以前
                            /*Thur*/
                            return getDateFormate_only_week_short().format(new Date(chat_mills));
                        } else if (Math.abs(today_week_of_day - chat_week_of_day) == 1) { //相差一天(昨天)
                             /*Yesterday*/
                            return context.getString(R.string.yesterday);
                        } else { //小于一天(当天内)
                              /*HH:mm*/
                            return getDateFormate_list().format(new Date(chat_mills));
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
