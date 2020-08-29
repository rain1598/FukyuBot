package mybot;
import java.io.*;
import java.util.*;
import org.javacord.api.*;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.server.Server;
public class Main {
	static BufferedReader Token = new BufferedReader(new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("Token.txt"))));
	static BufferedReader read = new BufferedReader(new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("redditinsultsnodupe.txt"))));
	
	static HashMap<Channel, Thread> threads = new HashMap<Channel, Thread>();
	static HashMap<Server, String> prefixes = new HashMap<Server, String>();
	static StringBuilder al = new StringBuilder();
	static StringBuilder sp = new StringBuilder();
	public static void main(String[] args) throws IOException {
		sp.append((char)8203);
		for(int i = 0; i < 999; i++) sp.append("\n");
		sp.append((char)8203);//spaces
		
		for(int i = 0; i < 2000; i++) al.append(((char)65021));//allah
		
		DiscordApi api = new DiscordApiBuilder().setToken(Token.readLine()).login().join();
		System.out.println("Logged in!");
		
		api.addMessageCreateListener(event -> {
			String pre = prefix(event.getServer().get());
			String message = event.getMessage().getContent().toLowerCase();
			if(message.contains(pre)) {
				//boolean candelete = true; //candelete is for hiding commands
				if (message.contains(pre + "!insult")) {//reddit database insulter
					try {
						insult(event.getChannel());
					} catch (IOException e) {
						event.getChannel().sendMessage("This is an IOException you dumb fuck");
					}
				}
				else if (message.contains(pre + "!haram")) {//long arabic character spammer
					event.getChannel().sendMessage(al.toString());
				}
				else if (message.contains(pre + "!space")) {//hard ping
					event.getChannel().sendMessage(sp.toString());
				}
				else if (message.contains(pre + "!stop")) {//stop 
					
				}
				else if (message.contains(pre + "!runharam")) {//multithreaded haram
					
				}
				else if (message.contains(pre + "!runinsult")) {//multithreaded insult
					
				}
				else if (message.contains(pre + "!runspace")) {//multithreaded space
					
				}
				else if (message.contains(pre + "!runping")) {//multithreaded pinging
					
				}
				else if (message.contains(pre + "!die")) {//debug, exits program
					event.getChannel().sendMessage("Going Offline...");
					api.disconnect();
					System.exit(0);
				}
				else if (message.contains(pre + "!help")) {//help
					event.getChannel().sendMessage("pre + !changeprefix\nprefix + !haram\nprefix + !spaces\nprefix + !die");
				}
				//change pre
				else if (message.length() > pre.length()+14 && message.substring(0, pre.length()+13).equals((pre + "!changeprefix"))) {
					pre = message.split(" ")[1];
					prefixes.put(event.getServer().get(), pre);
					event.getChannel().sendMessage("pre changed to: "+pre);
				}
				//else candelete = false;
				//if(candelete)event.deleteMessage();
			}
		});
	}
	static void insult(Channel chan) throws IOException{//Reddit insulter
		int n = (int) (Math.random()*21795);
		for(int i = 0; i < n; i++) read.readLine();
		String re = read.readLine();
		if(re.length() > 2000)re = re.substring(0, 1999);
		((Messageable) chan).sendMessage(re);
	}
	static void stop(Channel chan) {
		if(threads.containsKey(chan)) {
			threads.get(chan).interrupt();
		}
	}
	static String prefix(Server serv) {
		if(prefixes.containsKey(serv)) return prefixes.get(serv);
		return "f";
	}
}
