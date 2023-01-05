package com.cps.plugingenerate.template.utils

import com.cps.plugingenerate.locationProject
import com.cps.plugingenerate.manager.PackageManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinLanguage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun String.asKt() = "${this.capitalize()}.kt"

val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
fun String.toSnakeCase() = camelRegex.replace(this) { "_${it.value}" }.toLowerCase()

/**
 * Checking project directory and create a subDir with file include a function to
 * written overwrite data in file Injection.kt
 */
fun String.save(srcDir: PsiDirectory, subDirPath: String, fileName: String, className: String) {
  try {
    val destDir = subDirPath.split(".").toDir(srcDir)
    val psiFile = PsiFileFactory
      .getInstance(srcDir.project)
      .createFileFromText(fileName, KotlinLanguage.INSTANCE, this)
       destDir.add(psiFile)

    if (fileName.contains("Activity") || fileName.contains("Fragment"))
      appendInjection(
        className,
        locationProject,
        replaceDotBySlash(PackageManager.applicationPackageName)
      )

  } catch (exc: Exception) {
    exc.printStackTrace()
  }
}

fun convertLowerCase(string: String): String {
  val sb = StringBuffer()
  for (i in string.indices) {
    if (Character.isUpperCase(string[i])) {
      sb.append("_")
      sb.append(string[i])
    } else {
      sb.append(string[i])
    }
  }
  val result = sb.toString()
  return result.lowercase()
}

fun List<String>.toDir(srcDir: PsiDirectory): PsiDirectory {
  var result = srcDir
  forEach {
    result = result.findSubdirectory(it) ?: result.createSubdirectory(it)
  }
  return result
}

fun appendInjection(className: String, location: String, fileName: String) {
  val osCheck = System.getProperty("os.name")
//  val userHomeMac = System.getProperty("user.home")
//  val userHomeWin = System.getProperty("user.dir")

  val resource = "/src/main/java"

  val dir = if (osCheck.contains("Windows"))
  location + replaceWinDir("/$resource$fileName/di/Injection.kt")
  else
    "$location/$resource/$fileName/di/Injection.kt"

  val st = readFileAsString(dir)
  val content = st +
          "\n" +
          "    fun provide${className}ViewModelFactory(context: Context): ${className}ViewModelFactory =\n" +
          "        ${className}ViewModelFactory(\n" +
          "            ${className}UseCase(\n" +
          "                ${className}Repository(apiServiceImpl, ${className}LocalDatasource(${className}Database.getInstance(context))))" +
          "\n" +
          "        )" +
          "\n" +
          "}"
  File(dir).writeText(content)
}

@Throws(java.lang.Exception::class)
fun readFileAsString(fileName: String): String {
  val data = String(Files.readAllBytes(Paths.get(fileName)))
  return data.substring(0, data.length - 1)
}

private fun replaceDotBySlash(string: String): String {
  return string.replace(".", "/")
}

private fun replaceWinDir(string: String): String {
  return string.replace("/", "\\")
}