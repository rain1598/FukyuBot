package mybot;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.javacord.api.*;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.server.Server;
public class Main {
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
	static BufferedReader Token = new BufferedReader(new InputStreamReader(load.getResourceAsStream("Token.txt")));
	static BufferedReader rin = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("redditinsultsnodupe.txt")));
	static BufferedReader copy = new BufferedReader(new InputStreamReader(load.getResourceAsStream("copypastas.txt")));
	
	static HashMap<Messageable, Thread> threads = new HashMap<Messageable, Thread>();
	static HashMap<Server, String> prefixes = new HashMap<Server, String>();
	static StringBuilder haram = new StringBuilder();
	static StringBuilder space = new StringBuilder();
	
	public static void main(String[] args) throws IOException {
		space.append((char)8203);
		for(int i = 0; i < 999; i++) space.append("\n");
		space.append((char)8203);//spaces
		for(int i = 0; i < 2000; i++) haram.append(((char)65021));//allah
		String plagueis = copy.readLine();
		String sorry = copy.readLine();
		String doctor = copy.readLine();
		String kira = copy.readLine();
		String pandemonika = copy.readLine();
		
		DiscordApi api = new DiscordApiBuilder().setToken(Token.readLine()).login().join();
		System.out.println("Logged in!");
		
		api.addMessageCreateListener(event -> {
			Messageable chan = event.getChannel();
			String pre = prefix(event.getServer().get());
			String message = event.getMessage().getContent().toLowerCase();
			if(message.contains(pre)) {
				//boolean candelete = true; //candelete is for hiding commands
				if (message.contains(pre + "!insult")) {//reddit database insulter
					chan.sendMessage(insult());
				}
				else if (message.contains(pre + "!haram")) {//long arabic character spammer
					chan.sendMessage(haram.toString());
				}
				else if (message.contains(pre + "!space")) {//hard ping
					chan.sendMessage(space.toString());
				}
				else if (message.contains(pre + "!stop")) {//stop
					stop(chan);
				}
				else if(message.contains(pre + "!m")) {
					threading(message, chan, pre);
				}
				else if (message.contains(pre + "!die")) {//debug, exits program
					chan.sendMessage("Going Offline...");
					api.disconnect();
					System.exit(0);
				}
				else if (message.contains(pre + "!help")) {//help
					chan.sendMessage("pre + !changeprefix\nprefix + !haram\nprefix + !spaces\nprefix + !die");
				}
				//change pre
				else if (message.length() > pre.length()+14 && message.substring(0, pre.length()+13).equals((pre + "!changeprefix"))) {
					pre = message.split(" ")[1];
					if(pre.equals("f")) {
						prefixes.remove(event.getServer().get());
						chan.sendMessage("prefix reset");
					}
					else {
						prefixes.put(event.getServer().get(), pre);
						chan.sendMessage("prefix changed to: "+pre);
					}
				}
				//else candelete = false;
				//if(candelete)event.deleteMessage();
			}
		});
	}
	static String insult(){//Reddit insulter
		String re = "";
		int n = (int) (Math.random()*21795);
		try {
			for(int i = 0; i < n; i++) rin.readLine();
			re = rin.readLine();
			if(re.length() > 2000)re = re.substring(0, 1999);
		} catch (IOException e) {}
		return re;
	}
	static void stop(Messageable chan) {//stops multithreading
		if(threads.containsKey(chan)){
			threads.get(chan).interrupt();
			threads.remove(chan);
		}
	}
	static void threading(String message, Messageable chan, String pre){
		stop(chan);
		multithread.chan = chan;
		if (message.contains(pre + "!mharam")) {//multithreaded haram
			multithread.spam = haram.toString();
		}
		else if (message.contains(pre + "!minsult")) {//multithreaded insult (not working)
			insultthread.chan = chan;
			threads.put(chan, new insultthread());
			threads.get(chan).start();
			return;
		}
		else if (message.contains(pre + "!mspace")) {//multithreaded space
			multithread.spam = space.toString();
		}
		else if (message.contains(pre + "!mping")) {//multithreaded pinging
			multithread.spam = "@here\n";
		}
		threads.put(chan, new multithread());
		threads.get(chan).start();
	}
	static String prefix(Server serv) {
		if(prefixes.containsKey(serv)) return prefixes.get(serv);
		return "f";
	}
}
class multithread extends Thread{
	public static String spam;
	public static Messageable chan;
	public void run() {
		String sp = spam;
		Messageable ch = chan;
		try {
			while(true) {
				ch.sendMessage(sp).join();
				TimeUnit.SECONDS.sleep(1);		
			}
		} catch (InterruptedException e) {
			ch.sendMessage("Spam Stopped");
			return;
		}
	}
}
class insultthread extends Thread{//prob need java.nio to work
	public static Messageable chan;
	public void run() {
		Messageable ch = chan;
		try {
			while(true) {
				ch.sendMessage(Main.insult()).join();
				TimeUnit.SECONDS.sleep(1);		
			}
		} catch (InterruptedException e) {
			ch.sendMessage("Spam Stopped");
			return;
		}
	}
}