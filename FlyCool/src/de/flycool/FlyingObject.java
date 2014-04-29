package de.flycool;

import android.content.SharedPreferences;

/**
 * Stellt ein fliegendes Objekt mit zwei Bezugssystemen da (MSL und GND)
 * 
 * @author daniel
 * 
 */
public class FlyingObject {

	/**
	 * Aufzählung aller Warnlevel
	 * 
	 * @author daniel
	 * 
	 */
	enum WarnLevel {
		info, warn
	}

	/**
	 * Aufzählung aller Bezugssysteme
	 * 
	 * @author daniel
	 * 
	 */
	enum ReferenceAttitude {
		msl, gnd
	}

	/**
	 * Aufzählung aller Extremwerte
	 * 
	 * @author daniel
	 * 
	 */
	enum MinMax {
		min, max
	}

	/**
	 * Aufzählung aller Flugaktionen
	 * 
	 * @author daniel
	 * 
	 */
	enum FlyAction {
		sink, climb
	}

	/**
	 * Stellt eine Benachrichtigung da (ReadOnly)
	 * 
	 * @author daniel
	 * 
	 */
	class Popup {

		WarnLevel warnLevel;
		FlyAction flyAction;

		public WarnLevel getWarnLevel() {
			return warnLevel;
		}

		public FlyAction getFlyAction() {
			return flyAction;
		}

		public Popup(WarnLevel warnLevel, FlyAction flyAction) {
			this.warnLevel = warnLevel;
			this.flyAction = flyAction;
		}

		@Override
		public boolean equals(Object o) {

			if (o instanceof Popup) {
				Popup popup = (Popup) o;
				if (popup.flyAction.equals(flyAction)
						&& popup.warnLevel.equals(warnLevel))
					return true;
				else
					return false;
			} else
				return false;
		}
	}

	SharedPreferences sharedPref;
	FlyingObjectPopupListener popupListener;

	double attitudeAboveMsl;
	double elevation;

	Popup lastPopup = null;

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

	public FlyingObject(SharedPreferences sharedPref,
			FlyingObjectPopupListener popupListener) {
		this.sharedPref = sharedPref;
		this.popupListener = popupListener;
	}

	/**
	 * Aktualisiert das Objekt wenn die Höhe üner MSL oder die Bodenhöhe
	 * aktualisiert wurden
	 */
	void newData() {
		Popup lastPopup = this.lastPopup;
		this.lastPopup = getAction();

		if ((this.lastPopup == null && lastPopup != null)
				|| (this.lastPopup != null && lastPopup == null)
				|| (this.lastPopup != null && lastPopup != null && !this.lastPopup
						.equals(lastPopup)))
			popupListener.onFlyingObjectPopupChanged(this.lastPopup);
	}

	/**
	 * Findet die aktuelle Aktion, die das Objekt ausführen muss, um nicht zu
	 * kollidieren
	 * 
	 * @return Die aktuelle Aktion (null wenn keine erforderlich)
	 */
	Popup getAction() {
		// GND
		if (getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.min) != null
				&& getAttitudeAboveGnd() < getSetting(WarnLevel.warn,
						ReferenceAttitude.gnd, MinMax.min))
			return new Popup(WarnLevel.warn, FlyAction.climb);

		if (getSetting(WarnLevel.warn, ReferenceAttitude.gnd, MinMax.max) != null
				&& getAttitudeAboveGnd() > getSetting(WarnLevel.warn,
						ReferenceAttitude.gnd, MinMax.max))
			return new Popup(WarnLevel.warn, FlyAction.sink);

		if (getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.min) != null
				&& getAttitudeAboveGnd() < getSetting(WarnLevel.info,
						ReferenceAttitude.gnd, MinMax.min))
			return new Popup(WarnLevel.info, FlyAction.climb);

		if (getSetting(WarnLevel.info, ReferenceAttitude.gnd, MinMax.max) != null
				&& getAttitudeAboveGnd() > getSetting(WarnLevel.info,
						ReferenceAttitude.gnd, MinMax.max))
			return new Popup(WarnLevel.info, FlyAction.sink);

		// MSL
		if (getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.min) != null
				&& getAttitudeAboveMsl() < getSetting(WarnLevel.warn,
						ReferenceAttitude.msl, MinMax.min))
			return new Popup(WarnLevel.warn, FlyAction.climb);

		if (getSetting(WarnLevel.warn, ReferenceAttitude.msl, MinMax.max) != null
				&& getAttitudeAboveMsl() > getSetting(WarnLevel.warn,
						ReferenceAttitude.msl, MinMax.max))
			return new Popup(WarnLevel.warn, FlyAction.sink);

		if (getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.min) != null
				&& getAttitudeAboveMsl() < getSetting(WarnLevel.info,
						ReferenceAttitude.msl, MinMax.min))
			return new Popup(WarnLevel.info, FlyAction.climb);

		if (getSetting(WarnLevel.info, ReferenceAttitude.msl, MinMax.max) != null
				&& getAttitudeAboveMsl() > getSetting(WarnLevel.info,
						ReferenceAttitude.msl, MinMax.max))
			return new Popup(WarnLevel.info, FlyAction.sink);

		return null;

	}

	/**
	 * Ruft die entsprechende Einstellung ab
	 * 
	 * @param warnLevel
	 * @param referenceAttitude
	 * @param minMax
	 * @return Wert der Einstellung
	 */
	Integer getSetting(WarnLevel warnLevel,
			ReferenceAttitude referenceAttitude, MinMax minMax) {
		return SettingsFragment.getSetting(sharedPref, warnLevel,
				referenceAttitude, minMax);
	}

	public Popup getLastPopup() {
		return lastPopup;
	}
}
