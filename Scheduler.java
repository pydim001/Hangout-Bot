import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;

public class Scheduler {
    public static Calendar calendar = Calendar.getInstance();
    public static String day;
    public static int minutes;
    public static int hours;
    public static Time time;
    public static HashMap<Integer, String> weekMap = new HashMap<>(); 

    public Scheduler(){}

    public static void getTime(){
        if(weekMap.size() != 7){
            weekMap.put(1, "Sunday");
            weekMap.put(2, "Monday");
            weekMap.put(3, "Tuesday");
            weekMap.put(4, "Wednesday");
            weekMap.put(5, "Thursday");
            weekMap.put(6, "Friday");
            weekMap.put(7, "Saturday");
        }

        int minutes = calendar.get(Calendar.MINUTE);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        time = new Time(hours, minutes);
        day = weekMap.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static void checkTime(JSONObject object, Time time, Read read){
        ArrayList<WHEN> whens = new ArrayList<WHEN>();
        Collection<JSONArray> values = object.values(); 
        ArrayList<JSONArray> objValues = new ArrayList<JSONArray>(values);
        ArrayList<Object> courseList = new ArrayList<Object>();
        for(Object className: object.keySet()){
            courseList.add(className);
        }
        for(JSONArray times: objValues){
            whens.add(time.when(Time.toTime(String.valueOf(times.get(0))), Time.toTime(String.valueOf(times.get(1)))));
        }
        if(object.size() == 0){
            read.message = " had no classes today";
            read.secMessage = "";
        }else{
            if(whens.contains(WHEN.BETWEEN)){
                ArrayList<String> classtime = objValues.get(whens.indexOf(WHEN.BETWEEN));
                int minutesleft = time.timeLeft(Time.toTime(classtime.get(1))).toMinutes();
                String course = String.valueOf(courseList.get(whens.indexOf(WHEN.BETWEEN)));
                String room = (String) objValues.get(whens.indexOf(WHEN.BETWEEN)).get(2);
                read.message = " is in " + course + " in " + room;
                read.secMessage = "It ends in " + hourFormat(minutesleft) + " minutes";
            }else if(whens.get(0).equals(WHEN.BEFORE)){
                read.message = "has no class right now";
                ArrayList<String> firstClass = objValues.get(0);
                int minutesLeft = time.timeLeft(Time.toTime(firstClass.get(0))).toMinutes();
                read.secMessage = String.valueOf(courseList.get(0)) + " starts in " + hourFormat(minutesLeft) + " minutes";
            }else if(whens.get(whens.size() - 1).equals(WHEN.AFTER)){
                read.message = " has no more classes left";
                ArrayList<String> lastClass = objValues.get(objValues.size() - 1);
                int minutesAgo = Time.toTime(lastClass.get(1)).timeLeft(time).toMinutes();
                read.secMessage = "The last class ended " + hourFormat(minutesAgo) + " minutes ago";
            }else{
                int indexBreak = 0;
                for(int i = 1; i < whens.size(); i++){
                    if(whens.get(i).equals(whens.get(i - 1))){
                        indexBreak = i;
                        break;
                    }
                }
                read.message = " has no classes right now";
                ArrayList<String> courseInfo = objValues.get(indexBreak);
                int minutesLeft = time.timeLeft(Time.toTime(courseInfo.get(0))).toMinutes();
                read.secMessage = courseInfo.get(2) + " is in " + hourFormat(minutesLeft) + " minutes";
            }
        }
    }

    public static EmbedBuilder classFormat(JSONObject object, EmbedBuilder embed){
        embed.setTitle("Classes");
        ArrayList<String> courses = new ArrayList<String>();
        for(Object course: object.keySet()){
            String courseString = String.valueOf(course);
            JSONArray courseInfo = (JSONArray) object.get(course);
            String startTime = militaryToStandard(String.valueOf(courseInfo.get(0)));
            String endTime = militaryToStandard(String.valueOf(courseInfo.get(1)));
            String place = String.valueOf(courseInfo.get(2));
            courses.add(courseString + ": " + startTime + " - " + endTime + " at " + place + "\r");
        }
        embed.setDescription(String.join("\r", courses));
        return embed;
    }


    public static String hourFormat(int minutes){
        int[] hm = Time.minutestoHour(minutes);
        return hm[0] + " hours and " + hm[1];
    }

    public static String militaryToStandard(String time){
        int temphours = Integer.parseInt(time.substring(0, time.length() - 3));
        String tempminutes = time.substring(time.length() - 2, time.length());
        String timeOfDay;
        if(temphours > 12) {
            temphours -= 12;
            timeOfDay = "pm";
        } else if(temphours == 12) timeOfDay = "pm";
        else if(temphours == 0){ 
            temphours += 12; 
            timeOfDay = "am";
        }else timeOfDay = "am";
        return temphours + ":" + tempminutes + timeOfDay;
    }

    public static void main(String[] args) {
        Scheduler.getTime();
        System.out.println(Scheduler.time);
        System.out.println(day);
        

    }
}
