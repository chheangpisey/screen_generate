package com.cps.plugingenerate

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider
import com.cps.plugingenerate.template.cleanArchActivityTemplate
import com.cps.plugingenerate.template.cleanArchFragmentTemplate

class WizardTemplateProviderImpl : WizardTemplateProvider() {
  override fun getTemplates(): List<Template> = listOf(cleanArchActivityTemplate, cleanArchFragmentTemplate)
}

var locationProject = ""
