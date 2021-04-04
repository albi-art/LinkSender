/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender.mesage;

import org.junit.Test;

import my.firstApp.linkSender.message.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageTest {
    @Test
    public void viewMessage_isCorrect() {
        Message message = new Message("test message");
        assertFalse(message.isViewed());
        message.read();
        assertTrue(message.isViewed());
    }

    @Test
    public void readMessage_isCorrect() {
        String testMessage = "test message";
        Message message = new Message(testMessage);
        assertEquals(testMessage, message.read());
    }
}
