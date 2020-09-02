package mybot;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.javacord.api.*;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.event.message.MessageCreateEvent;

import com.udojava.evalex.*;
public class Main {
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
	static BufferedReader Token = new BufferedReader(new InputStreamReader(load.getResourceAsStream("Token.txt")));
	static BufferedReader rin;
	static BufferedReader badwords = new BufferedReader(new InputStreamReader(load.getResourceAsStream("badwords.txt")));
	static BufferedReader copy = new BufferedReader(new InputStreamReader(load.getResourceAsStream("copypastas.txt")));
	static HashMap<Messageable, Thread> threads = new HashMap<Messageable, Thread>();
	static HashMap<MessageAuthor, String> prefixes = new HashMap<MessageAuthor, String>();
	static StringBuilder allah = new StringBuilder();
	static StringBuilder space = new StringBuilder();
	static String message;
	static Messageable chan;
	static String pre;
	static String servername;
	static String[] pastas = new String[9];
	static MessageCreateEvent event;
	static int third = 0;
	static HashSet<String> bad = new HashSet<String>();
	static HashSet<String> sfw = new HashSet<String>();
	
	public static void main(String[] args) throws IOException {
		space.append((char)8203);
		for(int i = 0; i < 999; i++) space.append("\n");
		space.append((char)8203);//spaces
		for(int i = 0; i < 2000; i++) allah.append(((char)65021));//allah
		for(int i = 0; i < pastas.length; i++)pastas[i] = copy.readLine();
		for(int i = 0; i < 76; i++)bad.add(badwords.readLine());
		sfw.add("SSPS");
		sfw.add("F Y T 2");
		
		DiscordApi api = new DiscordApiBuilder().setToken(Token.readLine()).login().join();
		System.out.println("Logged in!");
		
		String botname = Token.readLine();
		api.addMessageCreateListener(event -> {
			if(!event.getMessageAuthor().getDiscriminatedName().equals(botname)){
				if(third > 0) {
					message = event.getMessageContent();
					chan = event.getChannel();
					pre = prefix(event.getMessageAuthor());
					thirdmanager();
				}
				else {
					String check = event.getMessageContent().substring(0, prefix(event.getMessageAuthor()).length()+1);
					if(check.equals(prefix(event.getMessageAuthor())+"!")) {
						readmanager(event);
					}
					else if(event.getMessageContent().contains("diexit")) {//debug, exits program
						event.getChannel().sendMessage("Going Offline...");
						api.disconnect();
						System.exit(0);
					}
					else if(sfw.contains(event.getServer().get().getName())){
						for(String e:event.getMessageContent().split(" ")) {
							if(bad.contains(e)) event.getChannel().sendMessage("NO SWEARING on my Christian Discord Server!!!");
						}
					}
				}
			}
		});
	}
	static void readmanager(MessageCreateEvent eve){
		String[] cmd = eve.getMessageContent().split(" ");
		chan = eve.getChannel();
		pre = prefix(eve.getMessageAuthor());
		servername = event.getServer().get().getName();
		if(cmd.length>1) message = cmd[1].toLowerCase();
		event = eve;
		switch(cmd[0].charAt(pre.length()+1)){
		case 'p':paste(); break;
		case 'm':threading(); break;
		case 'c':changeprefix(); break;
		case 's':stop(chan); break;
		case 'x':special(); break;
		}
	}
	static String prefix(MessageAuthor us) {
		if(prefixes.containsKey(us)) return prefixes.get(us);
		return "f";
	}
	static void paste(){
		switch(message) {
		case "insult":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			chan.sendMessage(insult()); break;//reddit database insulter
		case "stop" :
			stop(chan); break; //stop treads
		case "allah" :
			chan.sendMessage(allah.toString()); break;
		case "space" :
			chan.sendMessage(space.toString()); break;
		case "plagueis" :
			chan.sendMessage(pastas[0]); break;
		case "sorry" :
			chan.sendMessage(pastas[1]); break;
		case "doctor" :
			chan.sendMessage(pastas[2]); break;
		case "kira" :
			chan.sendMessage(pastas[3]); break;
		case "pandemonika" :
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			chan.sendMessage(pastas[4]); break;
		case "navy":
			chan.sendMessage(pastas[5]); break;
		case "fitness":
			chan.sendMessage(pastas[6]); break;
		case "linux":
			chan.sendMessage(pastas[7]); break;
		case "furry":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			chan.sendMessage(pastas[8]); break;
		case "help":
			chan.sendMessage("https://github.com/rain1598/SpammersHaven"); break;
		case "mathhelp":
			chan.sendMessage("https://github.com/uklimaschewski/EvalEx"); break;
		case "pingme":
			chan.sendMessage(event.getMessageAuthor().asUser().get().getMentionTag()); break;
		case "nsfwtest":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			chan.sendMessage("NSFW");
		}
	}
	static String insult(){//Reddit insulter
		String re = "";
		int n = (int) (Math.random()*21795);
		try {
			rin = new BufferedReader(new InputStreamReader(load.getResourceAsStream("redditmoment.txt")));
			for(int i = 0; i < n; i++) rin.readLine();
			re = rin.readLine();
			if(re.length() > 2000)re = re.substring(0, 1999);
		} catch (IOException e) {}
		return re;
	}
	static void stop(Messageable stchan) {//stops multithreading
		if(threads.containsKey(stchan)){
			threads.get(stchan).interrupt();
			threads.remove(stchan);
		}
	}
	static void threading(){
		stop(chan);
		mt.mode = 0;
		mt.chan = chan;
		switch(message) {
		case "insult":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			mt.mode = 1; break;
		case "allah" :
			mt.spam = allah.toString(); break;
		case "stop" :
			stop(chan); return;
		case "space" :
			mt.spam = space.toString(); break;
		case "plagueis" :
			mt.spam = pastas[0]; break;
		case "sorry" :
			mt.spam = pastas[1]; break;
		case "doctor" :
			mt.spam = pastas[2]; break;
		case "kira" :
			mt.spam = pastas[3]; break;
		case "pandemonika" :
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			mt.spam = pastas[4]; break;
		case "navy":
			mt.spam = pastas[5]; break;
		case "fitness":
			mt.spam = pastas[6]; break;
		case "linux":
			mt.spam = pastas[7]; break;
		case "furry":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			mt.spam = pastas[8]; break;
		case "help":
			mt.spam = "https://github.com/rain1598/SpammersHaven"; break;
		case "mathhelp":
			mt.spam = "https://github.com/uklimaschewski/EvalEx"; break;
		case "pingme" :
			mt.spam = event.getMessageAuthor().asUser().get().getMentionTag(); break;
		case "cum" :
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			mt.mode = 2; break;
		case "nsfwtest":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			mt.spam = "NSFW";
		}
		threads.put(chan, new mt());
		threads.get(chan).start();
	}
	static void changeprefix(){
		if(message.equals("f")) {
			prefixes.remove(event.getMessageAuthor());
			chan.sendMessage("prefix reset");
		}
		else {
			prefixes.put(event.getMessageAuthor(), message);
			chan.sendMessage("prefix changed to: "+message);
		}
	}
	static void thirdmanager() {
		int rd = third;
		third = 0;
		switch(rd) {
		case 1:exp();break;
		}
	}
	static void exp() {
		chan.sendMessage(new Expression(message).eval().toPlainString());
	}
	static void special() {
		switch(message) {
		case "math":
			chan.sendMessage("Math brought to you by EvalEx.\nNote: % is modulo, not percentage");
			third = 1; break;
		}
	}
}
class mt extends Thread{
	public static String spam;
	public static Messageable chan;
	public static int mode;
	public void run() {
		String sp = spam;
		Messageable ch = chan;
		try {
			switch(mode) {
			case 0:
				while(true) {
					ch.sendMessage(sp).join();
					TimeUnit.SECONDS.sleep(1);		
				}
			case 1:
				while(true) {
					ch.sendMessage(Main.insult()).join();
					TimeUnit.SECONDS.sleep(1);		
				}
			case 2:
				BufferedReader cum = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("cum.txt")));
				while(true) {
					String c2 = cum.readLine();
					if(c2 == null)cum = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("cum.txt")));
					ch.sendMessage(c2).join();
					TimeUnit.SECONDS.sleep(1);		
				}
			}
		} catch (InterruptedException e) {
			ch.sendMessage("Spam Stopped");
			return;
		} catch (IOException e) {}
	}
}