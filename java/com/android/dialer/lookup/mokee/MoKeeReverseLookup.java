/*
 * Copyright (C) 2019 The MoKee Open Source Project
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

package com.android.dialer.lookup.mokee;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.dialer.lookup.ContactBuilder;
import com.android.dialer.lookup.ReverseLookup;
import com.android.dialer.phonenumbercache.ContactInfo;
import com.mokee.cloud.location.CloudNumber;

public class MoKeeReverseLookup extends ReverseLookup {
    private static final String TAG =
            MoKeeReverseLookup.class.getSimpleName();

    private static final boolean DEBUG = false;

    public MoKeeReverseLookup (Context context) {
    }

    @Override
    public ContactInfo lookupNumber(Context context, String normalizedNumber, String formattedNumber) {
        if (normalizedNumber.startsWith("+") && !normalizedNumber.startsWith("+86")) {
            // Any non-CN number will return "We currently accept only CN numbers"
            return null;
        }
        String displayName = CloudNumber.detect(formattedNumber, context);
        if (DEBUG) Log.d(TAG, "Reverse lookup returned name: " + displayName);

        if (TextUtils.isEmpty(displayName)) return null;

        String number = formattedNumber != null
                ? formattedNumber : normalizedNumber;

        ContactBuilder builder = new ContactBuilder(
                ContactBuilder.REVERSE_LOOKUP,
                normalizedNumber, formattedNumber);
        builder.setName(ContactBuilder.Name.createDisplayName(displayName));
        builder.addPhoneNumber(ContactBuilder.PhoneNumber.createMainNumber(number));
        builder.setPhotoUri(ContactBuilder.PHOTO_URI_BUSINESS);
        builder.setGeoDescription(displayName);

        return builder.build();
    }
}
