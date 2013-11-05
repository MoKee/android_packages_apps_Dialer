/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dialer.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.location.CountryDetector;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.android.dialer.R;

public class IPCallPreferenceActivity extends PreferenceActivity {

    private static final String IPCALL_PREFIX = "ipcall_prefix";
    private Preference mipCallPrefix;
    private static SharedPreferences ePrefs;
    private static TelephonyManager mTelManager;
    private Context context;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.preference_ipcall_options);

        //IPCall add
        context = getBaseContext();
        String preFixStr = getIPCallPrefix(context);
        mipCallPrefix = (Preference) findPreference(IPCALL_PREFIX);
        mipCallPrefix.setSummary(TextUtils.isEmpty(preFixStr) ? context.getString(R.string.dialer_ip_call_prefix_summary) : preFixStr);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
    	if (preference == mipCallPrefix) {
        	LinearLayout layoutIPCallPrefix=new LinearLayout(this);
        	layoutIPCallPrefix.setOrientation(LinearLayout.VERTICAL);
    		final EditText etIpCallPrefix=new EditText(this);
            	etIpCallPrefix.setGravity(Gravity.CENTER_HORIZONTAL);
            	etIpCallPrefix.setText(getIPCallPrefix(context));
            	etIpCallPrefix.setInputType(InputType.TYPE_CLASS_NUMBER);
            	layoutIPCallPrefix.addView(etIpCallPrefix);
            	new AlertDialog.Builder(this).setTitle(R.string.dialer_ip_call_prefix_title).setView(layoutIPCallPrefix).setPositiveButton(android.R.string.ok, new OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String preFixStr = etIpCallPrefix.getText().toString().trim();
					ePrefs.edit().putString(IPCALL_PREFIX, preFixStr).commit();	
					mipCallPrefix.setSummary(TextUtils.isEmpty(preFixStr) ? context.getString(R.string.dialer_ip_call_prefix_summary) : preFixStr);
				}}).setNegativeButton(android.R.string.cancel, null).create().show();
        	return true;
        }
        
        return false;
    }

    public static String getCurrentCountryCode(Context context) {
        final CountryDetector countryDetector =
                (CountryDetector) context.getSystemService(Context.COUNTRY_DETECTOR);
        return countryDetector.detectCountry().getCountryIso();
    }

	public static String getIPCallPrefix(Context context) {
		ePrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mTelManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String ipCallPrefix = ePrefs.getString(IPCALL_PREFIX, "");
		if (ipCallPrefix.length() == 0) {
			String operator = mTelManager.getSimOperator();
			if (operator != null) {
				if (operator.equals("46000") || operator.equals("46002")
						|| operator.equals("46007")) {
					// 中国移动
					ipCallPrefix = "12593";
				} else if (operator.equals("46001") || operator.equals("46006")) {
					// 中国联通
					ipCallPrefix = "10193";
				} else if (operator.equals("46003") || operator.equals("46005")) {
					// 中国电信
					ipCallPrefix = "17909";
				} else
					ipCallPrefix = "";
			}
		}
		return ipCallPrefix;
	} 

}
