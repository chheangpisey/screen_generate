package com.cps.plugingenerate.services

import com.intellij.openapi.project.Project
import com.cps.plugingenerate.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
