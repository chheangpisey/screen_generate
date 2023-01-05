package com.cps.plugingenerate.template.base

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.generateSimpleLayout
import com.cps.plugingenerate.manager.PackageManager
import com.cps.plugingenerate.manager.PackageManager.applicationPackageName
import com.cps.plugingenerate.template.manifest.manifestTemplateXml
import com.cps.plugingenerate.template.utils.*
import java.io.File
import java.time.LocalDateTime

fun RecipeExecutor.createActivity(
    packageName: String = PackageManager.packageName,
    applicationPackageName: String = PackageManager.applicationPackageName,
    className: String,
    manifestOut: File,
    moduleData: ModuleTemplateData
): String {
    val activityClassName = "${className}Activity"
    val layoutFileName = "Activity${className}"
    mergeXml(manifestTemplateXml(packageName, activityClassName), manifestOut.resolve("AndroidManifest.xml"))
    generateSimpleLayout(moduleData, activityClassName, layoutFileName.toSnakeCase())
    return createActivity(packageName, applicationPackageName, className)
}

fun createActivity(
    packageName: String,
    applicationPackageName: String,
    className: String
) = """
  package $packageName
  
  import androidx.lifecycle.ViewModelProvider
  import $applicationPackageName.R
  import $applicationPackageName.di.Injection
  import $applicationPackageName.databinding.Activity${className}Binding
  import $applicationPackageName.base.BaseActivity
  
  /**
  * @author    : ${System.getProperty("user.home")}
  * Created on : ${LocalDateTime.now()}
  * File name  : ${className}Activity.kt
  */
 
  class ${className}Activity : BaseActivity<Activity${className}Binding, ${className}ViewModel>() {
  
    override val mLayoutId = R.layout.activity${convertLowerCase(className)}
    override fun getViewModelClass(): Class<${className}ViewModel> = ${className}ViewModel::class.java
    override fun getViewModelFactory(): ViewModelProvider.Factory = Injection.provide${className}ViewModelFactory
    
    override fun initView() {
        /** Coding here...*/

    }
    
  }
""".trimIndent()

fun RecipeExecutor.createFragment(
    packageName: String = PackageManager.packageName,
    applicationPackageName: String = PackageManager.applicationPackageName,
    className: String,
    manifestOut: File,
    moduleData: ModuleTemplateData
): String {
    val fragmentClassName = "${className}Fragment"
    val layoutFileName = "Fragment${className}"
    mergeXml(manifestTemplateXml(packageName, fragmentClassName), manifestOut.resolve("AndroidManifest.xml"))
    generateSimpleLayout(moduleData, fragmentClassName, layoutFileName.toSnakeCase())
    return createFragment(packageName, applicationPackageName, className)
}

fun createFragment(
    packageName: String,
    applicationPackageName: String,
    className: String
) = """
  package $packageName
  
  import androidx.lifecycle.ViewModelProvider
  import $applicationPackageName.R
  import $applicationPackageName.di.Injection
  import $applicationPackageName.databinding.Fragment${className}Binding
  import $applicationPackageName.base.BaseFragment
  
  class ${className}Fragment : BaseFragment<Fragment${className}Binding, ${className}ViewModel>() {
  
    override val mLayoutId = R.layout.fragment${convertLowerCase(className)}
    override fun getViewModelClass(): Class<${className}ViewModel> = ${className}ViewModel::class.java
    override fun getViewModelFactory(): ViewModelProvider.Factory = Injection.provide${className}ViewModelFactory
    
    override fun initView() {
        /** Coding here...*/

    }
    
  }
""".trimIndent()

