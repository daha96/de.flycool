package de.flycool;

import java.util.Date;

import de.flycool.FlyingObject.FlyAction;
import de.flycool.FlyingObject.Popup;
import de.flycool.FlyingObject.WarnLevel;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener, FlyingObjectPopupListener {

	SharedPreferences sharedPref;
	
	Date lastGndUpdateTime = new Date(0);

	TextView latitudeTextView;
	TextView longitudeTextView;

	TextView mslAltitudeGndTextView;
	TextView minAltitudeGndTextView;
	TextView altitudeGndTextView;

	TextView maxAltitudeMslTextView;
	TextView altitudeMslTextView;

	FrameLayout warnFrame;
	TextView warnMessage;

	protected LocationManager locationManager;
	protected LocationListener locationListener;

	Location location = null;

	public Location getLocation() {
		return location;
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.activity_main);

		latitudeTextView = (TextView) findViewById(R.id.latitudeValue);
		longitudeTextView = (TextView) findViewById(R.id.longitudeValue);

		mslAltitudeGndTextView = (TextView) findViewById(R.id.mslAltitudeGndValue);
		minAltitudeGndTextView = (TextView) findViewById(R.id.minAltitudeGndValue);
		altitudeGndTextView = (TextView) findViewById(R.id.altitudeGndValue);

		maxAltitudeMslTextView = (TextView) findViewById(R.id.maxAltitudeMslValue);
		altitudeMslTextView = (TextView) findViewById(R.id.altitudeMslValue);

		warnFrame = (FrameLayout) findViewById(R.id.warnFrame);
		warnFrame.setBackgroundColor(Color.RED);
		warnMessage = (TextView) findViewById(R.id.warnMessage);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Aufforderung, das GPS einzuschalten
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(R.string.no_gps_title);
			builder.setMessage(R.string.no_gps_message);

			builder.setPositiveButton(R.string.no_gps_button,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});

			builder.show();
		}

		flyingObject = new FlyingObject(sharedPref, this);
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		this.location = location;

		latitudeTextView.setText(String.format("%f", location.getLatitude()));
		longitudeTextView.setText(String.format("%f", location.getLongitude()));

		if ((new Date().getTime() - lastGndUpdateTime.getTime()) >= Integer
				.parseInt(sharedPref.getString(
						"pref_key_elevation_update_timespan", "0"))) {
			GetElevationFromGoogleMapsAsyncTask getElevationFromGoogleMapsAsyncTask = new GetElevationFromGoogleMapsAsyncTask();
			getElevationFromGoogleMapsAsyncTask.execute(this);
		}

		refreshMslAltitude(location.getAltitude());
	}

	@Override
	public void onFlyingObjectPopupChanged(Popup popup) {
		
		boolean enabled;// = false;
		String message;//; = "";
		// Alles OK
		if (popup == null)
		{
			enabled = false;
			message = "";
		}
		else
		{
			// Warnung
			if (popup.getWarnLevel() == WarnLevel.warn)
			{
				if (popup.getFlyAction() == FlyAction.climb)
				{
					enabled = true;
					message = getResources().getString(R.string.climb);					
				}
				else
				{
					enabled = true;
					message = getResources().getString(R.string.sink);
				}				
			}
			// Info
			else
			{
				if (popup.getFlyAction() == FlyAction.climb)
				{
					enabled = true;
					message = getResources().getString(R.string.climb);
				}
				else
				{
					enabled = true;
					message = getResources().getString(R.string.sink);
				}
			}
		}
		
		

		if (enabled)
			warnFrame.setVisibility(View.VISIBLE);
		else
			warnFrame.setVisibility(View.GONE);

		warnMessage.setText(message);
		
		
	}

	void refreshGndAltitude(double gndElevation) {
		
		flyingObject.setElevation(gndElevation);/*
	//	refreshPopup();
		int minAltitudeGnd = Integer.parseInt(sharedPref.getString(
				"pref_key_min_altitude_gnd", "50"));
		minAltitudeGndTextView.setText("> "
				+ String.format("%d", minAltitudeGnd) + " m");
		*/
		mslAltitudeGndTextView.setText(String.format("%.2f", flyingObject.getElevation()) + " m");
		altitudeGndTextView.setText(String.format("%.2f", flyingObject.getAttitudeAboveGnd()) + " m");
		/*
		if ((location.getAltitude() - gndElevation) < minAltitudeGnd)
			setTooLow(true);
		else
			setTooLow(false);*/
	}

	void refreshMslAltitude(double altitudeMsl) {
		flyingObject.setAttitudeAboveMsl(altitudeMsl);/*
	//	refreshPopup();
		int maxAltitudeMsl = Integer.parseInt(sharedPref.getString(
				"pref_key_max_altitude_msl", "2950"));
		maxAltitudeMslTextView.setText("< "
				+ String.format("%d", maxAltitudeMsl) + " m");
		*/
		altitudeMslTextView.setText(String.format("%.2f", flyingObject.getAttitudeAboveMsl()) + " m");
/*
		if (altitudeMsl > maxAltitudeMsl)
			setTooHigh(true);
		else
			setTooHigh(false);*/
	}
	
	FlyingObject flyingObject;

/*	void refreshPopup() {
		
		
		
		
	/*	boolean enabled = false;
		String message = "";

		if (tooHigh) {
			enabled = true;
			message = getResources().getString(R.string.sink);
		}

		if (tooLow) {
			enabled = true;
			message = getResources().getString(R.string.climb);
		}

		if (enabled)
			warnFrame.setVisibility(View.VISIBLE);
		else
			warnFrame.setVisibility(View.GONE);

		warnMessage.setText(message);*
	}*/
/*
	boolean tooLow = false;

	void setTooLow(boolean value) {
		tooLow = value;
		refreshPopup();
	}

	boolean tooHigh = false;

	void setTooHigh(boolean value) {
		tooHigh = value;
		refreshPopup();
	}
*/
	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}
}
