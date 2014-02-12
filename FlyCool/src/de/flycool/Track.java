package de.flycool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;

import de.flycool.FlyingObject.Popup;

/**
 * Stellt ein Höhenprofil da
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
	 * Stellt einen Eintrag eines Höhenprofils da
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

		Location location;
		double attitudeAboveMsl;
		double elevation;

		Popup popup;

		public TrackEntry(Date time, Location location, double attitudeAboveMsl, double elevation,
				Popup popup) {

			this.time = time;
			this.location = location;
			this.attitudeAboveMsl = attitudeAboveMsl;
			this.elevation = elevation;
			this.popup = popup;
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

		public Popup getPopup() {
			return popup;
		}

		public void setPopup(Popup popup) {
			this.popup = popup;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
	}

	List<TrackEntry> trackEntries = new ArrayList<TrackEntry>();
	Date startTime = new Date();

	/*
	 * public Track() { }
	 */

	/**
	 * Fügt dem Höhenprofil einen Eintrag hinzu
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
