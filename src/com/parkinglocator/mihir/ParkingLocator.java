package com.parkinglocator.mihir;
import java.security.Provider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import android.view.View;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


public class ParkingLocator extends Activity implements View.OnClickListener{
	
	GoogleMap gm;
	final int RQS_GooglePlayServices = 1;
	Button set,remove,nav;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		checkGPServices();
		initUI();
		initGM();
	}
	private void initUI() {
		// TODO Auto-generated method stub
		set=(Button) findViewById(R.id.button1);
		nav=(Button) findViewById(R.id.button2);
		remove=(Button) findViewById(R.id.button3);
		set.setOnClickListener(this);
	}
	private void initGM() {
		// TODO Auto-generated method stub
		gm = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		gm.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gm.setMyLocationEnabled(true);
	}
	private void checkGPServices() {
		// TODO Auto-generated method stub
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		  if (resultCode == ConnectionResult.SUCCESS){
		  }else{
		   GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		  }
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.button1:
			LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		    Criteria cri = new Criteria();
		    String bestProvider = locManager.getBestProvider(cri, true);
		    Location loc = locManager.getLastKnownLocation(bestProvider);
		    if(loc!=null){
		    String latMy = String.valueOf(loc.getLatitude());
		    String lngMy = String.valueOf(loc.getLongitude());
		    Toast.makeText(this, " " +latMy+"  "+lngMy, Toast.LENGTH_SHORT).show();
		    }
		    else{
		    	Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
		    }
			break;
		case R.id.button2:
			
			break;
		case R.id.button3:
			
			break;
		}
		
	}
}

