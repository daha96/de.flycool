package de.flycool;

import de.flycool.FlyingObject.MinMax;
import de.flycool.FlyingObject.ReferenceAttitude;
import de.flycool.FlyingObject.WarnLevel;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

/**
 * Stellt das Fragment fÃ¼r die Einstellungen da
 * 
 * @author daniel
 * 
 */
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		getPreferenceScreen().findPreference("pref_key_warnings_msl_min")
				.setOnPreferenceChangeListener(onPreferenceChangeListener);

		/*
		 * () {
		 * 
		 * @Override public boolean onPreferenceChange(Preference preference,
		 * Object newValue) { Boolean rtnval = true; if (Your_Test) { final
		 * AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 * builder.setTitle("Invalid Input");
		 * builder.setMessage("Something's gone wrong...");
		 * builder.setPositiveButton(android.R.string.ok, null); builder.show();
		 * rtnval = false; } return rtnval; } });
		 */
	}

	/**
	 * Ruft die entsprechende Einstellung ab
	 * 
	 * @param warnLevel
	 * @param referenceAttitude
	 * @param minMax
	 * @return Wert der Einstellung
	 */
	public static Integer getSetting(SharedPreferences sharedPref,
			WarnLevel warnLevel, ReferenceAttitude referenceAttitude,
			MinMax minMax) {
		String str = "";
		/*
		 * int minAltitudeGnd = Integer.parseInt(sharedPref.getString(
		 * "pref_key_min_altitude_gnd", "50"));
		 * minAltitudeGndTextView.setText("> " + String.format("%d",
		 * minAltitudeGnd) + " m");
		 */
		if (warnLevel == WarnLevel.info) {
			if (referenceAttitude == ReferenceAttitude.msl) {
				if (minMax == MinMax.min) {
					str = sharedPref.getString("pref_key_informations_msl_min",
							"");
				} else if (minMax == MinMax.max) {
					str = sharedPref.getString("pref_key_informations_msl_max",
							"");
				}
			} else if (referenceAttitude == ReferenceAttitude.gnd) {
				if (minMax == MinMax.min) {
					str = sharedPref.getString("pref_key_informations_gnd_min",
							"");
				} else if (minMax == MinMax.max) {
					str = sharedPref.getString("pref_key_informations_gnd_max",
							"");
				}
			}
		} else if (warnLevel == WarnLevel.warn) {
			if (referenceAttitude == ReferenceAttitude.msl) {
				if (minMax == MinMax.min) {
					str = sharedPref.getString("pref_key_warnings_msl_min", "");
				} else if (minMax == MinMax.max) {
					str = sharedPref.getString("pref_key_warnings_msl_max", "");
				}
			} else if (referenceAttitude == ReferenceAttitude.gnd) {
				if (minMax == MinMax.min) {
					str = sharedPref.getString("pref_key_warnings_gnd_min", "");
				} else if (minMax == MinMax.max) {
					str = sharedPref.getString("pref_key_warnings_gnd_max", "");
				}
			}
		}

		if (str == "")
			return null;
		else
			return Integer.parseInt(str);
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
		return getSetting(sharedPref, warnLevel, referenceAttitude, minMax);
	}

	SharedPreferences sharedPref;

	boolean showPreferenceValidationError() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setTitle(R.string.pref_invalidInputTitle);
		builder.setMessage(R.string.pref_invalidInputMessage);
		// TODO: Button Beschriftung!
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
		return false;
	}

	OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			String prefKey = preference.getKey();
			
			// Wert löschen immer erlaubt
			if (newValue == null || newValue == "")
				return true;

			int newVal = Integer.valueOf((String) newValue);

			
			if (prefKey.equals("pref_key_warnings_msl_min")) {
				Integer val = getSetting(WarnLevel.warn, ReferenceAttitude.msl,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.info, ReferenceAttitude.msl,
						MinMax.min);
				if (val != null && newVal > val)
					return showPreferenceValidationError();

			}
			else if (prefKey.equals("pref_key_warnings_msl_max")) {
				Integer val = getSetting(WarnLevel.warn, ReferenceAttitude.msl,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.info, ReferenceAttitude.msl,
						MinMax.max);
				if (val != null && newVal < val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_warnings_gnd_min")) {
				Integer val = getSetting(WarnLevel.warn, ReferenceAttitude.gnd,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.info, ReferenceAttitude.gnd,
						MinMax.min);
				if (val != null && newVal > val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_warnings_gnd_max")) {
				Integer val = getSetting(WarnLevel.warn, ReferenceAttitude.gnd,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.info, ReferenceAttitude.gnd,
						MinMax.max);
				if (val != null && newVal < val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_informations_msl_min")) {
				Integer val = getSetting(WarnLevel.info, ReferenceAttitude.msl,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.warn, ReferenceAttitude.msl,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_informations_msl_max")) {
				Integer val = getSetting(WarnLevel.info, ReferenceAttitude.msl,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.warn, ReferenceAttitude.msl,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_informations_gnd_min")) {
				Integer val = getSetting(WarnLevel.info, ReferenceAttitude.gnd,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.warn, ReferenceAttitude.gnd,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();
			}
			else if (prefKey.equals("pref_key_informations_gnd_max")) {
				Integer val = getSetting(WarnLevel.info, ReferenceAttitude.gnd,
						MinMax.min);
				if (val != null && newVal < val)
					return showPreferenceValidationError();

				val = getSetting(WarnLevel.warn, ReferenceAttitude.gnd,
						MinMax.max);
				if (val != null && newVal > val)
					return showPreferenceValidationError();
			}
			
			return true;
		}
	};
}
