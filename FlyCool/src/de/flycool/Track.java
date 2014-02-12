package de.flycool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.flycool.FlyingObject.Popup;

/**
 * Stellt ein Höhenprofil da
 * 
 * @author daniel.hardt
 * 
 */
public class Track {

	/**
	 * Stellt einen Eintrag eines Höhenprofils da
	 * 
	 * @author daniel.hardt
	 * 
	 */
	public class TrackEntry {

		Date time;

		double attitudeAboveMsl;
		double elevation;

		Popup popup;

		public TrackEntry(Date time, double attitudeAboveMsl, double elevation,
				Popup popup) {

			this.time = time;
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
