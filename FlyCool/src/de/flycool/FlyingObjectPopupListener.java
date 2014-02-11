package de.flycool;

import de.flycool.FlyingObject.Popup;

/**
 * Stellt eine Schnittstelle für den Erhalt von Benachrichtigungen eines
 * FlyingObject da
 * 
 * @author daniel
 * 
 */
public interface FlyingObjectPopupListener {
	/**
	 * Erhält die Benachrichtigungen eines FlyingObject
	 * 
	 * @param popup
	 */
	public void onFlyingObjectPopupChanged(Popup popup);
}
