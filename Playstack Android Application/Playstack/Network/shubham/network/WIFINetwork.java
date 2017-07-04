package shubham.network;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

import shubham.network.WIFIControl.Client;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WIFINetwork {
	
	
	private WIFIControl wifiApControl;
	private Context context;
	private WIFIStatus status = WIFIStatus.UNKNOWN;
	private Listner listner;
	private WIFI wifi;
	
	
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			final WIFIStatus newStatus = getConnectivityStatus();
			
			if(newStatus != status){
				status = newStatus;
				 
				 
				wifi.status = status;
				wifi.ipList = getClientIpList();
				wifi.ip = getIp();
				listner.onChange(wifi);
			}
		}
	};
	
	
	public WIFINetwork(Context context){
		this.context = context;
		
		wifi = new WIFI();
		wifiApControl = WIFIControl.getInstance(context);
		
	}
	
	
	public void setOnListner(Listner listner){

		final IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");

		context.registerReceiver(receiver, filter);
		this.listner = listner;
		
	}
	
	
	public void updateConnectivity(){
		status = getConnectivityStatus();
	}
	
	public WIFIStatus getConnectivityStatus() {
		final String service = Context.CONNECTIVITY_SERVICE;
		final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(service);
		final NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		
		if(wifiApControl.isEnabled()){
			return WIFIStatus.WIFI_AP_CONNECTED;
		}
		
		if (networkInfo == null) {
			return WIFIStatus.OFFLINE;
		}

		if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return WIFIStatus.WIFI_CONNECTED;
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return WIFIStatus.MOBILE_CONNECTED;
		}
		
		return WIFIStatus.OFFLINE;
	}
	
	
	
	
	public void onDestroy(){
		context.unregisterReceiver(receiver);
	}
	
	public interface Listner{
		public void onChange(WIFI wifi);
	}
	
	public String[] getClientIpList(){
		ArrayList<String> list = new ArrayList<>();
		if(status == WIFIStatus.WIFI_AP_CONNECTED){
			List<Client> clients = wifiApControl.getClients();
			for(Client client : clients){
				list.add(client.ipAddr);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	

	@SuppressLint("DefaultLocale")
	public String getIp(){
		if(status == WIFIStatus.WIFI_AP_CONNECTED){
			Inet4Address address = wifiApControl.getInet4Address();
			
			return address != null ? address.getHostAddress() : null;
		}else if(status == WIFIStatus.WIFI_CONNECTED) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			
		
		    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		    int ip = wifiInfo.getIpAddress();

		    String ipString = String.format("%d.%d.%d.%d",(ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
		    return ipString;
		}
		
		return null;
	}
	
	
	

	
	public class WIFI{
		public WIFIStatus status;
		public String[] ipList;
		public String ip;
	}
}