fun createViewModel(
    packageName: String = PackageManager.packageName,
    className: String
) = """
  package $packageName
  
  import $applicationPackageName.base.BaseViewModel
  import $applicationPackageName.domain.${className}UseCase

 
  class ${className}ViewModel(private val useCase: ${className}UseCase) : BaseViewModel() {
  
  /** 1 - Using StateFlow : Sample GET-METHOD
      private val _getValueStateFlow: MutableStateFlow<StateFlowResponse> =
          MutableStateFlow(StateFlowResponse.Empty)
      val getValueStateFlow: StateFlow<StateFlowResponse> = _getValueStateFlow

      val gettingValueResponse = viewModelScope.launch {
          useCase.invoke()
              .catch { e ->
                  _getValueStateFlow.emit(StateFlowResponse.Failure(handleErrorResponse(e)))
              }.collectLatest { data ->
                  _getValueStateFlow.emit(StateFlowResponse.Success(data))
              }
      }
      
      2 - Using StateFlow : Sample POST-METHOD
      var requestBody = MutableSharedFlow<REQUEST_BODY>()
      private val _setValueStateFlow: MutableStateFlow<StateFlowResponse> =
        MutableStateFlow(StateFlowResponse.Empty)
      val setValueStateFlow: StateFlow<StateFlowResponse> = _setValueStateFlow

      @OptIn(ExperimentalCoroutinesApi::class)
      val fetchingValueResponse = requestBody.mapLatest {
        useCase.invokeCreate(it)
            .catch { e ->
                _setValueStateFlow.emit(StateFlowResponse.Failure(handleErrorResponse(e)))
            }.collectLatest { data ->
                _setValueStateFlow.emit(StateFlowResponse.Success(data))
            }
    }
  */
  }
""".trimIndent()

fun createViewModelFactory(
    packageName: String = PackageManager.packageName,
    className: String
) = """
  package $packageName
  
  import androidx.lifecycle.ViewModel
  import androidx.lifecycle.ViewModelProvider
  import $applicationPackageName.domain.${className}UseCase
  
  class ${className}ViewModelFactory(private val useCase: ${className}UseCase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ${className}ViewModel(useCase) as T
    }
""".trimIndent()

fun createRepository(
    repositoryPackageName: String = "$applicationPackageName.data.repository",
    className: String
) = """
  package $repositoryPackageName
  
  import $applicationPackageName.webservice.WebService
  
  class ${className}Repository(private val webService: WebService) {
   /** 
    1- POST - PUT Method:
        suspend fun sampleRequestBody(scope: CoroutineScope, req: [Your Request Model]):
                LiveData<ResourceResponse<[Your Response Model]>> {
            return networkRequest(scope, [Calling to service api])
        }
    2- GET Method:
        suspend fun sampleRequestGet(scope: CoroutineScope):
                LiveData<ResourceResponse<[Your Response Model]>> {
            return networkRequest(scope, [Calling to service api])
        }
    */
  }
""".trimIndent()

fun createRepositoryNew(
    repositoryPackageName: String = "$applicationPackageName.data.repository",
    className: String
) = """
    
    package $repositoryPackageName

    import android.util.Log
    import androidx.lifecycle.LiveData
    import $applicationPackageName.data.implement.${className}DataSourceImpl
    import $applicationPackageName.utils.retrofit.networkRequest
    import $applicationPackageName.service.implement.ApiServiceImpl
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.launch

    class ${className}Repository(
        private val apiServiceImpl: ApiServiceImpl,
        private val dataLocal: ${className}DataSourceImpl,
        private val dataRemote: ${className}DataSourceImpl
    ) {
        /**
        1- POST - PUT Method:
        suspend fun sampleRequestBody(scope: CoroutineScope, req: [Your Request Model]):
        LiveData<ResourceResponse<[Your Response Model]>> {
        return networkRequest(scope, [Calling to service api])
        }
        2- GET Method:
        suspend fun sampleRequestGet(scope: CoroutineScope):
        LiveData<ResourceResponse<[Your Response Model]>> {
        return networkRequest(scope, [Calling to service api])
        }
         */
    }
""".trimIndent()

