package com.matthewtamlin.sliding_intro_screen_example_app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NumberFragment extends Fragment {
	private TextView textView;

	private int number = 0;

	public NumberFragment() {
		// Required empty public constructor
	}

	public void setNumber(final int number) {
		this.number = number;

		if (textView != null) {
			textView.setText(Integer.toString(number));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View root = inflater.inflate(R.layout.number_fragment, container, false);

		textView = (TextView) root.findViewById(R.id.number_fragment_text_holder);
		textView.setText(null);
		Log.d("test", "" + number);
		textView.setText(Integer.toString(number));

		return root;
	}
}
