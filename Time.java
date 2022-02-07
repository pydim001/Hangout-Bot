public class Time {
    //Military Time
    public int hours;
    public int minutes;

    public Time(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }

    public static Time toTime(String time){
        int hours = Integer.parseInt(time.substring(0, time.length() - 3));
        int minutes = Integer.parseInt(time.substring(time.length() - 2, time.length()));

        return new Time(hours, minutes);
    }

    public String toString(){
        return this.hours + ":" + this.minutes;
    }

    public int toMinutes(){
        return this.hours * 60 + this.minutes;
    }

    public static int[] minutestoHour(int minutes){
        int[] div = {minutes/60, minutes%60};
        return div;
    }

    public boolean equals(Time time){
        if(this.hours == time.hours && this.minutes == time.minutes) return true;
        return false;
    }

    public Time timeLeft(Time end){
        if(end.minutes >= this.minutes){
            return new Time(end.hours - this.hours, end.minutes - this.minutes);
        }else return new Time(end.hours - this.hours - 1, end.minutes - this.minutes + 60);
    }

    public WHEN when(Time start, Time end){
        if(end.hours > start.hours){
            Time first = start;
            Time second = end;
            if(this.hours < first.hours) return WHEN.BEFORE;
            else if(this.hours > second.hours) return WHEN.AFTER;
            else if(this.hours == second.hours && this.minutes > second.minutes) return WHEN.AFTER;
            else if(this.hours == first.hours && this.minutes < first.minutes) return WHEN.BEFORE;
            else return WHEN.BETWEEN;
        }else if(end.hours < start.hours){
            Time first = end;
            Time second = start;
            if(this.hours < first.hours) return WHEN.BEFORE;
            else if(this.hours > second.hours) return WHEN.AFTER;
            else if(this.hours == second.hours && this.minutes > second.minutes) return WHEN.AFTER;
            else if(this.hours == first.hours && this.minutes < first.minutes) return WHEN.BEFORE;
            else return WHEN.BETWEEN;
        }else {
            if(end.minutes > start.minutes){
                Time first = start;
                Time second = end;
                if(this.hours < first.hours) return WHEN.BEFORE;
                else if(this.hours > second.hours) return WHEN.AFTER;
                else if(this.hours == second.hours && this.minutes > second.minutes) return WHEN.AFTER;
                else if(this.hours == first.hours && this.minutes < first.minutes) return WHEN.BEFORE;
                else return WHEN.BETWEEN;
            }else if(end.minutes < start.minutes){
                Time first = end;
                Time second = start;
                if(this.hours < first.hours) return WHEN.BEFORE;
                else if(this.hours > second.hours) return WHEN.AFTER;
                else if(this.hours == second.hours && this.minutes > second.minutes) return WHEN.AFTER;
                else if(this.hours == first.hours && this.minutes < first.minutes) return WHEN.BEFORE;
                else return WHEN.BETWEEN;
            }else return WHEN.BETWEEN;
        }
        
    }
}
