package com.android.dialer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.contacts.common.CallUtil;
import com.android.dialer.preference.IPCallPreferenceActivity;
import com.android.i18n.phonenumbers.NumberParseException;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class IPCallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent ipcallIntent = getIntent();
        if (ipcallIntent != null) {
            final String mNumber = ipcallIntent.getStringExtra("number");
            PhoneNumber pNumber;
            String nNumber= mNumber;
            String ipNumber = "";
            String ip_call_prefix = IPCallPreferenceActivity.getIPCallPrefix(this);
            if (TextUtils.isEmpty(ip_call_prefix)) {
                Toast.makeText(this, R.string.dialer_ipcall_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    pNumber = PhoneNumberUtil.getInstance().parse(mNumber, IPCallPreferenceActivity.getCurrentCountryCode(this));
                    nNumber = String.valueOf(pNumber.getNationalNumber());

                    if (nNumber.indexOf(ip_call_prefix) == 0 && ip_call_prefix.length() != 0) {
                         nNumber = nNumber.replaceFirst(ip_call_prefix, "");
                    }
                    ipNumber = ip_call_prefix + nNumber;
                    } catch (NumberParseException e) {
                         e.printStackTrace();
                    }
                Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED);
                intent.setData(Uri.fromParts(CallUtil.SCHEME_TEL, ipNumber, null));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        finish();
    }
}
