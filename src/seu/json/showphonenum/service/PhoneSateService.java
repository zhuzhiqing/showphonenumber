package seu.json.showphonenum.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

public class PhoneSateService extends Service {

	private final String PhoneStateString = "PHONESTATE";
	private final String PhoneNumberString = "PHONENUM";
	
	private int PhoneStateInt = -1;						//来电状态
	private static String IncommingNumber = null;	//来电号码
	private static String IncommingNumberName = null;
	private WindowManager wm; 
	private TextView tv; 
	
	 /**联系人显示名称**/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/**电话号码**/
	private static final int PHONES_NUMBER_INDEX = 1;
	/**头像ID**/
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/**联系人的ID**/
	private static final int PHONES_CONTACT_ID_INDEX = 3;
	
	
	private static final String[] PHONES_PROJECTION = new String[] {
		       Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_ID,Phone.CONTACT_ID };
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent.hasExtra(PhoneStateString))			//获得来电状态
			PhoneStateInt = intent.getIntExtra( PhoneStateString , -1);
		if(intent.hasExtra(PhoneNumberString))			//获得来电号码
			IncommingNumber = null;
			IncommingNumber = intent.getStringExtra(PhoneNumberString);
		switch(PhoneStateInt){
		case TelephonyManager.CALL_STATE_RINGING:		//来电
			showContact();
			break;
		case TelephonyManager.CALL_STATE_IDLE:  		//来电结束
			removeContact();
			break;
        default:                                
            break;
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void showContact(){
        wm = (WindowManager)this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;    
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL 
      		  | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;   
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;    
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; 
                if(IncommingNumber.length() < 8){		//是短号
        	String ShortNumber = IncommingNumber.substring(IncommingNumber.length()-4, IncommingNumber.length());
	        IncommingNumberName = getContactNameFromPhoneNum(ShortNumber);
	        if(IncommingNumberName != null){
		        tv = new TextView(this); 
		        tv.setText(IncommingNumberName);
		        tv.setTextSize(30);
		        wm.addView(tv, params); 
	        }
        }
	}
	
	public void removeContact(){
		if(wm != null ){
			wm.removeView(tv);
		}
	}
	
	public String getContactNameFromPhoneNum(String phoneNum){
		String ContactName = null;
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				PHONES_PROJECTION, 
				null,
				null, 
				null);
		while(cursor.moveToNext()){
			String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);		//获得电话号码
			String ContactBookLast4Number = phoneNumber.substring(
					phoneNumber.length()-4, phoneNumber.length());
			if(ContactBookLast4Number.contentEquals(phoneNum)  ){
				if(ContactName != null)
					ContactName += ";" + cursor.getString(PHONES_DISPLAY_NAME_INDEX);
				else
					ContactName =  cursor.getString(PHONES_DISPLAY_NAME_INDEX);
			}
		}
		return ContactName;
		
	}
	
}
