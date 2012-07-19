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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A basic bootstrap for starting a bot.
 * 
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public class BotBootstrap {
	private ScheduledThreadPoolExecutor stpe;
	private IBot bot;

	/**
	 * Creates a bootstrapper for a <code>IBot</code>.
	 * @param bot the bot to bootstrap
	 */
	public BotBootstrap(IBot bot) {
		this.bot = bot;
		this.stpe = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
	}

	/**
	 * Runs the bootstrapped bot.
	 */
	public void run() {
		bot.connect();
		if (Integer.parseInt(bot.getConfiguration().get("BOT_PERIODIC")) > 0) {
			Runnable command = new Runnable() {
									public void run() {
										bot.periodic();
									}
								};
			stpe.scheduleWithFixedDelay(command, 0, Integer.parseInt(bot.getConfiguration().get("BOT_PERIODIC")), TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * Shuts down the boostrapped bot.
	 */
	public void shutdown() {
		stpe.shutdown();
		bot.disconnect();
	}
}
