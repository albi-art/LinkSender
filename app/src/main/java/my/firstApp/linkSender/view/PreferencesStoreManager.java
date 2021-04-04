package my.firstApp.linkSender.view;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import my.firstApp.linkSender.IoC;

/**
 * The class manage preferences store
 */
public class PreferencesStoreManager {
    private final String LAST_CONNECTION_IP = "lastConnectionIpAddress";

    private final IoC ioC;

    public PreferencesStoreManager(IoC ioC) {
        this.ioC = ioC;
    }

    public void saveReceiverIpAddressToStore(String ipAddress) {
        SharedPreferences.Editor editor = ioC.getPreferences().edit();
        editor.putString(LAST_CONNECTION_IP, ipAddress);
        editor.apply();
    }

    public String readReceiverIpAddressFromStore() {
        return ioC.getPreferences().getString(LAST_CONNECTION_IP, getDefaultIP());
    }

    /**
     * @return IP address of this device
     */
    private String getDefaultIP() {
        WifiManager wifiManager = ioC.getWifiManager();
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }
}
