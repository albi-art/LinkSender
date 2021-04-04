/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender.connection;

public interface ClientManager {
    void sendMessage(String receiverIpAddress, String message);

    void connect(String receiverIpAddress);

    String getLastConnectionAddress();

    ConnectionStatus getLastClientStatus();
}
