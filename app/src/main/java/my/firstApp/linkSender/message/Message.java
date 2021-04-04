/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender.message;

public class Message {
    private final String text;
    private boolean viewed = false;

    public Message(String text) {
        this.text = text;
    }

    public String read() {
        viewed = true;
        return text;
    }

    public boolean isViewed() {
        return viewed;
    }
}
