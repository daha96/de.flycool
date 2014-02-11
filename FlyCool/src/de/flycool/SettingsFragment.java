package de.flycool;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Stellt das Fragment für die Einstellungen da
 * 
 * @author daniel
 * 
 */
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}

}
