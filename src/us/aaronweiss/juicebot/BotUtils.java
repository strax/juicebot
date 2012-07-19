/*
 * Copyright (c) 2012 Aaron Weiss <aaronweiss74@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package us.aaronweiss.juicebot;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A set of static utilities to be used by bots.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public class BotUtils {
	/**
	 * Registers the <code>bot</code> with the server it's connected to.
	 * @param bot the bot to register
	 */
	public static void register(IBot bot) {
		String username = bot.getConfiguration().get("BOT_NAME");
		bot.write("NICK :" + username + "\r\n");
		bot.write("USER " + username + " 0 * :" + username + "\r\n");
	}
	
	/**
	 * Directs <code>bot</code> to join a channel.
	 * @param channel the channel to join
	 * @param bot the bot to join
	 */
	public static void join(String channel, IBot bot) {
		bot.write("JOIN " + channel + "\r\n");
	}
	

	/**
	 * Directs <code>bot</code> to part a channel.
	 * @param channel the channel to part from
	 * @param bot the bot to part
	 */
	public static void part(String channel, IBot bot) {
		bot.write("PART " + channel + "\r\n");
	}
	
	/**
	 * Directs <code>bot</code> to part a channel for the specified reason.
	 * @param channel the channel to part from
	 * @param reason the reason for parting
	 * @param bot the bot to part
	 */
	public static void part(String channel, String reason, IBot bot) {
		bot.write("PART " + channel + " :" + reason + "\r\n");
	}
	
	/**
	 * Directs <code>bot</code> to quit with the specified reason.
	 * @param reason the reason for quitting
	 * @param bot the bot to quit
	 */
	public static void quit(String reason, IBot bot) {
		bot.write("QUIT :" + reason + "\r\n");
	}
	
	/**
	 * Directs <code>bot</code> to send a message on a specified channel.
	 * @param message the message to send
	 * @param channel the channel to send to
	 * @param bot the bot to speak
	 */
	public static void say(String message, String channel, IBot bot) {
		bot.write("PRIVMSG " + channel + " :" + message + "\r\n");
	}

	/**
	 * Directs <code>bot</code> to send a message as an action on a specified channel.
	 * @param message the message to send
	 * @param channel the channel to send to
	 * @param bot the bot to speak
	 */
	public static void sayMe(String message, String channel, IBot bot) {
		bot.write("PRIVMSG " + channel + " :\u0001ACTION " + message + "\r\n");
	}
	
	/**
	 * Outputs text to the console for a user to read.
	 * @param message the text to output
	 */
	public static void output(String message) {
		BotUtils.output(message, true);
	}
	
	/**
	 * Outputs text to the console for a user to read.
	 * @param message the text to output
	 * @param newLine whether or not to end the message with a new line carriage
	 */
	public static void output(String message, boolean newLine) {
		System.out.print("[" + BotUtils.now() + "] " + message + ((newLine) ? "\n" : ""));
	}
	
	/**
	 * Gets a timestamp for the current moment in time.
	 * @return a timestamp for this moment in time
	 */
	public static String now() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		return ((hours + (TimeZone.getDefault().useDaylightTime() ? 0 : -1)) + ":" + ((minutes < 10) ? "0" + minutes : "" + minutes) + ":" + ((seconds < 10) ? "0" + seconds : "" + seconds));
	}
	
	/**
	 * Joins an array into a string starting at the specified index.
	 * @param array the array to join
	 * @param start the index to begin joining the array at
	 * @return the joined string from the array
	 */
	public static String joinStringFrom(String[] array, int start) {
		StringBuilder builder = new StringBuilder();
		for (int i = start; i < array.length; i++) {
			builder.append(array[i]);
			if (i != array.length - 1) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}
}
