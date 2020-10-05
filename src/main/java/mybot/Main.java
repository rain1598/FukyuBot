package mybot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.event.ListenerManager;
import org.mariuszgromada.math.mxparser.Expression;
public class Main {
	static BufferedReader reader;
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
	static HashMap<Messageable, Thread> threads = new HashMap<>();
	static HashMap<String, Integer> dict = new HashMap<>();
	static HashMap<Messageable, Integer> third = new HashMap<>();
	static String message;
	static Messageable chan;
	static String servername;
	static MessageCreateEvent event;
	static String botname = "";
	static DiscordApi api;
	static User target;
	static ListenerManager<MessageCreateListener> dml;

	static ArrayList<String> pastas = new ArrayList<>();
	static HashSet<String> bad = new HashSet<>();
	static HashSet<String> sfw = new HashSet<>();
	
	public static void main(String[] args) throws IOException {
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("copypastas.txt"))));
		for(String s = reader.readLine(); s != null; s = reader.readLine())pastas.add(s);
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("badwords.txt"))));
		for(String s = reader.readLine(); s != null; s = reader.readLine())bad.add(s);
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("sfw.txt"))));
		for(String s = reader.readLine(); s != null; s = reader.readLine())sfw.add(s);
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("wlist.txt"))));
		for(int i = 0; i < 117528; i++)dict.put(reader.readLine(), i);
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("Token.txt"))));
		
		api = new DiscordApiBuilder().setToken(reader.readLine()).login().join();
		System.out.println("Logged in!");
		botname = reader.readLine();
		reader.close();
		listener();
		
	}
	static void listener(){
		api.addMessageCreateListener(eve -> {
			event = eve;
			message = event.getMessageContent().toLowerCase();
			chan = event.getChannel();
			servername = event.getServer().isPresent() ? event.getServer().get().getName():null;
			if(!event.getMessageAuthor().isYourself()) {
				if (message.contains("diexit")) {//debug, exits program
					chan.sendMessage("Going Offline...");
					System.exit(0);
				}
				if (!third.containsKey(chan)) third.put(chan, 0);
				if (third.get(chan) > 0) {
					message = event.getMessageContent();
					thirdmanager();
				} else if(message.length() > 2){
					String check = message.substring(0, 3);
					if (check.equals("sh!")) {
						readmanager();
					} else if (sfw.contains(servername)) {
						for (String e : message.split(" ")) {
							if (bad.contains(e)) chan.sendMessage(bible());
						}
					}
				} else if (message.equals("ok") || message.equals("k")) {
					event.getMessage().delete();
				}
			}
		});
	}
	static void readmanager(){
		String[] cmd = message.split(" ");
		if(cmd.length>1) message = cmd[1].toLowerCase();
		switch(cmd[0].charAt(3)){
		case 'p':paste('p'); break;
		case 'm':paste('m'); break;
		case 's':stop(chan); break;
		case 'x':special(); break;
		}
	}
	static void special() {
		switch(message) {
			case "math":
				new MessageBuilder().setEmbed(new EmbedBuilder()
						.setTitle("Math brought to you by mXparser, from MathParser.org")
						.setDescription("Read the documentation here")
						.setUrl("https://github.com/mariuszgromada/MathParser.org-mXparser#built-in-tokens")
				).send(chan);
				third.put(chan, 1); break;
			case "dict":
				new MessageBuilder().setEmbed(new EmbedBuilder()
						.setTitle("Dictionary Brought to you by OPTED")
						.setDescription("Please don't use obvious plurals\n(Cacti still works)")
				).send(chan);
				third.put(chan, 2); break;
			case "dm":
				new MessageBuilder().setEmbed(new EmbedBuilder()
						.setTitle("DM anyone you want")
						.setDescription("Use at your own risk")
				).send(chan);
				third.put(chan, 3); break;
		}
	}
	static void thirdmanager() {
		if(message.equals("taskend")|message.equals("sh!s")) {
			if(third.get(chan) == 4){
				dml.remove();
				target = null;
			}
			chan.sendMessage("Task Ended");
			third.put(chan, 0);
			return;
		}
		switch(third.get(event.getChannel())) {
			case 1:exp();break;
			case 2:dictionary();break;
			case 3:dmlist();break;
			case 4:target.sendMessage(message);break;
		}
	}
	static void stop(Messageable stchan) {//stops multithreading
		if(threads.containsKey(stchan)){
			threads.get(stchan).interrupt();
			threads.remove(stchan);
		}
	}
	static void paste(char pm){
		String pasted = "";
		switch(message) {
		case "insult":
			if(sfw.contains(servername)) {
				chan.sendMessage(bible());
				return;
			}
			if(pm == 'm') {
				mt.chan = chan;
				mt.mode = 1;
				break;
			}
			pasted = insult(); break;
		case "ssat":
			if(pm == 'm') {
				mt.chan = chan;
				mt.mode = 5;
				break;
			}
			pasted = ssat(); break;
		case "allah" :
			StringBuilder allah = new StringBuilder();
			for(int i = 0; i < 2000; i++) allah.append(((char)65021));
			pasted = allah.toString(); break;
		case "stop" :
			stop(chan); return;
		case "space" :
			StringBuilder space = new StringBuilder();
			space.append((char)8203);
			for(int i = 0; i < 999; i++) space.append("\n");
			space.append((char)8203);
			pasted = space.toString();
			break;
		case "plagueis" :
			pasted = pastas.get(0); break;
		case "sorry" :
			pasted = pastas.get(1); break;
		case "doctor" :
			pasted = pastas.get(2); break;
		case "kira" :
			pasted = pastas.get(3); break;
		case "pandemonika" :
			if(sfw.contains(servername)) {
				chan.sendMessage(bible());
				return;
			}
			pasted = pastas.get(4); break;
		case "navy":
			pasted = pastas.get(5); break;
		case "fitness":
			pasted = pastas.get(6); break;
		case "linux":
			pasted = pastas.get(7); break;
		case "furry":
			if(sfw.contains(servername)) {
				chan.sendMessage(bible());
				return;
			}
			pasted = pastas.get(8); break;
		case "freeman":
			pasted = pastas.get(9); break;
		case "pingme" :
			pasted = event.getMessageAuthor().asUser().isPresent() ? event.getMessageAuthor().asUser().get().getMentionTag():null; break;
		case "cum" :
			if(sfw.contains(servername)) {
				chan.sendMessage(bible());
				return;
			}
			if(pm == 'm') {
				mt.chan = chan;
				mt.mode = 2;
				break;
			}
			return;
		case "bruh":
			pasted = "bruh"; break;
		case "help":
			pasted = "https://github.com/rain1598/SpammersHaven"; break;
		case "mathhelp":
			pasted = "https://github.com/mariuszgromada/MathParser.org-mXparser#built-in-tokens"; break;
		case "invitebot":
			new MessageBuilder().setEmbed(new EmbedBuilder().setTitle("Bot Invite Link")
.setUrl("https://discord.com/api/oauth2/authorize?client_id=747632462191919204&permissions=3263489&redirect_uri=https%3A%2F%2Fdiscord.com%2Fapi%2Foauth2%2Fauthorize&scope=bot"))
			.send(chan); return;
		case "invite":
			pasted = event.getServerTextChannel().isPresent() ? new InviteBuilder(event.getServerTextChannel().get())
			.setMaxAgeInSeconds(0)
		    .setMaxUses(0)
		    .create().join().getUrl().toString():null; break;
		case "nsfwtest":
			if(sfw.contains(servername)) {
				chan.sendMessage(bible());
				return;
			}
			chan.sendMessage("NSFW");
		case "bee":
			if(pm == 'm') {
				mt.chan = chan;
				mt.mode = 3;
				break;
			}
		case "bible":
			if(pm == 'm') {
				mt.chan = chan;
				mt.mode = 4;
				break;
			}
			pasted = bible(); break;
		}
		if(pasted != null){
			if(pm == 'm') {
				mt.chan = chan;
				mt.spam = pasted;
				threads.put(chan, new mt());
				threads.get(chan).start();
			}
			else chan.sendMessage(pasted);
		}
	}
	static String insult(){//Reddit insulter
		String re = "";
		int n = (int) (Math.random()*21795);
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("redditmoment.txt"))));
			for(int i = 0; i < n; i++) reader.readLine();
			re = reader.readLine();
			reader.close();
			if(re.length() > 2000)re = re.substring(0, 1999);
		} catch (IOException ignored) {}
		return re;
	}
	static String bible(){//bible verse generator
		String re = "";
		int n =(((int)(Math.random()*31102))*2);
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("bible.txt"))));
			for(int i = 0; i < n; i++) reader.readLine();
			re = reader.readLine()+"\n"+reader.readLine();
			reader.close();
		} catch (IOException ignored) {}
		return re;
	}
	static void dictionary() {
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("deflist.txt"))));
		try {
			int line = dict.get(message);
			StringBuilder output = new StringBuilder();
			for(int i = 0; i < line; i++)reader.readLine();
			for(String e:reader.readLine().split("#")){
				output.append(e).append("\n");
			}
			new MessageBuilder().setEmbed(new EmbedBuilder().setTitle(message).setDescription(output.toString())).send(chan);
			reader.close();
		}catch(Exception NullPointerException) {chan.sendMessage("Word not found");}
	}
	static void exp() {
		try {
			chan.sendMessage(Double.toString(new Expression(message).calculate()));
		}catch(Exception e) {chan.sendMessage("Syntax invalid");}
	}
	static String ssat() {
		String re = "";
		int n = (int) (Math.random()*2001);
		try {
			reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("SSAT.txt"))));
			for(int i = 0; i < n*2; i++) reader.readLine();
			String word = reader.readLine();
			String def = reader.readLine();
			re = word+"\n"+def;
			reader.close();
		} catch (IOException ignored) {}
		return re;
	}
	static void dmlist(){
		String me = message;
		for(Server e:api.getServers()){
			target = e.getMemberByDiscriminatedName(me).isPresent() ? e.getMemberByDiscriminatedName(me).get():null;
			if(target != null)break;
		}
		if(target == null) {
			chan.sendMessage("User Not Found");
		}
		else{
			chan.sendMessage("User Identified");
			third.put(chan, 4);
			dml = target.addMessageCreateListener(dm ->
				new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(dm
						.getMessageContent()).setAuthor(dm.getMessageAuthor())).send(chan));
		}
	}
}
class mt extends Thread{
	public static String spam;
	public static Messageable chan;
	public static int mode;
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
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
				BufferedReader cum = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("cum.txt"))));
				while(true) {
					String c2 = cum.readLine();
					if(c2 == null)cum = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("cum.txt"))));
					ch.sendMessage(c2).join();
					TimeUnit.SECONDS.sleep(1);
				}
			case 3:
				BufferedReader bee = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("bee.txt"))));
				while(true) {
					String c2 = bee.readLine();
					if(c2 == null)return;
					ch.sendMessage(c2).join();
					TimeUnit.SECONDS.sleep(1);
				}
			case 4:
				while(true) {
					ch.sendMessage(Main.bible()).join();
					TimeUnit.SECONDS.sleep(1);
				}
			case 5:
				while(true) {
					ch.sendMessage(Main.ssat()).join();
					TimeUnit.SECONDS.sleep(1);
				}
			}
		} catch (Exception ignored) {}
		chan.sendMessage("Spam Stopped");
	}
}