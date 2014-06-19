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
	
	private int PhoneStateInt = -1;						//����״̬
	private static String IncommingNumber = null;	//�������
	private static String IncommingNumberName = null;
	private WindowManager wm; 
	private TextView tv; 
	
	 /**��ϵ����ʾ����**/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/**�绰����**/
	private static final int PHONES_NUMBER_INDEX = 1;
	/**ͷ��ID**/
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/**��ϵ�˵�ID**/
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
		if(intent.hasExtra(PhoneStateString))			//�������״̬
			PhoneStateInt = intent.getIntExtra( PhoneStateString , -1);
		if(intent.hasExtra(PhoneNumberString))			//����������
			IncommingNumber = null;
			IncommingNumber = intent.getStringExtra(PhoneNumberString);
		switch(PhoneStateInt){
		case TelephonyManager.CALL_STATE_RINGING:		//����
			showContact();
			break;
		case TelephonyManager.CALL_STATE_IDLE:  		//�������
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
                if(IncommingNumber.length() < 8){		//�Ƕ̺�
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
			String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);		//��õ绰����
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
