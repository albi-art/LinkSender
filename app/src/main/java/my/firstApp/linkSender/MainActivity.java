/*
 * Copyright (c) 2021.
 * https://github.com/albi-art/LinkSender
 */

package my.firstApp.linkSender;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import my.firstApp.linkSender.connection.ClientManager;
import my.firstApp.linkSender.connection.ConnectionStatus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final IoC ioC = IoC.getInstance();
    private TextView ipAddressField;

    //Initialize inverse of control container
    private void initIoC() {
        if (ioC.isInit()) {
            return;
        }
        ioC.setPreferences(getPreferences(MODE_PRIVATE))
                .setWifiMan((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE))
                .setConnStatus(findViewById(R.id.connectionStatus))
                .init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIoC();
        handleLastConnection();
        handleIntent(getIntent());
    }

    protected void initView() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnConnect).setOnClickListener(this);
        ipAddressField = findViewById(R.id.receiverIpAddress);
    }

    protected void handleLastConnection() {
        ClientManager clientManager = ioC.getClientManager();
        fillIpAddress(clientManager.getLastConnectionAddress());
        if (clientManager.getLastClientStatus() == ConnectionStatus.DISCONNECTED) {
            connectToReceiver();
        }
    }

    protected void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SEND) || action.equals(Intent.ACTION_VIEW)) {
            handleSharedLink(intent);
            super.onBackPressed();
        }
    }

    /**
     * Handle link shared from other application
     *
     * @param intent Intent
     */
    protected void handleSharedLink(Intent intent) {
        String sharedText = intent.getAction().equals(Intent.ACTION_SEND)
                ? intent.getStringExtra(Intent.EXTRA_TEXT)
                : intent.getDataString();

        String ipAddress = ipAddressField.getText().toString();
        ioC.getClientManager().sendMessage(ipAddress, sharedText);
    }

    @Override
    public void onClick(View v) {
        connectToReceiver();
    }

    private void connectToReceiver() {
        ioC.getStatusWatcher().handle(ConnectionStatus.WAITING);
        ioC.getClientManager().connect(ipAddressField.getText().toString());
    }

    /**
     * Fill receiver device IP address in View input filed
     *
     * @param ipAddress String
     */
    private void fillIpAddress(String ipAddress) {
        ipAddress = ipAddress.equals("")
                ? ioC.getStoreManager().readReceiverIpAddressFromStore()
                : ipAddress;
        EditText btnConfigure = (EditText) ipAddressField;
        btnConfigure.setText(ipAddress);
        //Move cursor to end of line
        btnConfigure.setSelection(btnConfigure.getText().length());
    }
}