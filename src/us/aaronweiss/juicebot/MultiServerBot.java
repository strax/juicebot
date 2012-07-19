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

import java.net.InetSocketAddress;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;

/**
 * An abstract base for a bot that manages it's own connection to multiple servers.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public abstract class MultiServerBot extends Bot {
	private final ChannelGroup sessions = new DefaultChannelGroup("sessions");
	
	public MultiServerBot(String username, String server, String port) {
		super(username, server, port);
	}
	
	@Override
	public void onConnect(Channel session) {
		this.sessions.add(session);
	}
	
	@Override
	public void connect() {
		String[] servers = config.get("IRC_SERVER").replaceAll(" ", "").split(",");
		String[] ports = config.get("IRC_PORT").replaceAll(" ", "").split(",");
		if (servers.length == ports.length) {
			for (int i = 0; i < servers.length; i++) {
				ChannelFuture cf = this.bootstrap.connect(new InetSocketAddress(servers[i], Integer.parseInt(ports[i])));
				cf.awaitUninterruptibly();
				sessions.add(cf.getChannel());
			}
		} else if (ports.length == 1) {
			for (String server : servers) {
				ChannelFuture cf = this.bootstrap.connect(new InetSocketAddress(server, Integer.parseInt(ports[0])));
				cf.awaitUninterruptibly();
				sessions.add(cf.getChannel());
			}
		} else {
			throw new RuntimeException("Invalid multi-server configuration for IRC_SERVER and IRC_PORT.");
		}
	}
	
	@Override
	public void write(String message) {
		this.sessions.write(message);
	}
	
	/**
	 * Writes a message to the specified server.
	 * @param message the message to write
	 * @param server the server to write to
	 */
	public void write(String message, String server) {
		for (Channel session : sessions) {
			if (session.getRemoteAddress().toString().contains(server)) {
				session.write(message);
			}
		}
	}

	@Override
	public void disconnect() {
		ChannelGroupFuture cgf = this.sessions.disconnect();
		cgf.awaitUninterruptibly();
	}

	@Override
	public boolean isConnected() {
		return !this.sessions.isEmpty();
	}
}
