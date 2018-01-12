/*
 * Copyright (C) 2015-2018 The MoKee Open Source Project
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

package com.android.dialer.app.calllog;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.dialer.app.R;
import com.mokee.cloud.location.LocationUtils;

public class MiniMarkActivity extends Activity implements AdapterView.OnItemClickListener {

    private boolean mUseStrictPhoneNumberComparation;
    private TextView tvMarkNumber;
    private EditText btnMarkNumberEdit;
    private ListView lvMarkList;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_mark);
        lvMarkList = (ListView) findViewById(R.id.mark_list);
        tvMarkNumber = (TextView) findViewById(R.id.mark_number_header);
        btnMarkNumberEdit = (EditText) findViewById(R.id.mark_number_edit);
        lvMarkList.setOnItemClickListener(this);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("number");
        if (TextUtils.isEmpty(phoneNumber)) finish();
        mUseStrictPhoneNumberComparation = getResources().getBoolean(com.android.internal.R.bool.config_use_strict_phone_number_comparation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvMarkNumber.setText(String.format(getString(R.string.cloud_location_lookup_mark_title), phoneNumber));
    }

    public void updateUserMarkInfo(String number, String userMark, int phoneType) {
        LocationUtils.updateUserMarkInfo(getContentResolver(), number, userMark, phoneType);
        ContentValues values = new ContentValues();
        values.put(Calls.GEOCODED_LOCATION, userMark);
        getContentResolver().update(Calls.CONTENT_URI, values,
                Calls.NUMBER + " = \"" + number + "\" OR PHONE_NUMBERS_EQUAL(number, ?, " +
                        (mUseStrictPhoneNumberComparation ? 1 : 0) + ")", null);
        finish();
    }

    public void btnCancelOnClick(View view) {
        finish();
    }

    public void btnComfirmOnClick(View view) {
        String userMark = btnMarkNumberEdit.getText().toString().trim();
        // 非空验证
        if (TextUtils.isEmpty(userMark)) {
            finish();
        } else {
            updateUserMarkInfo(phoneNumber, userMark, 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int value[] = getResources().getIntArray(R.array.cloud_location_lookup_mark_values);
        updateUserMarkInfo(phoneNumber, parent.getItemAtPosition(position).toString(), value[position]);
    }
}
