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
package us.aaronweiss.juicebot.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import us.aaronweiss.juicebot.BotUtils;
import us.aaronweiss.juicebot.IBot;

/**
 * A <code>SimpleChannelHandler</code> for handling bot networking.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public class BotChannelHandler extends SimpleChannelHandler {
	protected final IBot bot;
	private boolean hasReceivedMessage = false;
	
	/**
	 * Creates a <code>BotChannelHandler</code> for bot networking.
	 * @param bot the bot to bind to the handler
	 */
	public BotChannelHandler(IBot bot) {
		this.bot = bot;
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// Something.
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		String[] splitted = ((String) e.getMessage()).split("\r\n");
		for (String command : splitted) {
			BotUtils.output(command);
			String[] sub = command.split(" ");
			if (sub[0].equals("PING")) {
				String resp = "PONG";
				for (int i = 1; i < sub.length; i++) {
					resp += " " + sub[i];
				}
				e.getChannel().write(resp + "\r\n");
			} else if (sub[1].equals("PING")) {
				String resp = "PONG";
				for (int i = 2; i < sub.length; i++) {
					resp += " " + sub[i];
				}
				e.getChannel().write(resp + "\r\n");
			} else {
				bot.onMessage(sub);
			}
		}
		if (!hasReceivedMessage) {
			this.bot.onConnect(e.getChannel());
			this.hasReceivedMessage = true;
		}
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		this.bot.onDisconnect();
	}
}
