package de.blau.android;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import de.blau.android.exception.OsmException;
import de.blau.android.osm.BoundingBox;

/**
 * Start vespucci with OSM remote control url
 */
public class RemoteControlUrlActivity extends Activity {
	
	private static final String DEBUG_TAG = "RemoteControlUrlAct...";
	public static final String RCDATA = "de.blau.android.RemoteControlActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Uri data = getIntent().getData(); 
		Log.d(DEBUG_TAG,data.toString());
	    Intent intent = new Intent(this, Main.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    String command = data.getPath();
	    if (command.startsWith("/")) {
	    	command = command.substring(1);
	    }
	    Log.d(DEBUG_TAG,command);
	    if (command.equals("load_and_zoom") || command.equals("zoom")) {
	    	try {
				Double left = Double.valueOf(data.getQueryParameter("left"));
				Double right = Double.valueOf(data.getQueryParameter("right"));
				Double bottom = Double.valueOf(data.getQueryParameter("bottom"));
				Double top = Double.valueOf(data.getQueryParameter("top"));
				
				RemoteControlUrlData rcData = new RemoteControlUrlData();
				rcData.setBox(new BoundingBox(left, bottom, right, top));
				rcData.setLoad(command.equals("load_and_zoom"));
				
				Log.d(DEBUG_TAG,"bbox " + rcData.getBox() + " load " + rcData.load());
				String select = data.getQueryParameter("select");
				if (rcData.load() && select != null) {
					rcData.setSelect(select);
				}
				intent.putExtra(RCDATA, rcData);

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				Log.d(DEBUG_TAG,"NumberFormatException ", e);
				e.printStackTrace();
			} catch (OsmException e) {
				Log.d(DEBUG_TAG,"OsmException ", e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    startActivity(intent);
	    finish();
	}
	
	public static class RemoteControlUrlData implements Serializable {
		private static final long serialVersionUID = 1L;
		private boolean load = false;
		private BoundingBox box;
		private String select = null;
		/**
		 * @return the box
		 */
		public BoundingBox getBox() {
			return box;
		}
		/**
		 * return string with elements to select
		 * @return
		 */
		public String getSelect() {
			return select;
		}
		public void setSelect(String select) {
			this.select = select;
		}
		/**
		 * @param box the box to set
		 */
		public void setBox(BoundingBox box) {
			this.box = box;
		}
		/**
		 * @return the load
		 */
		public boolean load() {
			return load;
		}
		/**
		 * @param load the load to set
		 */
		public void setLoad(boolean load) {
			this.load = load;
		}
	}
}