fun createDao(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
  package ${packageName}.${className.lowercase()}.dbLocator
  
  import androidx.room.Dao
  
  @Dao
  interface ${className}Dao {
  
  }
""".trimIndent()

fun createDatabase(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
    package $packageName.${className.lowercase()}.dbLocator
    
    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
    
    private const val DATABASE_NAME = ""
    @Database(entities = [YourClassName::class], version = 1, exportSchema = false)
    abstract class ${className}Database: RoomDatabase() {
        abstract fun ${className.lowercase()}(): ${className}Dao

        companion object {
            @Volatile
            private var INSTANCE: ${className}Database? = null

            fun getInstance(context: Context): ${className}Database =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(context).also { INSTANCE = it }
                }

            private fun buildDatabase(context: Context) =
                Room.databaseBuilder(
                    context.applicationContext,
                    ${className}Database::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
        }
    }
      
""".trimIndent()

fun createTable(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String) = """
    
    package $packageName.${className.lowercase()}.dbLocator
     
    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "")
    data class ${className}Tb(
        @PrimaryKey(autoGenerate = true)
        val uId: Int = 0
    )
""".trimIndent()

fun createServiceLocator(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
    
    package  $packageName.${className.lowercase()}.dbLocator

    import android.content.Context
    import androidx.annotation.VisibleForTesting
    import androidx.room.Room
    import $packageName.${className.lowercase()}.${className}FakeDatasource
    import $packageName.${className.lowercase()}.${className}LocalDatasource
    import $applicationPackageName.data.implement.${className.lowercase()}.${className}DataSourceImpl
    import $applicationPackageName.data.repository.${className}Repository
    import $applicationPackageName.webservice.WebService

    object ${className}ServiceLocator {
        private var database: ${className}Database? = null
        @Volatile
        var repository: ${className}Repository? = null
            @VisibleForTesting set

        fun provideRepository(context: Context): ${className}Repository {
            synchronized(this) {
                return repository ?: repository ?: createRepository(context)
            }
        }

        private fun createRepository(context: Context): ${className}Repository {
            return ${className}Repository(webService = WebService, ${className}FakeDatasource, createDataLocalDataSource(context))
        }

        private fun createDataLocalDataSource(context: Context): ${className}DataSourceImpl {
            val database = database ?: createDataBase(context)
            return ${className}LocalDatasource(database.${className}Dao())
        }

        private fun createDataBase(context: Context): ${className}Database {
            val result = Room.databaseBuilder(
                context.applicationContext,
                ${className}Database::class.java, "Users.db"
            ).build()
            database = result
            return result
        }
    }
    
""".trimIndent()

fun createFakeDatasource(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
  package $packageName.${className.lowercase()}

  import $applicationPackageName.implement.${className.lowercase()}.${className}DataSourceImpl

  object ${className}FakeDataSource: ${className}DataSourceImpl {

  }
  
""".trimIndent()

fun createRemoteDatasource(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
  package $packageName.${className.lowercase()}
    
  class ${className}RemoteDatasource {
  
  }
""".trimIndent()

fun createLocalDatasource(
    packageName: String = "$applicationPackageName.data.datasource",
    className: String
) = """
     package $packageName.${className.lowercase()}
    
     import $packageName.${className.lowercase()}.dbLocator.${className}Database
     import $applicationPackageName.data.implement.${className}DataSourceImpl
     import kotlinx.coroutines.CoroutineDispatcher
     import kotlinx.coroutines.Dispatchers
     import kotlinx.coroutines.withContext
  
     class ${className}LocalDatasource(
     private var database: ${className}Database,
     private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ${className}DataSourceImpl {
    
     }
""".trimIndent()

fun createDatasourceImpl(
    packageName: String = "$applicationPackageName.data.implement",
    className: String
) = """
    package $packageName
  
    interface ${className}DataSourceImpl {
        
    }
""".trimIndent()

fun createRepositoryImpl(
    packageName: String = "$applicationPackageName.data.implement",
    className: String
) = """
    package $packageName.${className.lowercase()}
  
    interface ${className}RepositoryImpl {
        
    }
""".trimIndent()

//Create UseCase In Directory domain
fun createUseCase(
    packageName: String = "$applicationPackageName.domain",
    className: String
) = """
  package $packageName
    
  import ig.core.android.data.repository.${className}Repository
  import kotlinx.coroutines.CoroutineDispatcher
  import kotlinx.coroutines.Dispatchers

  class ${className}UseCase(
      private val repository: ${className}Repository,
      private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
  ) {

  }
""".trimIndent()


