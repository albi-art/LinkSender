/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import my.firstApp.linkSender.message.Message;
import my.firstApp.linkSender.message.MessageBroker;

public class Client extends Thread {

    private final InetAddress serverIPAddress;
    private final int serverPortNumber;
    private final ConnectionStatusWatcher connectionStatusWatcher;

    private final MessageBroker messageBroker;

    private boolean active = true;

    public Client(InetSocketAddress inetSocketAddress,
                  ConnectionStatusWatcher connectionStatusWatcher,
                  MessageBroker messageBroker) {
        serverIPAddress = inetSocketAddress.getAddress();
        serverPortNumber = inetSocketAddress.getPort();
        this.messageBroker = messageBroker;
        this.connectionStatusWatcher = connectionStatusWatcher;
    }

    public void close() {
        active = false;
    }

    @Override
    public void run() {
        super.run();
        try (Socket clientSocket = new Socket(serverIPAddress.getHostAddress(), serverPortNumber);
             InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()
        ) {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));

            connectionStatusWatcher.handle(ConnectionStatus.CONNECTED);
            Thread statusWatcher = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        handleResponse(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            statusWatcher.start();
            handleMessage(in, out);

        } catch (IOException exception) {
            connectionStatusWatcher.handle(ConnectionStatus.DISCONNECTED);
            exception.printStackTrace();
        }
    }

    private void handleMessage(BufferedReader in, BufferedWriter out) throws IOException {
        while (active) {
            if (!messageBroker.hasMessage()) continue;

            Message message = messageBroker.getMessage();
            if (message.isViewed()) continue;

            String url = message.read();
            System.out.print(url);
            out.write(url + "\n");
            out.flush();
            String serverWord = in.readLine();
            System.out.println(serverWord);
        }
    }

    private void handleResponse(BufferedReader in) throws IOException {
        while (active) {
            if (in.read() != -1) break;
            connectionStatusWatcher.handle(ConnectionStatus.DISCONNECTED);
            close();
        }
    }
}
