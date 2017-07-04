package shubham.network;

public enum WIFIStatus {
	
	UNKNOWN("unknown"),
	WIFI_CONNECTED("connected to WiFi"),
	WIFI_CONNECTED_HAS_INTERNET("connected to WiFi (Internet available)"),
	WIFI_CONNECTED_HAS_NO_INTERNET("connected to WiFi (Internet not available)"),
	MOBILE_CONNECTED("connected to mobile network"),
	WIFI_AP_CONNECTED("wifi hostpost on"),
	OFFLINE("offline");
	
	
	public final String description;
	
	WIFIStatus(final String description) {
	    this.description = description;
	    
	}
}
