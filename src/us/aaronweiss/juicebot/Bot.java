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
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import us.aaronweiss.juicebot.net.BotChannelPipelineFactory;

/**
 * The abstract base for all bots, as it handles network bootstrapping.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public abstract class Bot implements IBot {
	protected final ClientBootstrap bootstrap;
	protected final Configuration config;
	private Channel session;
	
	public Bot(String username, String server, String port) {
		ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new BotChannelPipelineFactory(this));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		config = new Configuration(username, server, port);
		config.put("BOT_PERIODIC", "-1");
	}
	
	@Override
	public void onConnect(Channel session) {
		this.session = session;
	}

	@Override
	public void onDisconnect() {
		this.session = null;
	}

	@Override
	public void periodic() {
		BotUtils.output("BOT_PERIODIC is set without overriding default Bot.periodic()");
	}
	
	@Override
	public void connect() {
		ChannelFuture cf = this.bootstrap.connect(new InetSocketAddress(config.get("IRC_SERVER"), Integer.parseInt(config.get("IRC_PORT"))));
		cf.awaitUninterruptibly();
		this.session = cf.getChannel();
	}
	
	@Override
	public void write(String message) {
		this.session.write(message);
	}
	
	@Override
	public void disconnect() {
		ChannelFuture cf = this.session.disconnect();
		cf.awaitUninterruptibly();
		this.session = null;
	}

	@Override
	public boolean isConnected() {
		return this.session.isConnected();
	}
	
	@Override
	public Configuration getConfiguration() {
		return config;
	}

}
