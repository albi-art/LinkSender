package my.firstApp.linkSender;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import my.firstApp.linkSender.connection.ClientManager;
import my.firstApp.linkSender.connection.ConnectionStatusWatcher;
import my.firstApp.linkSender.connection.SocketClientManager;
import my.firstApp.linkSender.view.PreferencesStoreManager;
import my.firstApp.linkSender.view.ViewStatusWatcher;

/**
 * The class of main inverse of control app container
 */
public class IoC {
    private ClientManager clientManager;
    private PreferencesStoreManager storeManager;
    private ConnectionStatusWatcher statusWatcher;
    private TextView connStatus;
    private SharedPreferences preferences;
    private WifiManager wifiManager;

    @SuppressLint("StaticFieldLeak")
    private static IoC INSTANCE;

    public static IoC getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IoC();
        }

        return INSTANCE;
    }

    private IoC() {
    }

    private boolean isInit = false;

    public void init() {
        isInit = true;
    }

    public boolean isInit() {
        return isInit;
    }

    private void initClientManager() {
        clientManager = new SocketClientManager(this);
    }

    private void initStoreManager() {
        storeManager = new PreferencesStoreManager(this);
    }

    private void initStatusWatcher() {
        statusWatcher = new ViewStatusWatcher(this);
    }

    public ClientManager getClientManager() {
        if (clientManager == null) {
            initClientManager();
        }
        return clientManager;
    }

    public PreferencesStoreManager getStoreManager() {
        if (storeManager == null) {
            initStoreManager();
        }
        return storeManager;
    }

    public ConnectionStatusWatcher getStatusWatcher() {
        if (statusWatcher == null) {
            initStatusWatcher();
        }
        return statusWatcher;
    }

    public TextView getConnStatus() {
        return connStatus;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public IoC setConnStatus(TextView connStatus) {
        this.connStatus = connStatus;
        return this;
    }

    public IoC setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
        return this;
    }

    public IoC setWifiMan(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        return this;
    }
}
