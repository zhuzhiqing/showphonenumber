package seu.json.showphonenum.service;

import seu.json.showphonenum.broadcastreceiver.PhoneStateReceiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BootCompleteService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		IntentFilter localIntentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
		localIntentFilter.setPriority(2147483647);
		PhoneStateReceiver localMessageReceiver =new PhoneStateReceiver();
		Intent localIntent = registerReceiver(localMessageReceiver, localIntentFilter);
		
		return super.onStartCommand(intent, flags, startId);
	}

}
