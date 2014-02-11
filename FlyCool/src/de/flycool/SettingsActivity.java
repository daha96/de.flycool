package de.flycool;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Stellt die Aktivität für die Einstellungen da
 * 
 * @author daniel
 * 
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}
}
