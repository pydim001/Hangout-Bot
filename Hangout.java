import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Hangout{
    public static JDABuilder jda;
    public static String prefix = ";";
    public static void main(String[] args) throws LoginException, FileNotFoundException {
        File abspath = new File("");
        File tokenfile = new File(abspath.getAbsolutePath() + "/TOKEN.txt");
        Scanner scanner = new Scanner(tokenfile);
        String token = scanner.nextLine();
        jda = JDABuilder.createDefault(token);
        scanner.close();
        jda.setStatus(OnlineStatus.ONLINE);
        jda.setActivity(Activity.watching("Commands"));
        jda.addEventListeners(new Commands());
        jda.build();
    }
}
