package de.flycool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;

import de.flycool.FlyingObject.FlyAction;
import de.flycool.FlyingObject.Popup;
import de.flycool.FlyingObject.WarnLevel;

/**
 * Stellt ein H�henprofil da
 * 
 * @author daniel.hardt
 * 
 */
public class Track implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2738996226468088361L;

	/**
	 * Stellt einen Eintrag eines H�henprofils da
	 * 
	 * @author daniel.hardt
	 * 
	 */
	public class TrackEntry implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1700235839784324479L;

		Date time;

		double latitude;
		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		double longitude;
		double attitudeAboveMsl;
		double elevation;

		WarnLevel warnLevel;
		public WarnLevel getWarnLevel() {
			return warnLevel;
		}

		public void setWarnLevel(WarnLevel warnLevel) {
			this.warnLevel = warnLevel;
		}

		public FlyAction getFlyAction() {
			return flyAction;
		}

		public void setFlyAction(FlyAction flyAction) {
			this.flyAction = flyAction;
		}

		FlyAction flyAction;

		public TrackEntry(Date time, Location location, double attitudeAboveMsl, double elevation,
				Popup popup) {

			this.time = time;
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();			
			this.attitudeAboveMsl = attitudeAboveMsl;
			this.elevation = elevation;
			this.warnLevel = popup.warnLevel;
			this.flyAction = popup.flyAction;
		}

		public Date getTime() {
			return time;
		}

		public void setTime(Date time) {
			this.time = time;
		}

		public double getAttitudeAboveMsl() {
			return attitudeAboveMsl;
		}

		public void setAttitudeAboveMsl(double attitudeAboveMsl) {
			this.attitudeAboveMsl = attitudeAboveMsl;
		}

		public double getElevation() {
			return elevation;
		}

		public void setElevation(double elevation) {
			this.elevation = elevation;
		}


	/*	public Location getLocation() {
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
		}
*/
		public void setLocation(Location location) {
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
		}
	}

	List<TrackEntry> trackEntries = new ArrayList<TrackEntry>();
	Date startTime = new Date();

	/*
	 * public Track() { }
	 */

	/**
	 * F�gt dem H�henprofil einen Eintrag hinzu
	 * 
	 * @param trackEntry
	 */
	public void addTrackEntry(TrackEntry trackEntry) {
		trackEntries.add(trackEntry);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setTrackEntries(List<TrackEntry> trackEntries) {
		this.trackEntries = trackEntries;
	}

	public List<TrackEntry> getTrackEntries() {
		return trackEntries;
	}
}
