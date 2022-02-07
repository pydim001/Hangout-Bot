import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter{
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        String[] shortdays = {"sun", "mon", "tues", "wed", "thur", "fri", "sat"};
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        HashMap<String, String> daymap = new HashMap<String, String>();

        for(int i = 0; i < days.length; i++){
            daymap.put(shortdays[i], days[i]);
        }

        if(args[0].equalsIgnoreCase(";wya")){
            User username = event.getMessage().getMentionedUsers().get(0);
            Scheduler.getTime();
            Read read = new Read(username.getName());
            try{
                read.ScheduleRead();
            }catch(IOException e){
                e.printStackTrace();
            }catch(ParseException e){
                e.printStackTrace();
            }
            Scheduler.checkTime(read.schedules, Scheduler.time, read);
            event.getMessage().reply(username.getAsMention() + read.message + "\r" + read.secMessage).queue();
        }
        else if(args[0].equalsIgnoreCase(";classes")){
            User username = event.getMessage().getMentionedUsers().get(0);
            Scheduler.getTime();
            Read read = new Read(username.getName());
            try{
                read.ScheduleRead();
            }catch(IOException e){
                e.printStackTrace();
            }catch(ParseException e){
                e.printStackTrace();
            }
            EmbedBuilder embed = new EmbedBuilder();
            EmbedBuilder classesEmbed = Scheduler.classFormat(read.schedules, embed);
            event.getChannel().sendMessage(classesEmbed.build()).queue();
            classesEmbed.clear();
        }
        else if(args[0].equalsIgnoreCase(";anyone")){
            EmbedBuilder embed = new EmbedBuilder();
            ArrayList<String> availability = new ArrayList<String>();
            embed.setTitle("Availabilities");
            try {
				Read.people();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
            for(Object user: Read.people){
                String person = String.valueOf(user);
                Read read = new Read(person);
                try {
                    read.ScheduleRead();
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                Scheduler.checkTime(read.schedules, Scheduler.time, read);
                String avail = person + " - \r" + read.message + "\r" + read.secMessage;
                availability.add(avail);
            }
            embed.setDescription(String.join("\r\r", availability));
            event.getChannel().sendMessage(embed.build()).queue();
            embed.clear();
        }
        else if(args[0].equalsIgnoreCase(";help")){
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Hangout Bot Commands");
            String[] commands = {";wya - gives current status of someone, mention them after typing the command", 
            ";classes - gives the classes of the day for someone, mention them after typing the command", 
            ";anyone - gives a list of everyone's availability", 
            ";(some day of the week) - gives the classes of someone at the given day, mention them after typing the command" + "\r" +
            "Use these commands: [;sun, ;mon, ;tues, ;wed, ;thur, ;fri, ;sat]"};
            embed.setDescription(String.join("\r\r", commands));
            event.getChannel().sendMessage(embed.build()).queue();
            embed.clear();
        }
        else if(args[0].substring(0, 1).equals(";") && Arrays.asList(shortdays).contains(args[0].substring(1))){
            String daycalled = daymap.get(args[0].substring(1));
            User username = event.getMessage().getMentionedUsers().get(0);
            Read read = new Read(username.getName(), daycalled);
            try {
                read.ScheduleRead();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            EmbedBuilder empty = new EmbedBuilder();
            EmbedBuilder embed = Scheduler.classFormat(read.schedules, empty);
            event.getChannel().sendMessage(embed.build()).queue();
            embed.clear();
        }  
    }
}
