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

import org.jboss.netty.channel.Channel;

/**
 * The basic interface describing an IRC bot.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public interface IBot {
	/**
	 * Performs the desired actions on connect.
	 * @param session the newly connected to channel
	 */
	public void onConnect(Channel session);
	
	/**
	 * Performs the desired actions on message received.
	 * @param message the message received, split by spaces.
	 */
	public void onMessage(String[] message);
	
	/**
	 * Performs the desired actions on disconnect.
	 */
	public void onDisconnect();
	
	/**
	 * Performs the desired actions periodically, according to <code>BOT_PERIODIC</code>.
	 */
	public void periodic();
	
	/**
	 * Initiates the bot, making it connect to the desired server.
	 */
	public void connect();
	
	/**
	 * Writes a message to the server.
	 * @param message the message to write
	 */
	public void write(String message);
	
	/**
	 * Disconnects the bot from the server.
	 */
	public void disconnect();
	
	/**
	 * Gets whether or not the bot is currently connected.
	 * @return whether or not the bot is connected
	 */
	public boolean isConnected();
	
	/**
	 * Gets the configuration map for the bot.
	 * @return the bot's configuration map
	 */
	public Configuration getConfiguration();
}
