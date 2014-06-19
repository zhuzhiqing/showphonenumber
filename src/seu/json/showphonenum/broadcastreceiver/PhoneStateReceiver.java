package seu.json.showphonenum.broadcastreceiver;
import seu.json.showphonenum.service.BootCompleteService;
import seu.json.showphonenum.service.PhoneSateService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {
	
	private static final String TAG = "PhoneStatReceiver";
	private static String IncommingNumber = null;
	private Intent intent;
	private final String PhoneStateString = "PHONESTATE";
	private final String PhoneNumberString = "PHONENUM";

  @Override
  public void onReceive(Context context, Intent intent) {
          //如果是拨打电话
		 if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
             Intent service=new Intent(context, BootCompleteService.class);
             context.startService(service);
		 }else if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){      
         }else{                        
                  //如果是来电
                  TelephonyManager tm = 
                      (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);                        
                  
                  switch (tm.getCallState()) {
                  case TelephonyManager.CALL_STATE_RINGING:
                      IncommingNumber = intent.getStringExtra("incoming_number");
                      intent = new Intent(context,PhoneSateService.class);
                      intent.putExtra(PhoneStateString, TelephonyManager.CALL_STATE_RINGING);
                      intent.putExtra(PhoneNumberString, IncommingNumber);
                      context.startService(intent);
                      break;
                  case TelephonyManager.CALL_STATE_OFFHOOK:                                
                      break;
                  case TelephonyManager.CALL_STATE_IDLE:   
                	  intent = new Intent(context,PhoneSateService.class);
                	  intent.putExtra(PhoneStateString, TelephonyManager.CALL_STATE_IDLE);
                	  context.startService(intent);
                      break;
                  } 
          }
  }
}
