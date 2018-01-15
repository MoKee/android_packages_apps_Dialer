/*
 * Copyright (C) 2016-2018 The MoKee Open Source Project
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

package com.android.contacts.common.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import com.android.contacts.common.R;

public final class CallLogDeleteLimitPreference extends ListPreference {

    private ContactsPreferences mPreferences;
    private Context mContext;

    public CallLogDeleteLimitPreference(Context context) {
        super(context);
        prepare();
    }

    public CallLogDeleteLimitPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare();
    }

    private void prepare() {
        mContext = getContext();
        mPreferences = new ContactsPreferences(mContext);
        setEntries(mContext.getResources().getStringArray(R.array.call_log_delete_limit_entries));
        setEntryValues(mContext.getResources().getStringArray(R.array.call_log_delete_limit_values));
        setValue(String.valueOf(mPreferences.getCallLogDeleteLimit()));
    }

    @Override
    protected boolean shouldPersist() {
        return false;   // This preference takes care of its own storage
    }

    @Override
    public CharSequence getSummary() {
        int limit = mPreferences.getCallLogDeleteLimit();
        return limit == 0 ? mContext.getString(R.string.call_log_delete_limit_nolimit) : mContext.getString(R.string.call_log_delete_limit_summary, limit);
    }

    @Override
    protected boolean persistString(String value) {
        int newValue = Integer.parseInt(value);
        if (newValue != mPreferences.getDisplayOrder()) {
            mPreferences.setCallLogDeleteLimit(newValue);
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
}
