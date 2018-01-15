/*
 * Copyright (C) 2016-2019 The MoKee Open Source Project
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

package com.android.dialer.calllog.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import android.provider.Settings;
import android.util.AttributeSet;

import com.android.dialer.R;

public final class CallLogLimitPreference extends ListPreference {

    private Context mContext;

    public CallLogLimitPreference(Context context) {
        super(context);
        prepare();
    }

    public CallLogLimitPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare();
    }

    private void prepare() {
        mContext = getContext();
        setEntries(mContext.getResources().getStringArray(R.array.display_options_call_log_limit_entries));
        setEntryValues(mContext.getResources().getStringArray(R.array.display_options_call_log_limit_values));
        setValue(String.valueOf(getCallLogLimit()));
    }

    @Override
    protected boolean shouldPersist() {
        return false;   // This preference takes care of its own storage
    }

    @Override
    public CharSequence getSummary() {
        int limit = getCallLogLimit();
        return limit == 0 ? mContext.getString(R.string.display_options_call_log_limit_unlimited) : mContext.getString(R.string.display_options_call_log_limit_summary, limit);
    }

    @Override
    protected boolean persistString(String value) {
        int newValue = Integer.parseInt(value);
        if (newValue != getCallLogLimit()) {
            setCallLogLimit(newValue);
            notifyChanged();
        }
        return true;
    }

    @Override
    // UX recommendation is not to show cancel button on such lists.
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(null, null);
    }

    public int getCallLogLimit() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.CALL_LOG_LIMIT, 500);
    }

    public void setCallLogLimit(int limit) {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.CALL_LOG_LIMIT, limit);
    }
}
