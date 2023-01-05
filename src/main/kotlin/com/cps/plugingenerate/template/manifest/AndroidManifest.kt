package com.cps.plugingenerate.template.manifest

fun manifestTemplateXml(packageName: String, activityClassName: String) = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools">

    <application>
        <activity android:name="$packageName.$activityClassName"/>   
    </application>
</manifest>
"""