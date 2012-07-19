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

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import us.aaronweiss.juicebot.IBot;

/**
 * A <code>ChannelPipelineFactory</code> for <code>BotChannelHandlers</code>.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public class BotChannelPipelineFactory implements ChannelPipelineFactory {
	private final IBot bot;
	
	/**
	 * Creates a <code>BotChannelPipelineFactory</code>.
	 * @param bot the bot to wrap the handlers around
	 */
	public BotChannelPipelineFactory(IBot bot) {
		this.bot = bot;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline cp =  Channels.pipeline(new BotChannelHandler(bot));
		cp.addFirst("customDecoder", new StringDecoder());
		cp.addLast("customEncoder", new StringEncoder());
		return cp;
	}
}
