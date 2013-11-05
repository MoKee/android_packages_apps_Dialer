package com.android.dialer.preference;

import java.util.HashSet;
import java.util.Set;

import com.android.dialer.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.widget.Toast;

public class SpeedDialPreferenceActivity extends PreferenceActivity {

	public static final String SPEED_DIAL = "speed_dial";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		SharedPreferences pref = getSharedPreferences(SPEED_DIAL, Context.MODE_PRIVATE);
		Intent intent = this.getIntent();
		String name = intent.getStringExtra("name");
		String number = intent.getStringExtra("number");
		String id = intent.getStringExtra("id");
		if (!TextUtils.isEmpty(name)) {
			Set<String> entry = new HashSet<String>();
			entry.add(name);
			entry.add(number);
			pref.edit().putStringSet(SPEED_DIAL + id, entry).apply();
			onBackPressed();
			Toast.makeText(this, getString(R.string.speeddial_added, name ,number, id), Toast.LENGTH_LONG).show();
		}
	}

}
