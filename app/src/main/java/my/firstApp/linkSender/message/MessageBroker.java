package my.firstApp.linkSender.message;

public class MessageBroker {
    private Message message;

    public synchronized void sendMessage(Message message) {
        this.message = message;
    }

    public synchronized boolean hasMessage() {
        return message != null;
    }

    public synchronized Message getMessage() {
        return message;
    }
}
