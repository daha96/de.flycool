package de.flycool;

import android.content.SharedPreferences;

public class FlyingObject {
	
	SharedPreferences sharedPref;
	FlyingObjectPopupListener popupListener;
	
	Popup lastPopup = null;
	
	public FlyingObject(SharedPreferences sharedPref, FlyingObjectPopupListener popupListener)
	{
		this.sharedPref = sharedPref;
		this.popupListener = popupListener;
	}

	double attitudeAboveMsl;

	public double getAttitudeAboveMsl() {
		return attitudeAboveMsl;
	}

	public double getAttitudeAboveGnd() {
		return attitudeAboveMsl - elevation;
	}

	public void setAttitudeAboveMsl(double attitudeAboveMsl) {
		this.attitudeAboveMsl = attitudeAboveMsl;
		newData();
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
		newData();
	}

	double elevation;

	enum WarnLevel
	{
		info, warn
	}

	enum ReferenceAttitude
	{
		msl, gnd
	}

	enum MinMax
	{
		min, max
	}
	
	void newData()
	{
		Popup lastPopup = this.lastPopup;
		this.lastPopup = getAction();
		
		if (	(this.lastPopup == null && lastPopup != null) ||
				(this.lastPopup != null && lastPopup == null) ||
				(this.lastPopup != null && lastPopup != null && !this.lastPopup.equals(lastPopup)))
			popupListener.onFlyingObjectPopupChanged(this.lastPopup);
	}

	Popup getAction()
	{
		// GND
		if (getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.min) != null && getAttitudeAboveGnd() < getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.min))
			return new Popup(WarnLevel.warn, FlyAction.climb);
		
		if (getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.max) != null && getAttitudeAboveGnd() > getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.max))
			return new Popup(WarnLevel.warn, FlyAction.sink);
		
		if (getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.min) != null && getAttitudeAboveGnd() < getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.min))
			return new Popup(WarnLevel.info, FlyAction.climb);
		
		if (getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.max) != null && getAttitudeAboveGnd() > getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.max))
			return new Popup(WarnLevel.info, FlyAction.sink);
		
		
		// MSL
		if (getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.min) != null && getAttitudeAboveMsl() < getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.min))
			return new Popup(WarnLevel.warn, FlyAction.climb);
		
		if (getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.max) != null && getAttitudeAboveMsl() > getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.max))
			return new Popup(WarnLevel.warn, FlyAction.sink);
		
		if (getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.min) != null && getAttitudeAboveMsl() < getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.min))
			return new Popup(WarnLevel.info, FlyAction.climb);
		
		if (getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.max) != null && getAttitudeAboveMsl() > getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.max))
			return new Popup(WarnLevel.info, FlyAction.sink);
		
		
		return null;
			
	}
	
	class Popup
	{
		public Popup(WarnLevel warnLevel, FlyAction flyAction)
		{
			this.warnLevel = warnLevel;
			this.flyAction = flyAction;
		}
		WarnLevel warnLevel;
		public WarnLevel getWarnLevel() {
			return warnLevel;
		}
	/*	public void setWarnLevel(WarnLevel warnLevel) {
			this.warnLevel = warnLevel;
		}
		*/public FlyAction getFlyAction() {
			return flyAction;
		}
	/*	public void setFlyAction(FlyAction flyAction) {
			this.flyAction = flyAction;
		}
		*/FlyAction flyAction;
	}

	enum FlyAction
	{
		sink, climb
	}
	
/*	boolean isLowWarnMsl()
	{
		if (getSetting(WarnLevel, minMax))
		return 
	}
	*/
	Integer getSetting(WarnLevel warnLevel, ReferenceAttitude referenceAttitude, MinMax minMax)
	{
		String str = "";
	/*	int minAltitudeGnd = Integer.parseInt(sharedPref.getString(
				"pref_key_min_altitude_gnd", "50"));
		minAltitudeGndTextView.setText("> "
				+ String.format("%d", minAltitudeGnd) + " m");
		*/
		if (warnLevel == WarnLevel.info)
		{
			if (referenceAttitude == ReferenceAttitude.msl)
			{
				if (minMax == MinMax.min)
				{
					str = sharedPref.getString("pref_key_informations_msl_min", "");
				}
				else if (minMax == MinMax.max)
				{
					str = sharedPref.getString("pref_key_informations_msl_max", "");
				}
			}
			else if (referenceAttitude == ReferenceAttitude.gnd)
			{
				if (minMax == MinMax.min)
				{
					str = sharedPref.getString("pref_key_informations_gnd_min", "");
				}
				else if (minMax == MinMax.max)
				{
					str = sharedPref.getString("pref_key_informations_gnd_max", "");
				}
			}
		}
		else if (warnLevel == WarnLevel.warn)
		{
			if (referenceAttitude == ReferenceAttitude.msl)
			{
				if (minMax == MinMax.min)
				{
					str = sharedPref.getString("pref_key_warnings_msl_min", "");
				}
				else if (minMax == MinMax.max)
				{
					str = sharedPref.getString("pref_key_warnings_msl_max", "");
				}
			}
			else if (referenceAttitude == ReferenceAttitude.gnd)
			{
				if (minMax == MinMax.min)
				{
					str = sharedPref.getString("pref_key_warnings_gnd_min", "");
				}
				else if (minMax == MinMax.max)
				{
					str = sharedPref.getString("pref_key_warnings_gnd_max", "");
				}
			}
		}
		
		if (str == "")
			return null;
		else
			return Integer.parseInt(str);
	}
}
