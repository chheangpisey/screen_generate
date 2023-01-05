package com.cps.plugingenerate.template

import com.android.tools.idea.wizard.template.*
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter

val cleanArchFragmentTemplate
    get() = template {
      name = "CPS:MVVM Generate Fragment Screen"
      description = "Create files for clean architecture"
      minApi = 21
      category = Category.Fragment
      formFactor = FormFactor.Mobile
      screens = listOf(WizardUiContext.MenuEntry)

      val packageNameParam = defaultPackageNameParameter
      val osCheck = System.getProperty("os.name")

      val dir = { t: ModuleTemplateData ->
        t.rootDir.absolutePath
      }
      val locationParam = stringParameter {
        name = "Project Location"
        default = dir.toString()
//        default = if (osCheck.contains("Windows"))
//          "D:\\"
//        else "Documents/"
      }

      val classNameParam = stringParameter {
        name = "Class Name"
        default = "Main"
        help = "Use the class name for prefix"
        constraints = listOf(Constraint.NONEMPTY)
      }

//      widgets(
//        PackageNameWidget(packageNameParam),
//        TextFieldWidget(classNameParam),
//        TextFieldWidget(locationParam)
//      )

      widgets(
        PackageNameWidget(packageNameParam),
        TextFieldWidget(classNameParam)
      )

      recipe = { data: TemplateData ->
        cleanArchFragmentTemplate(
          data as ModuleTemplateData,
          data.rootDir.absolutePath,
          packageNameParam.value,
          classNameParam.value
        )
      }

//      if (locationProject != "") {
//        locationParam.value = locationProject
//      }
    }
