package mybot;
import java.io.*;
import java.util.*;
import org.javacord.api.*;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.event.message.MessageCreateEvent;
public class Main {
	static String prefix = "f";
	static HashMap<Channel ,Thread> threadmap = new HashMap<Channel ,Thread>();
	static StringBuilder al = new StringBuilder();
	static StringBuilder sp = new StringBuilder();
	public static void main(String[] args) throws IOException {
		sp.append("@here");
		for(int i = 0; i < 997; i++) sp.append("\n");
		sp.append((char)8203);//spaces
		
		for(int i = 0; i < 2000; i++) al.append(((char)65021));//allah
		
		DiscordApi api = new DiscordApiBuilder().setToken("NzQ3NjMyNDYyMTkxOTE5MjA0.X0Rs_Q.ZzLyScJLk-bMlwpfXZDnSaaVWtY")
		.login()
		.join();
		System.out.println("Logged in!");
		
		api.addMessageCreateListener(event -> {
			String message = event.getMessage().getContent().toLowerCase();
			if (message.contains(prefix + "!insult")) {
				try {
					insult(event.getChannel());
				} catch (IOException e) {
					event.getChannel().sendMessage("This is an IOException you dumb fuck");
				}
			}
			if (message.contains(prefix + "!haram")) {
				event.getChannel().sendMessage(al.toString());
			}
			if (message.contains(prefix + "!space")) {
				event.getChannel().sendMessage(sp.toString());
			}
			if (message.contains(prefix + "!die")) {
				event.getChannel().sendMessage("Going Offline...");
				api.disconnect();
			}
			if (message.contains(prefix + "!stop")) {
				
			}
			if (message.contains(prefix + "!runallah")) {
				
			}
			if (message.contains(prefix + "!runinsult")) {
				
			}
			if (message.contains(prefix + "!runspace")) {
				
			}
			if (message.contains(prefix + "!runping")) {
				
			}
		});
	}
	static void insult(Channel event) throws IOException{//Reddit insulter
		BufferedReader read = new BufferedReader(new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("redditinsultsnodupe.txt"))));
		int n = (int) (Math.random()*21795);
		for(int i = 0; i < n; i++) read.readLine();
		String re = read.readLine();
		if(re.length() > 2000)re = re.substring(0, 1999);
		((Messageable) event).sendMessage(re);
	}
	static void runspace(MessageCreateEvent event) {}
	static void stop(MessageCreateEvent event) {
		
	}
}
