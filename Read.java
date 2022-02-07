import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Read {
    public static JSONParser parser = new JSONParser();
    public JSONObject schedules;
    public String name;
    public String day;
    public String message;
    public String secMessage;
    public JSONObject classes;
    public static ArrayList<Object> people;

    public Read(){
        
    }

    public Read(String name) {
        this.name = name;
        Scheduler.getTime();
        this.day = Scheduler.day;
    }

    public Read(String name, String day) {
        this.name = name;
        this.day = day;
    }
    
    public void ScheduleRead() throws IOException, ParseException{
        FileReader reader = new FileReader("schedules.json");
        schedules = (JSONObject) parser.parse(reader);
        
        for(Object user: schedules.keySet()){
            String person = String.valueOf(user);
            if (name.equals(person)){
                classes = (JSONObject) schedules.get(person);
                classes = (JSONObject) classes.get(day);
                schedules = classes;
            }   
        }
    }

    public static void people() throws IOException, ParseException{
        FileReader reader = new FileReader("schedules.json");
        JSONObject json = (JSONObject) parser.parse(reader);
        Set<Object> keySet = json.keySet();
        people = new ArrayList<Object>(keySet);
    }
}
