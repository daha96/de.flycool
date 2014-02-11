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
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Stellt die Hauptaktivität da, die alle Komponenten zusammenführt. Sie ist der
 * Einstiegspunkt der App
 * 
 * @author daniel
 * 
 */
public class MainActivity extends Activity implements LocationListener,
		FlyingObjectPopupListener {

	SharedPreferences sharedPref;

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

	Date lastGndUpdateTime = new Date(0);

	FlyingObject flyingObject;

	NotificationManager notificationManager;

	Notification climbWarningNotification;
	Notification sinkWarningNotification;
	Notification climbInformationNotification;
	Notification sinkInformationNotification;

	/**
	 * 
	 * @return Die aktuelle Location (wenn nicht vorhanden null)
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Läd die Aktivität und verbindet den LocationListener
	 */
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

		climbWarningNotification = new NotificationCompat.Builder(this)
				.setContentTitle(
						getResources().getString(
								R.string.climbWarningNotificationTitle))
				.setContentText(
						getResources().getString(
								R.string.climbWarningNotificationText))
				.setAutoCancel(false).setSmallIcon(R.drawable.ic_climb_warning)
				.build();

		sinkWarningNotification = new NotificationCompat.Builder(this)
				.setContentTitle(
						getResources().getString(
								R.string.sinkWarningNotificationTitle))
				.setContentText(
						getResources().getString(
								R.string.sinkWarningNotificationText))
				.setAutoCancel(false).setSmallIcon(R.drawable.ic_sink_warning)
				.build();

		climbInformationNotification = new NotificationCompat.Builder(this)
				.setContentTitle(
						getResources().getString(
								R.string.climbInformationNotificationTitle))
				.setContentText(
						getResources().getString(
								R.string.climbInformationNotificationText))
				.setAutoCancel(false)
				.setSmallIcon(R.drawable.ic_climb_information).build();

		sinkInformationNotification = new NotificationCompat.Builder(this)
				.setContentTitle(
						getResources().getString(
								R.string.sinkInformationNotificationTitle))
				.setContentText(
						getResources().getString(
								R.string.sinkInformationNotificationText))
				.setAutoCancel(false)
				.setSmallIcon(R.drawable.ic_sink_information).build();

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

	/**
	 * Erstellt das Menü
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Wird aufgerufen, wenn ein Menüeintrag ausgewählt wurde
	 */
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

	/**
	 * Wird aufgerufen, wenn neue Positionsdaten verfügbar sind und aktualisiert
	 * die komplette App
	 */
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

	/**
	 * Wird aufgerufen, wenn eine neue Benachrichtigung vom FlyingObject
	 * verfügbar ist und aktualisiert die Benachrichtigungen
	 */
	@Override
	public void onFlyingObjectPopupChanged(Popup popup) {

		boolean enabled;
		String message;

		// Alles OK
		if (popup == null) {
			enabled = false;
			message = "";
		} else {
			notificationManager.cancelAll();

			// Warnung
			if (popup.getWarnLevel() == WarnLevel.warn) {
				if (popup.getFlyAction() == FlyAction.climb) {
					notificationManager.notify(0, climbWarningNotification);
					enabled = true;
					message = getResources().getString(
							R.string.climbWarningNotificationTitle);
				} else {
					notificationManager.notify(1, sinkWarningNotification);
					enabled = true;
					message = getResources().getString(
							R.string.sinkWarningNotificationTitle);
				}
			}
			// Info
			else {
				if (popup.getFlyAction() == FlyAction.climb) {
					notificationManager.notify(2, climbInformationNotification);
					enabled = true;
					message = getResources().getString(
							R.string.climbInformationNotificationTitle);
				} else {
					notificationManager.notify(3, sinkInformationNotification);
					enabled = true;
					message = getResources().getString(
							R.string.sinkInformationNotificationTitle);
				}
			}
		}

		if (enabled)
			warnFrame.setVisibility(View.VISIBLE);
		else
			warnFrame.setVisibility(View.GONE);

		warnMessage.setText(message);

	}

	/**
	 * Aktualisiert die Höhe über Grund
	 * 
	 * @param gndElevation
	 */
	void refreshGndAltitude(double gndElevation) {

		flyingObject.setElevation(gndElevation);
		mslAltitudeGndTextView.setText(String.format("%.2f",
				flyingObject.getElevation())
				+ " m");
		altitudeGndTextView.setText(String.format("%.2f",
				flyingObject.getAttitudeAboveGnd())
				+ " m");
	}

	/**
	 * Aktualisiert die Höhe über dem Meeresspiegel
	 * 
	 * @param altitudeMsl
	 */
	void refreshMslAltitude(double altitudeMsl) {
		flyingObject.setAttitudeAboveMsl(altitudeMsl);
		altitudeMslTextView.setText(String.format("%.2f",
				flyingObject.getAttitudeAboveMsl())
				+ " m");
	}

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
