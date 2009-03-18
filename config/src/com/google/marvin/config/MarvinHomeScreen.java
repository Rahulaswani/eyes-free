/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.marvin.config;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Alternate home screen that dispatches to the actual shell home screen
 * replacement.
 * 
 * @author sdoyon@google.com (Stephane Doyon)
 */

public class MarvinHomeScreen extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String packageName = "com.android.launcher";
    String className = "com.android.launcher.Launcher";
    int flags = Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY;
    try {
      if (prefs.getBoolean("use_shell", false) && shellInstalled()) {
        packageName = "com.google.marvin.shell";
        className = "com.google.marvin.shell.MarvinShell";
      }
      Context myContext = createPackageContext(packageName, flags);
      Class<?> appClass = myContext.getClassLoader().loadClass(className);
      Intent intent = new Intent(myContext, appClass);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      finish();
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  private boolean shellInstalled() {
    try {
      Context myContext = createPackageContext("com.google.marvin.shell", 0);
    } catch (NameNotFoundException e) {
      return false;
    }
    return true;
  }
}