package com.cps.plugingenerate.template

import com.android.tools.idea.wizard.template.*
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter

val cleanArchActivityTemplate
    get() = template {
      name = "CPS:MVVM Generate Activity Screen"
      description = "Create files for clean architecture"
      minApi = 21
      category = Category.Activity
      formFactor = FormFactor.Mobile
      screens = listOf(WizardUiContext.MenuEntry)


      val packageNameParam = defaultPackageNameParameter

      val classNameParam = stringParameter {
        name = "Class Name"
        default = "Main"
        help = "Use the class name for prefix"
        constraints = listOf(Constraint.NONEMPTY)
      }

      widgets(
        PackageNameWidget(packageNameParam),
        TextFieldWidget(classNameParam)
      )

      recipe = { data: TemplateData ->
        cleanArchActivityTemplate(
          data as ModuleTemplateData,
          data.rootDir.absolutePath,
          packageNameParam.value,
          classNameParam.value
        )
      }

    }
