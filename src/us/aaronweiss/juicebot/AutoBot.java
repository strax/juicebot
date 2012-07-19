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

/**
 * A base for a bot that auto-rejoins upon being kicked, and attempts to avert poor bans.
 * N.b. You must call super.onMessage() when you override it in order to receive desired functionality.
 * @author Aaron Weiss
 * @version 1.0
 * @since 1.0
 */
public abstract class AutoBot extends Bot {
	public AutoBot(String username, String server, String port) {
		super(username, server, port);
	}

	@Override
	public void onMessage(String[] message) {
		if (message[1].equals("KICK") && message[3].equals(this.config.get("BOT_NAME"))) {
			this.write("JOIN " + message[2] + "\r\n");
		} else if (message[1].equals(ServerResponseCode.ERR_BANNEDFROMCHAN.value)) {
			this.config.put("BOT_NAME", this.config.get("BOT_NAME") + "_");
		} else if (message[1].equals(ServerResponseCode.ERR_CANNOTSENDTOCHAN)) {
			this.write("PART " + message[3] + "\r\n");
			this.config.put("BOT_NAME", this.config.get("BOT_NAME") + "_");
			this.write("JOIN " + message[3] + "\r\n");
		}
	}

}
