package com.cps.plugingenerate.manager

fun addPackageName(packageName: String, applicationPackageName: String) =
  PackageManager.setPackageName(packageName, applicationPackageName)

fun addPackageNameFrg(packageName: String, applicationPackageName: String) =
  PackageManager.setPackageNameFrg(packageName, applicationPackageName)

object PackageManager {

  var packageName: String = ""
  //val packageName by lazy { _packageName }

  var applicationPackageName: String = ""
  //val applicationPackageName by lazy { _applicationPackageName }

  fun setPackageName(packageName: String, applicationPackageName: String) {
    this.packageName = packageName
    this.applicationPackageName = applicationPackageName
  }

  fun setPackageNameFrg(packageName: String, applicationPackageName: String) {
    this.packageName = packageName
    this.applicationPackageName = applicationPackageName
  }

}