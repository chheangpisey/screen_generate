package com.cps.plugingenerate.template

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.cps.plugingenerate.listeners.MyProjectManagerListener.Companion.projectInstance
import com.cps.plugingenerate.locationProject
import com.cps.plugingenerate.manager.PackageManager
import com.cps.plugingenerate.manager.Path.*
import com.cps.plugingenerate.manager.ProjectFileManager
import com.cps.plugingenerate.manager.addPackageName
import com.cps.plugingenerate.template.base.*
import com.cps.plugingenerate.template.utils.*

fun RecipeExecutor.cleanArchActivityTemplate(
  moduleData: ModuleTemplateData,
  location: String,
  packageName: String,
  className: String
) {

  locationProject = location
  val (projectData, _, _, manifestOut) = moduleData
  val project = projectInstance ?: return

  addAllKotlinDependencies(moduleData)
  addPackageName(packageName, projectData.applicationPackage.toString())

  val pfm = ProjectFileManager(project)
  if (pfm.init().not()) return

  createActivity(className = className, manifestOut = manifestOut, moduleData = moduleData)
    .save(pfm.dirOf(APP), packageName, "${className}Activity".asKt(), className)

  createViewModel(className = className)
    .save(pfm.dirOf(APP), packageName, "${className}ViewModel".asKt(), "")

  createViewModelFactory(className = className)
    .save(pfm.dirOf(APP), packageName, "${className}ViewModelFactory".asKt(), "")

  val repositoryDir = PackageManager.applicationPackageName + ".data.repository"
  createRepositoryNew(className = className)
    .save(pfm.dirOf(APP), repositoryDir, "${className}Repository".asKt(), "")

  val dataSourceDir = PackageManager.applicationPackageName + ".data.datasource"

  createDao(className = className)
    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}.dbLocator", "${className}Dao".asKt(), "")
  createDatabase(className = className)
    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}.dbLocator", "${className}Database".asKt(), "")
  createTable(className = className)
    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}.dbLocator", "${className}Table".asKt(), "")

  //  createServiceLocator(className = className)
  //    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}.dbLocator", "${className}ServiceLocator".asKt(), "")

  //  createFakeDatasource(className = className)
  //    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}", "${className}FakeDatasource".asKt(), "")

  createRemoteDatasource(className = className)
    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}", "${className}RemoteDatasource".asKt(), "")

  createLocalDatasource(className = className)
    .save(pfm.dirOf(APP), "$dataSourceDir.${className.lowercase()}", "${className}LocalDatasource".asKt(), "")

  val dataImplementDir = PackageManager.applicationPackageName + ".data.implement"
  createDatasourceImpl(className = className)
    .save(pfm.dirOf(APP), dataImplementDir, "${className}DatasourceImpl".asKt(), "")

  //  createRepositoryImpl(className = className)
  //    .save(pfm.dirOf(APP), "$dataImplementDir.${className.lowercase()}", "${className}RepositoryImpl".asKt(), "")

  val domainDir = PackageManager.applicationPackageName + ".domain"
  createUseCase(className = className)
    .save(pfm.dirOf(APP), domainDir, "${className}UseCase".asKt(), "")

}

