package com.matthewtamlin.testapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.matthewtamlin.sliding_intro_screen_library.core.IntroButton;

/**
 * Tests the ability to set behaviours, and tests the behaviours themselves.
 */
public class TestBehaviours extends ThreePageTestBase {
	/**
	 * Used to identify this class during debugging.
	 */
	private static final String TAG = "[TestBehaviours]";

	/**
	 * The permissions to request from the user.
	 */
	private static final String[] PERMISSIONS =
			{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_NETWORK_STATE};

	/**
	 * Identifies the permissions request when the result is received.
	 */
	private static final int PERM_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		getRootView().addView(layout);

		Button testCustomBehaviour = new Button(this);
		layout.addView(testCustomBehaviour);
		testCustomBehaviour.setText("Test custom behaviour");
		testCustomBehaviour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLeftButtonBehaviour(new IntroButton.BehaviourAdapter() {
					@Override
					public void run() {
						getActivity().goToPage(3);
					}
				});

				setLeftButtonText("Go back to 0 ", null);
				hideLeftButtonOnLastPage(false);
			}
		});

		Button testPermissionBehaviour = new Button(this);
		layout.addView(testPermissionBehaviour);
		testPermissionBehaviour.setText("Test permission behaviour");
		testPermissionBehaviour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setLeftButtonBehaviour(
						new IntroButton.RequestPermissions(PERMISSIONS, PERM_REQUEST_CODE));
				setLeftButtonText("REQUEST PERMS", null);
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
			int[] grantResults) {

		// Assume that grantResults will always have the same length as permissions
		for (int i = 0; i < grantResults.length; i++) {
			Log.d(TAG, "[Permission " + permissions[i] + " was granted: " + (grantResults[i] ==
					PackageManager.PERMISSION_GRANTED) + "]");
		}
	}
}
