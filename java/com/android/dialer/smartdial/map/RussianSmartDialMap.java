/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.dialer.smartdial.map;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import com.android.dialer.dialpadview.DialpadCharMappings;
import com.android.dialer.smartdial.util.SmartDialMatchPosition;
import com.android.dialer.smartdial.util.SmartDialNameMatcher;
import com.google.common.base.Optional;

import java.util.ArrayList;

/** A {@link SmartDialMap} for the Russian alphabet. */
@SuppressWarnings("Guava")
final class RussianSmartDialMap extends SmartDialMap {

  private static RussianSmartDialMap instance;

  static RussianSmartDialMap getInstance() {
    if (instance == null) {
      instance = new RussianSmartDialMap();
    }

    return instance;
  }

  private RussianSmartDialMap() {}

  @Override
  Optional<Character> normalizeCharacter(char ch) {
    ch = Character.toLowerCase(ch);
    return isValidDialpadAlphabeticChar(ch) ? Optional.of(ch) : Optional.absent();
  }

  @Override
  SimpleArrayMap<Character, Character> getCharToKeyMap() {
    return DialpadCharMappings.getCharToKeyMap("rus");
  }

  @Override
  public boolean matchesCombination(Context context, SmartDialNameMatcher smartDialNameMatcher, String displayName, String query, ArrayList<SmartDialMatchPosition> matchList) {
    return smartDialNameMatcher.matchesCombination(context, displayName, query, matchList);
  }

  @Override
  public String transliterateName(String index) {
    return index;
  }
}
