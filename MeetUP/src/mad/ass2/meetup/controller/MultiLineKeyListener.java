/*
 * NAME: SIA WAI SUAN
 * STUDENT ID: S3308555
 * MOBILE APPLICATION DEVELOPMENT
 * ASSIGNMENT 2
 * FILE: MultiLineKeyListener.java
 */
package mad.ass2.meetup.controller;

import android.view.KeyEvent;
import android.view.View;

/*
 * Listener responsible for disabling the ENTER KEY (button).
 */
public class MultiLineKeyListener implements View.OnKeyListener{

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return (keyCode == KeyEvent.KEYCODE_ENTER);
	}

}
