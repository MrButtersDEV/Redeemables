package me.mrbutters.dev.redeemables.Utils;

public class TimeAfter {

    long timeAfter;

    public TimeAfter(long milliseconds) {
        timeAfter = milliseconds;
    }

    public TimeAfter(long seconds, long milliseconds) {
        timeAfter = (seconds*1000) + milliseconds;
    }

    public TimeAfter(long minute, long seconds, long milliseconds) {
        timeAfter = (minute*60000) + (seconds*1000) + milliseconds;
    }

    public TimeAfter(long hours, long minute, long seconds, long milliseconds) {
        timeAfter = (hours*3600000) + (minute*60000) + (seconds*1000) + milliseconds;
    }

    public TimeAfter(long days, long hours, long minute, long seconds, long milliseconds) {
        timeAfter = (days*86400000) + (hours*3600000) + (minute*60000) + (seconds*1000) + milliseconds;
    }

    public TimeAfter(long weeks, long days, long hours, long minute, long seconds, long milliseconds) {
        timeAfter = (weeks*604800000) + (days*86400000) + (hours*3600000) + (minute*60000) + (seconds*1000) + milliseconds;
    }

    public long getTimeAfter() {
        return timeAfter;
    }

}
