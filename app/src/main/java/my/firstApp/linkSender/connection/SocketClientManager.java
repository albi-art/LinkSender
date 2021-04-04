/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender.connection;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import my.firstApp.linkSender.IoC;
import my.firstApp.linkSender.message.Message;
import my.firstApp.linkSender.message.MessageBroker;

public class SocketClientManager implements ClientManager {

    private final MessageBroker messageBroker = new MessageBroker();
    private final ConnectionStatusWatcher connectionStatusWatcher;

    private Client lastClient;
    private ConnectionStatus lastClientStatus = ConnectionStatus.DISCONNECTED;
    private String lastConnectionAddress = "";

    public SocketClientManager(IoC ioC) {
        this.connectionStatusWatcher = new StatusWatcher(ioC.getStatusWatcher());
    }

    public void sendMessage(String receiverIpAddress, String message) {
        if (!isClientConnected(receiverIpAddress)) {
            connect(receiverIpAddress);
        }
        messageBroker.sendMessage(new Message(message));
    }

    public void connect(String receiverIpAddress) {
        int SERVER_PORT = 42000;
        try {
            InetAddress ipAddress = InetAddress.getByName(receiverIpAddress);
            InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, SERVER_PORT);
            if (lastClient != null) {
                lastClient.close();
            }
            lastClient = new Client(socketAddress, connectionStatusWatcher, messageBroker);
            lastClient.start();
            this.lastConnectionAddress = receiverIpAddress;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String getLastConnectionAddress() {
        return lastConnectionAddress;
    }

    public ConnectionStatus getLastClientStatus() {
        return lastClientStatus;
    }

    protected boolean isClientConnected(String receiverIpAddress) {
        return lastConnectionAddress.equals(receiverIpAddress) &&
                lastClientStatus == ConnectionStatus.CONNECTED;
    }

    class StatusWatcher implements ConnectionStatusWatcher {
        private final ConnectionStatusWatcher nextWatcher;

        public StatusWatcher(ConnectionStatusWatcher nextWatcher) {
            this.nextWatcher = nextWatcher;
        }

        @Override
        public void handle(ConnectionStatus connectionStatus) {
            lastClientStatus = connectionStatus;
            nextWatcher.handle(connectionStatus);
        }
    }
}
