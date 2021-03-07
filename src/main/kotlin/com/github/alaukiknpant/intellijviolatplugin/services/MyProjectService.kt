package com.github.alaukiknpant.intellijviolatplugin.services

import com.github.alaukiknpant.intellijviolatplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
