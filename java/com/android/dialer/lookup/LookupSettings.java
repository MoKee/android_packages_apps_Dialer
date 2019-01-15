/*
 * Copyright (C) 2014 Xiao-Long Chen <chillermillerlong@hotmail.com>
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

package com.android.dialer.lookup;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.mokee.utils.MoKeeUtils;
import android.provider.Settings;

import mokee.providers.MKSettings;

import java.util.List;

public final class LookupSettings {
  private static final String TAG = LookupSettings.class.getSimpleName();

  /** Forward lookup providers */
  public static final String FLP_GOOGLE = "Google";
  public static final String FLP_OPENSTREETMAP = "OpenStreetMap";
  public static final String FLP_DEFAULT = FLP_GOOGLE;

  /** People lookup providers */
  public static final String PLP_AUSKUNFT = "Auskunft";
  public static final String PLP_DEFAULT = PLP_AUSKUNFT;

  /** Reverse lookup providers */
  public static final String RLP_OPENCNAM = "OpenCnam";
  public static final String RLP_YELLOWPAGES = "YellowPages";
  public static final String RLP_YELLOWPAGES_CA = "YellowPages_CA";
  public static final String RLP_ZABASEARCH = "ZabaSearch";
  public static final String RLP_MOKEE_CHINESE = "MoKeeChinese";
  public static final String RLP_DASTELEFONBUCH = "DasTelefonbuch";
  public static final String RLP_AUSKUNFT = "Auskunft";
  public static final String RLP_DEFAULT = MoKeeUtils.isSupportLanguage(true) ? RLP_MOKEE_CHINESE : RLP_OPENCNAM;

  private LookupSettings() {
  }

  public static boolean isForwardLookupEnabled(Context context) {
    return MKSettings.System.getInt(context.getContentResolver(),
        MKSettings.System.ENABLE_FORWARD_LOOKUP, 0) != 0;
  }

  public static boolean isPeopleLookupEnabled(Context context) {
    return MKSettings.System.getInt(context.getContentResolver(),
        MKSettings.System.ENABLE_PEOPLE_LOOKUP, 0) != 0;
  }

  public static boolean isReverseLookupEnabled(Context context) {
    return MKSettings.System.getInt(context.getContentResolver(),
        MKSettings.System.ENABLE_REVERSE_LOOKUP, 0) != 0;
  }

  public static String getForwardLookupProvider(Context context) {
    return getLookupProvider(context,
        MKSettings.System.FORWARD_LOOKUP_PROVIDER, FLP_DEFAULT);
  }

  public static String getPeopleLookupProvider(Context context) {
    return getLookupProvider(context,
        MKSettings.System.PEOPLE_LOOKUP_PROVIDER, PLP_DEFAULT);
  }

  public static String getReverseLookupProvider(Context context) {
    String provider = getLookupProvider(context,
        MKSettings.System.REVERSE_LOOKUP_PROVIDER, RLP_DEFAULT);

    if ("Google".equals(provider)) {
      MKSettings.System.putString(context.getContentResolver(),
          MKSettings.System.REVERSE_LOOKUP_PROVIDER, RLP_DEFAULT);
      provider = RLP_DEFAULT;
    }

    return provider;
  }

  private static String getLookupProvider(Context context, String key, String defaultValue) {
    ContentResolver cr = context.getContentResolver();
    String provider = MKSettings.System.getString(cr, key);

    if (provider == null) {
      MKSettings.System.putString(cr, key, defaultValue);
      return defaultValue;
    }

    return provider;
  }
}
