package com.example.serialdebugging;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
	
	private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitleTextView = (TextView) findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) findViewById(R.id.demoText);
        mScrollView = (ScrollView) findViewById(R.id.demoScroller);
	}
	
	protected void onTouch() {
		try {
			sendData();
		} catch (IOException e) {
			// 
		}
		
		try {
			receiveData();
		} catch (IOException e) {
			//
		}
		
	}
	
	public void receiveData() throws IOException {
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		UsbSerialDriver receiveDriver = UsbSerialProber.acquire(manager);
		
		if (receiveDriver != null) {
			receiveDriver.open();
			try {
			    receiveDriver.setBaudRate(115200);
			    byte buffer[] = new byte[16];
			    int numBytesRead = receiveDriver.read(buffer, 1000);
			    String message = "Read " + numBytesRead + " bytes: \n"
		                + HexDump.dumpHexString(buffer) + "\n\n";
			    mDumpTextView.append(message);
			    mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
			    
			} catch (IOException e) {
			    // Deal with error.	
			} finally {
			    receiveDriver.close();
			}
		}
	}
		    
	
	 public void sendData() throws IOException {
	    	UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
	    	UsbSerialDriver sendDriver = UsbSerialProber.acquire(manager);
	    	
	    	if(sendDriver != null) {
	    		sendDriver.open();
	    		try{
	    			sendDriver.setBaudRate(9600);
	    			char dataToSend = '1';
	    			byte [] byteToSend = new byte[1]; 
	    			byteToSend[0] = (byte)dataToSend;
	    			sendDriver.write(byteToSend, 1000);
	    			
	    			
	    		} catch (IOException e) {
	    			// bla
	    		} finally {
	    			sendDriver.close();
	    		}
	    	}
	    }


}
