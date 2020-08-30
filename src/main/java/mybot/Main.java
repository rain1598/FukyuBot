package mybot;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.javacord.api.*;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
public class Main {
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
	static BufferedReader Token = new BufferedReader(new InputStreamReader(load.getResourceAsStream("Token.txt")));
	static BufferedReader rin = new BufferedReader(new InputStreamReader(load.getResourceAsStream("redditinsultsnodupe.txt")));
	static BufferedReader copy = new BufferedReader(new InputStreamReader(load.getResourceAsStream("copypastas.txt")));
	static HashMap<Messageable, Thread> threads = new HashMap<Messageable, Thread>();
	static HashMap<Server, String> prefixes = new HashMap<Server, String>();
	static StringBuilder haram = new StringBuilder();
	static StringBuilder space = new StringBuilder();
	static String message;
	static Messageable chan; 
	static String pre;
	static String plagueis;
	static String sorry;
	static String doctor;
	static String kira;
	static String pandemonika;
	static MessageCreateEvent event;
	
	public static void main(String[] args) throws IOException {
		space.append((char)8203);
		for(int i = 0; i < 999; i++) space.append("\n");
		space.append((char)8203);//spaces
		for(int i = 0; i < 2000; i++) haram.append(((char)65021));//allah
		plagueis = copy.readLine();
		sorry = copy.readLine();
		doctor = copy.readLine();
		kira = copy.readLine();
		pandemonika = copy.readLine();
		
		DiscordApi api = new DiscordApiBuilder().setToken(Token.readLine()).login().join();
		System.out.println("Logged in!");
		
		api.addMessageCreateListener(event -> {
			String check = event.getMessageContent().substring(0, prefix(event.getServer().get()).length()+1);
			if(check.equals(prefix(event.getServer().get())+"!")) {
				readmanager(event);
			}
			else if(event.getMessageContent().contains("diexit")) {//debug, exits program
				event.getChannel().sendMessage("Going Offline...");
				api.disconnect();
				System.exit(0);
			}
		});
	}
	static void readmanager(MessageCreateEvent eve){
		String[] cmd = eve.getMessageContent().split(" ");
		chan = eve.getChannel();
		pre = prefix(eve.getServer().get());
		message = cmd[1];
		event = eve;
		switch(cmd[0].charAt(pre.length()+1)){
		case 'p':paste();break;
		case 'm':threading();break;
		case 'c':changeprefix();break;
		}
	}
	static void paste(){
		switch(message) {
		case "insult":
			chan.sendMessage(insult()); break;//reddit database insulter
		case "stop" :
			stop(chan); break; //stop treads
		case "haram" :
			chan.sendMessage(haram.toString()); break;
		case "space" :
			chan.sendMessage(space.toString()); break;
		case "plagueis" :
			chan.sendMessage(plagueis.toString()); break;
		case "sorry" :
			chan.sendMessage(sorry.toString()); break;
		case "doctor" :
			chan.sendMessage(doctor.toString()); break;
		case "kira" :
			chan.sendMessage(kira.toString()); break;
		case "pandemonika" :
			chan.sendMessage(pandemonika.toString()); break;
		case "help" :
			break;
		}
	}
	static void changeprefix(){
		if(message.equals("f")) {
			prefixes.remove(event.getServer().get());
			chan.sendMessage("prefix reset");
		}
		else {
			prefixes.put(event.getServer().get(), message);
			chan.sendMessage("prefix changed to: "+message);
		}
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
	static void threading(){
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