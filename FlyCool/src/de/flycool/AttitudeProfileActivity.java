package de.flycool;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import de.flycool.Track.TrackEntry;

import android.app.Activity;
import android.graphics.Color;

import android.os.Bundle;
import android.widget.LinearLayout;

public class AttitudeProfileActivity extends Activity {

	Track track;

	/**
	 * Läd die Aktivität
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.attitude_profile);

		track = (Track) getIntent().getExtras().getSerializable("Track");

		OpenChart();
	}

	private void OpenChart() {

		// Create XY Series for X Series.
		XYSeries groundSeries = new XYSeries(getResources().getString(
				R.string.attitudeProfileChart_gndSeries));
		XYSeries attitudeSeries = new XYSeries(getResources().getString(
				R.string.attitudeProfileChart_attitudeProfileSeries));

		long startTime = track.getStartTime().getTime();

		for (TrackEntry entry : track.getTrackEntries()) {
			groundSeries.add((entry.getTime().getTime() - startTime) / 1000,
					entry.getElevation());
			attitudeSeries.add((entry.getTime().getTime() - startTime) / 1000,
					entry.getAttitudeAboveMsl());
		}

		// Create a Dataset to hold the XSeries.

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		// Add X series to the Dataset.
		dataset.addSeries(groundSeries);
		dataset.addSeries(attitudeSeries);

		// Create XYSeriesRenderer to customize XSeries

		XYSeriesRenderer attitudeProfileRenderer = new XYSeriesRenderer();
		attitudeProfileRenderer.setColor(Color.BLUE);
		// attitudeProfileRenderer.setPointStyle(PointStyle.DIAMOND);
		attitudeProfileRenderer.setDisplayChartValues(true);
		attitudeProfileRenderer.setLineWidth(2);
		// attitudeProfileRenderer.setFillPoints(true);

		XYSeriesRenderer gndRenderer = new XYSeriesRenderer();
		gndRenderer.setColor(Color.RED);
		// gndRenderer.setPointStyle(PointStyle.DIAMOND);
		gndRenderer.setDisplayChartValues(true);
		gndRenderer.setLineWidth(2);
		// gndRenderer.setFillPoints(true);

		// Create XYMultipleSeriesRenderer to customize the whole chart

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setChartTitle(getResources().getString(
				R.string.attitudeProfileChart_Title));
		mRenderer.setXTitle(getResources().getString(
				R.string.attitudeProfileChart_XTitle));
		mRenderer.setYTitle(getResources().getString(
				R.string.attitudeProfileChart_YTitle));
		// mRenderer.setZoomButtonsVisible(true);
		// mRenderer.setXLabels(0);
		// mRenderer.setPanEnabled(false);

		mRenderer.setShowGrid(true);

		// Adding the XSeriesRenderer to the MultipleRenderer.
		mRenderer.addSeriesRenderer(attitudeProfileRenderer);
		mRenderer.addSeriesRenderer(gndRenderer);

		// Creating an intent to plot line chart using dataset and
		// multipleRenderer

		GraphicalView chart = (GraphicalView) ChartFactory.getLineChartView(
				getBaseContext(), dataset, mRenderer);

		// Add the graphical view mChart object into the Linear layout .
		((LinearLayout) findViewById(R.id.chartLayout)).addView(chart);

	}
}
