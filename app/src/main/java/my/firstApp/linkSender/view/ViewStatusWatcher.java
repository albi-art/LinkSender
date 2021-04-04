package my.firstApp.linkSender.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.TextView;

import my.firstApp.linkSender.IoC;
import my.firstApp.linkSender.connection.ConnectionStatus;
import my.firstApp.linkSender.connection.ConnectionStatusWatcher;

/**
 * The class for handle connection status in View
 */
public class ViewStatusWatcher implements ConnectionStatusWatcher {
    final IoC ioC;
    final TextView connStatus;

    public ViewStatusWatcher(IoC ioC) {
        this.ioC = ioC;
        this.connStatus = ioC.getConnStatus();
    }

    @Override
    public void handle(ConnectionStatus connectionStatus) {
        connStatus.post(() -> renderStatus(connectionStatus));
    }

    @SuppressLint("SetTextI18n")
    private void renderStatus(ConnectionStatus status) {
        switch (status) {
            case CONNECTED:
                connStatus.setTextColor(Color.parseColor("#FF009688"));
                connStatus.setText("connected");
                ioC.getStoreManager().saveReceiverIpAddressToStore(
                        ioC.getClientManager().getLastConnectionAddress());
                break;
            case DISCONNECTED:
                connStatus.setTextColor(Color.RED);
                connStatus.setText("disconnected");
                break;
            case WAITING:
                connStatus.setTextColor(Color.GRAY);
                connStatus.setText("...waiting...");
        }
    }
}