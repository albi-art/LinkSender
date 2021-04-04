package my.firstApp.linkSender.mesage;

import org.junit.Test;

import my.firstApp.linkSender.message.Message;
import my.firstApp.linkSender.message.MessageBroker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageBrokerTest {
    @Test
    public void hasMessage_isCorrect() {
        MessageBroker messageBroker = new MessageBroker();
        assertFalse(messageBroker.hasMessage());
        Message message = new Message("test message");
        messageBroker.sendMessage(message);
        assertTrue(messageBroker.hasMessage());
    }

    @Test
    public void getMessage_isCorrect() {
        String testMessage = "test message";
        Message message = new Message(testMessage);
        MessageBroker messageBroker = new MessageBroker();
        messageBroker.sendMessage(message);
        assertEquals(message, messageBroker.getMessage());
        assertEquals(testMessage, messageBroker.getMessage().read());
    }
}
