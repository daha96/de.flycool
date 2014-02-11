package de.flycool;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import de.flycool.MainActivity;
import android.location.Location;
import android.os.AsyncTask;

/**
 * Ruft die Bodenhöhe an einem Bestimmten Punkt auf der Erdoberfläche ab. Die
 * Daten stammen von der Google Elevation API
 * 
 * @author daniel
 * 
 */
public class GetElevationFromGoogleMapsAsyncTask extends
		AsyncTask<MainActivity, Void, Double> {
	MainActivity mainActivity;

	/**
	 * Startet den Task
	 */
	@Override
	protected Double doInBackground(MainActivity... params) {
		mainActivity = params[0];

		Location location = mainActivity.getLocation();

		return getElevationFromGoogleMaps(location.getLatitude(),
				location.getLongitude());
	}

	/**
	 * Ruft die Höhe von der Google Elevation API ab
	 * 
	 * @param latitude
	 * @param longitude
	 * @return Die Höhe in Metern
	 */
	private double getElevationFromGoogleMaps(double latitude, double longitude) {
		double result = Double.NaN;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		String url = "http://maps.googleapis.com/maps/api/elevation/"
				+ "xml?locations=" + String.valueOf(latitude) + ","
				+ String.valueOf(longitude) + "&sensor=true";
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				int r = -1;
				StringBuffer respStr = new StringBuffer();
				while ((r = instream.read()) != -1)
					respStr.append((char) r);
				String tagOpen = "<elevation>";
				String tagClose = "</elevation>";
				if (respStr.indexOf(tagOpen) != -1) {
					int start = respStr.indexOf(tagOpen) + tagOpen.length();
					int end = respStr.indexOf(tagClose);
					String value = respStr.substring(start, end);
					result = Double.parseDouble(value);
				}
				instream.close();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return result;
	}

	/**
	 * Teilt der MainActivity die neue Bodenhöhe mit
	 */
	protected void onPostExecute(Double elev) {
		mainActivity.refreshGndAltitude(elev);
	}
}