package com.example.thefirstmove.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final long ONE_DAY_MILLS = 3600000 * 24;

    private DateUtil() {

    }

    /**
     * 日期格式     enum(枚举类型):一组类似的值包含到一种类型当中。而这种枚举类型的名称则会被定义成独一无二的类型描述符 -->为申明的变量提供更大的取值范围
     */
    public enum DateFormat {
        /**
         * 格式："yyyy-MM-dd HH:mm:ss"
         */
        ALL_TIME {
            public String getValue() {
                return "yyyy-MM-dd HH:mm:ss";
            }
        },
        /**
         * 格式："yyyy-MM-dd HH:mm"
         */
        ONLY_MINUTE {
            public String getValue() {
                return "yyyy-MM-dd HH:mm";
            }
        },
        /**
         * 格式："yyyy-MM-dd HH"
         */
        ONLY_HOUR {
            public String getValue() {
                return "yyyy-MM-dd HH";
            }
        },
        /**
         * 格式："yyyy-MM-dd"
         */
        ONLY_DAY {
            public String getValue() {
                return "yyyy-MM-dd";
            }
        },
        /**
         * 格式："yyyy-MM"
         */
        ONLY_MONTH {
            public String getValue() {
                return "yyyy-MM";
            }
        },
        /**
         * 格式："MM-dd"
         */
        ONLY_MONTH_DAY {
            public String getValue() {
                return "MM-dd";
            }
        },
        /**
         * 格式："MM-dd HH:mm"
         */
        ONLY_MONTH_SEC {
            public String getValue() {
                return "MM-dd HH:mm";
            }
        },
        /**
         * 格式："HH:mm:ss"
         */
        ONLY_TIME {
            public String getValue() {
                return "HH:mm:ss";
            }
        },
        /**
         * 格式："HH:mm"
         */
        ONLY_HOUR_MINUTE {
            public String getValue() {
                return "HH:mm";
            }
        };

        public abstract String getValue();
    }

    /**
     * 获取当前时间  格式---->2019-08-04    13:30:29
     */
    public static String getNowTime(DateFormat format) {
        String nowtime = null;
        Calendar calendar = Calendar.getInstance();
        Date dateNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue(), Locale.CHINA);
        nowtime = sdf.format(dateNow);
        return nowtime;
    }

    /**
     * 将一个日期字符串转换成Data对象         string-->date
     *
     * @param dateString 日期字符串
     * @param format     转换格式
     * @return
     */
    public static Date stringToDate(String dateString, DateFormat format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue(), Locale.CHINA);
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将date转换成字符串               date--->string
     *
     * @param date   日期
     * @param format 日期目标格式
     * @return
     */
    public static String dateToString(Date date, DateFormat format) {
        String string = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue(), Locale.CHINA);
        string = sdf.format(date);
        return string;
    }

    /**
     * 获取指定日期
     *
     * @param date 指定日期
     * @return 返回值为： "周日", "周一", "周二", "周三", "周四", "周五", "周六"
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0)
            week = 0;
        return weekDays[week];
    }

    /**
     * 获取指定日期对应周几的序列
     *
     * @param date 指定日期
     * @return 周一：1    周二：2    周三：3    周四：4    周五：5    周六：6    周日：7
     */
    public static int getIndexWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_WEEK);
        if (index == 1) {
            return 7;
        } else {
            return --index;
        }
    }

    /**
     * 获取当前月份
     */
    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前月号
     */
    public static int getNowDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当前年份
     */
    public static int getNowYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取本月份的天数
     */
    public static int getNowDaysOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return daysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.DATE) + 1);
    }

    /**
     * 计算两个日期之间的年份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getYearGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }
        Calendar helpCalendar = Calendar.getInstance();
        helpCalendar.setTime(firstDate);
        int firstYear = helpCalendar.get(Calendar.YEAR);
        helpCalendar.setTime(secondDate);
        int secondYear = helpCalendar.get(Calendar.YEAR);
        return secondYear - firstYear;
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysGapOfDates(Date startDate, Date endDate) {
        int date = 0;
        if (startDate != null && endDate != null) {
            date = getDaysBetween(startDate, endDate);
        }
        return date;
    }

    /**
     * 计算两个日期之间的月份差距
     *
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static int getMonthGapOfDates(Date firstDate, Date secondDate) {
        if (firstDate == null || secondDate == null) {
            return 0;
        }

        return (int) ((secondDate.getTime() - firstDate.getTime())
                / ONE_DAY_MILLS / 30);

    }

    /**
     * 获取指定月份的天数
     *
     * @param year  年份
     * @param month 月份
     * @return 对应天数
     */
    public static int daysOfMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if ((year % 4 == 0 && year % 100 == 0) || year % 400 != 0) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    private static int getDaysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / ONE_DAY_MILLS);
    }
}
