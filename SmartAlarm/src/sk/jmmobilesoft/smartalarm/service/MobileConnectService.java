package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MobileConnectService extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		Logger.serviceInfo("MobileConnectService: onCreate");
		NetworkService nService = new NetworkService();
		if(nService.isConnected(getApplicationContext())){
			Logger.serviceInfo("MobileConnectService: network connected stopping service");
			stopSelf();
		}
		else{
			boolean permission = false;  //TODO properties
			if(permission){
				nService.turnMobileOn(getApplicationContext());
				Logger.serviceInfo("MobileConnectService: mobile data set on");
			}
			else{
				Logger.serviceInfo("MobileConnectService: permission denied");
			}
		}
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.serviceInfo("MobileConnectService: onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Logger.serviceInfo("MobileConnectService: onDestroy");
		super.onDestroy();
	}

}
