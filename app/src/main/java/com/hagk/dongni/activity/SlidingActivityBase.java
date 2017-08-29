package com.hagk.dongni.activity;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.hagk.dongni.lib.SlidingMenu;

public interface SlidingActivityBase {
	
	public void setBehindContentView(View view, LayoutParams layoutParams);

	public void setBehindContentView(View view);

	public void setBehindContentView(int layoutResID);

	public SlidingMenu getSlidingMenu();
		
	/**
	 * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
	 */
	public void toggle();
	
	/**
	 * Close the SlidingMenu and show the content view.
	 */
	public void showContent();
	
	/**
	 * Open the SlidingMenu and show the menu view.
	 */
	public void showMenu();

	/**
	 * Open the SlidingMenu and show the secondary (right) menu view. Will default to the regular menu
	 * if there is only one.
	 */
	public void showSecondaryMenu();
	
	/**
	 * Controls whether the ActionBar slides along with the above view when the menu is opened,
	 * or if it stays in place.
	 *
	 * @param slidingActionBarEnabled True if you want the ActionBar to slide along with the SlidingMenu,
	 * false if you want the ActionBar to stay in place
	 */
	public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled);
	
}
