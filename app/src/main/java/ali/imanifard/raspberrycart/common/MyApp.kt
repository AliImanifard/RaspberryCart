package ali.imanifard.raspberrycart.common

import ali.imanifard.raspberrycart.services.retrofit.ApiService
import ali.imanifard.raspberrycart.services.image_loading.FrescoImageLoadingService
import ali.imanifard.raspberrycart.services.image_loading.ImageLoadingService
import ali.imanifard.raspberrycart.data.Product.repository.ProductRepository
import ali.imanifard.raspberrycart.data.Product.repository.ProductRepositoryImpl
import ali.imanifard.raspberrycart.data.Product.repository.source.ProductLocalDataSource
import ali.imanifard.raspberrycart.data.Product.repository.source.ProductRemoteDataSource
import ali.imanifard.raspberrycart.data.cart.repository.CartRepository
import ali.imanifard.raspberrycart.data.cart.repository.CartRepositoryImpl
import ali.imanifard.raspberrycart.data.cart.repository.source.CartLocalDataSource
import ali.imanifard.raspberrycart.data.cart.repository.source.CartRemoteDataSource
import ali.imanifard.raspberrycart.data.db.AppDatabase
import ali.imanifard.raspberrycart.data.order.repository.OrderRepository
import ali.imanifard.raspberrycart.data.order.repository.OrderRepositoryImpl
import ali.imanifard.raspberrycart.data.order.repository.source.OrderLocalDataSource
import ali.imanifard.raspberrycart.data.order.repository.source.OrderRemoteDataSource
import ali.imanifard.raspberrycart.data.translate.repository.TranslateRepository
import ali.imanifard.raspberrycart.data.translate.repository.TranslateRepositoryImpl
import ali.imanifard.raspberrycart.data.translate.repository.source.TranslateRemoteDataSource
import ali.imanifard.raspberrycart.data.user.repository.UserRepository
import ali.imanifard.raspberrycart.data.user.repository.UserRepositoryImpl
import ali.imanifard.raspberrycart.data.user.repository.source.UserLocalDataSource
import ali.imanifard.raspberrycart.data.user.repository.source.UserRemoteDataSource
import ali.imanifard.raspberrycart.feature.checkout.CheckOutViewModel
import ali.imanifard.raspberrycart.feature.main.MainViewModel
import ali.imanifard.raspberrycart.feature.main.home.HomeViewModel
import ali.imanifard.raspberrycart.feature.main.ProductListAdapter
import ali.imanifard.raspberrycart.feature.main.auth.AuthViewModel
import ali.imanifard.raspberrycart.feature.main.cart.CartViewModel
import ali.imanifard.raspberrycart.feature.main.heart.HeartAdapter
import ali.imanifard.raspberrycart.feature.main.heart.HeartViewModel
import ali.imanifard.raspberrycart.feature.main.product_catalog.ProductCatalogViewModel
import ali.imanifard.raspberrycart.feature.main.product_detail_screen.ProductDetailViewModel
import ali.imanifard.raspberrycart.feature.main.profile.ProfileViewModel
import ali.imanifard.raspberrycart.feature.shipping.ShippingViewModel
import ali.imanifard.raspberrycart.services.retrofit.TranslateService
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this,
            ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .build()
        )

        val myModules = module {
            single<ApiService> { ApiService.createApiServiceInstance() }
            single<TranslateService> { TranslateService.createInstanceForTranslateService() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            factory(named("myProductListAdapterQualifier")) { (viewType : Int) -> ProductListAdapter(get(),get(),viewType) }
            single {
                Room.databaseBuilder(this@MyApp,AppDatabase::class.java,"db_app")
                    .build()
            }
            factory<ProductRepository> {
                ProductRepositoryImpl(ProductRemoteDataSource(),
                ProductLocalDataSource(get<AppDatabase>().productDAO())
            ) }
            factory<CartRepository> { CartRepositoryImpl(
                CartRemoteDataSource(get()),
                CartLocalDataSource(get<AppDatabase>().productDAO())
            ) }
            factory<TranslateRepository> { TranslateRepositoryImpl(TranslateRemoteDataSource()) }
            single<SharedPreferences> { this@MyApp.getSharedPreferences("app_settings", MODE_PRIVATE) }
            single<UserRepository> { UserRepositoryImpl(UserRemoteDataSource(get()), UserLocalDataSource(get())) }
            factory<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(),
                OrderLocalDataSource(get<AppDatabase>().productDAO())
            ) }
            viewModel(named("myHomeViewModel")) { HomeViewModel(get(),get()) }
            viewModel(named("myProductDetailViewModelQualifier")) { (bundle : Bundle) -> ProductDetailViewModel(bundle,get(),get()) }
            viewModel(named("myProductCatalogViewModel")) { (sort : Int) -> ProductCatalogViewModel(get(),get(), sort) }
            viewModel(named("myAuthViewModel")) { AuthViewModel(get()) }
            viewModel(named("myCartViewModel")) { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ProfileViewModel(get(),get()) }
            viewModel { HeartViewModel(get(),get()) }
            viewModel(named("myShippingViewModel")){ ShippingViewModel(get()) }

            viewModel { (orderId: Int) -> CheckOutViewModel(orderId,get()) }

            //factory { HeartAdapter(get(),) }

        }

        startKoin {
            androidContext(this@MyApp)
            modules(myModules)
        }

        // load token from memory every time app runs
        val userRepository:UserRepository=get()
        userRepository.loadToken()
    }
}