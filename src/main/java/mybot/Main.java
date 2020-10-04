package mybot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.mariuszgromada.math.mxparser.Expression;

public class Main {
	static BufferedReader reader;
	static ClassLoader load = Thread.currentThread().getContextClassLoader();
	static BufferedReader SAT = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("SSAT.txt"))));
	static HashMap<Messageable, Thread> threads = new HashMap<>();
	static HashMap<String, Integer> dict = new HashMap<>();
	static HashMap<Messageable, Integer> third = new HashMap<>();
	static String message;
	static Messageable chan;
	static String servername;
	static String[] pastas = new String[10];
	static MessageCreateEvent event;
	static HashSet<String> bad = new HashSet<>();
	static HashSet<String> sfw = new HashSet<>();
	static String botname = "";
	static DiscordApi api;

	public static void main(String[] args) throws IOException {
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("copypastas.txt"))));
		for(int i = 0; i < pastas.length; i++)pastas[i] = reader.readLine();
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("badwords.txt"))));
		for(int i = 0; i < 74; i++)bad.add(reader.readLine());
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("sfw.txt"))));
		for(int i = 0; i < 2; i++)sfw.add(reader.readLine());
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
			if(event.getServer().isPresent()){servername = event.getServer().get().getName();}
			else servername = null;
			if(!event.getMessageAuthor().getDiscriminatedName().equals(botname)) {
				if (!third.containsKey(chan)) {
					third.put(chan, 0);
				}
				if (third.get(chan) > 0) {
					message = event.getMessageContent();
					thirdmanager();
				} else {
					if (message.length() < 3 && (message.equals("ok") || message.equals("k"))) {
						event.getMessage().delete();
					}
					String check = message.substring(0, 3);
					if (check.equals("sh!")) {
						readmanager();
					} else if (message.contains("diexit")) {//debug, exits program
						chan.sendMessage("Going Offline...");
						System.exit(0);
					} else if (sfw.contains(servername)) {
						for (String e : message.split(" ")) {
							if (bad.contains(e)) chan.sendMessage(bible());
						}
					}
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
	static void paste(char pm){
		String pasted = "";
		switch(message) {
		case "insult":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
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
				mt.mode = 1;
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
			space = null;
			break;
		case "plagueis" :
			pasted = pastas[0]; break;
		case "sorry" :
			pasted = pastas[1]; break;
		case "doctor" :
			pasted = pastas[2]; break;
		case "kira" :
			pasted = pastas[3]; break;
		case "pandemonika" :
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			pasted = pastas[4]; break;
		case "navy":
			pasted = pastas[5]; break;
		case "fitness":
			pasted = pastas[6]; break;
		case "linux":
			pasted = pastas[7]; break;
		case "furry":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
				return;
			}
			pasted = pastas[8]; break;
		case "freeman":
			pasted = pastas[10]; break;
		case "pingme" :
			pasted = event.getMessageAuthor().asUser().get().getMentionTag(); break;
		case "cum" :
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
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
			pasted = new InviteBuilder(event.getServerTextChannel().get())
			.setMaxAgeInSeconds(0)
		    .setMaxUses(0)
		    .create().join().getUrl().toString(); break;
		case "nsfwtest":
			if(sfw.contains(servername)) {
				chan.sendMessage("This is a SFW Discord server, what you requested could be NSFW");
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
		if(pm == 'm') {
			mt.chan = chan;
			mt.spam = pasted;
			threads.put(chan, new mt());
			threads.get(chan).start();
		}
		else chan.sendMessage(pasted);
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
	static void stop(Messageable stchan) {//stops multithreading
		if(threads.containsKey(stchan)){
			threads.get(stchan).interrupt();
			threads.remove(stchan);
		}
	}
	static void thirdmanager() {
		if(message.equals("diexit")){
			chan.sendMessage("Going Offline...");
			api.disconnect();
			System.exit(0);
		}
		if(message.equals("taskend")|message.equals("sh!s")) {
			chan.sendMessage("Task Ended");
			third.put(chan, 0);
			return;
		}
		switch(third.get(event.getChannel())) {
		case 1:exp();break;
		case 2:dictionary();break;
		}
	}
	static void dictionary() {
		reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(load.getResourceAsStream("deflist.txt"))));
		try {
			int line = dict.get(message);
			for(int i = 0; i < line; i++)reader.readLine();
			for(String e:reader.readLine().split("#")){
				chan.sendMessage(e);
			}
			reader.close();
		}catch(Exception NullPointerException) {chan.sendMessage("Word not found");}
	}
	static void exp() {
		try {
			chan.sendMessage(Double.toString(new Expression(message).calculate()));
		}catch(Exception e) {chan.sendMessage("Syntax invalid");}
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
		}
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
			}
		} catch (InterruptedException e) {
			ch.sendMessage("Spam Stopped");
		} catch (IOException ignored) {}
	}
}