package com.kiran.jcache;

import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import javax.cache.spi.CachingProvider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;

import com.kiran.jcache.model.Address;
import com.kiran.jcache.model.Customer;
import com.kiran.jcache.util.CustomerCacheEntryEventFilter;
import com.kiran.jcache.util.CustomerCacheEntryListener;
import com.kiran.jcache.util.CustomerCacheLoaderFactory;
import com.kiran.jcache.util.CustomerCacheWriterFactory;

@SpringBootApplication
@EnableCaching
public class JcacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcacheApplication.class, args);
	}

	@Bean(destroyMethod="close")
	public CachingProvider createCachingProvider() {
		CachingProvider provider = Caching.getCachingProvider();
		return provider;
	}
	
	
	@Bean(destroyMethod="close")
	public CacheManager createCacheManager(CachingProvider cachingProvider) {
		CacheManager manager = cachingProvider.getCacheManager();
		return manager;
	}
	
	
	@Bean(name = "customersCacheConfig")
	public MutableConfiguration<Long,Customer> createCustomersCacheConfig(MutableCacheEntryListenerConfiguration<Long,Customer> listenerConfiguration){
		MutableConfiguration<Long,Customer> config = new MutableConfiguration<Long,Customer>()
				//.setTypes(Long.class, Customer.class)
				.setCacheWriterFactory(new CustomerCacheWriterFactory())
				.setWriteThrough(true)
				.setCacheLoaderFactory(new CustomerCacheLoaderFactory())
				.setReadThrough(true)
		        .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS,60)))
		        .addCacheEntryListenerConfiguration(listenerConfiguration)
		        .setStatisticsEnabled(true)
		        .setManagementEnabled(true);
		        
		return config;
	}
	
	@Bean(name = "customersCache",destroyMethod="close")
	public Cache<Long,Customer> createCustomersCache(CacheManager cacheManager, @Qualifier("customersCacheConfig")
	                                                              MutableConfiguration<Long,Customer> config){		
		return cacheManager.createCache("customers",config);
	}
	
	@Bean
	public JCacheCacheManager cacheCacheManager(CacheManager cacheManager) {
		return new JCacheCacheManager(cacheManager);
	}
	
	
	@Bean(name = "addressesCacheConfig")
	public MutableConfiguration<Long,Address> createAddressesCacheConfig(){
		MutableConfiguration<Long,Address> config = new MutableConfiguration<>();
		config.setStatisticsEnabled(true);
        config.setManagementEnabled(true);
		return config;
	}
	
	@Bean(name = "addressesCache",destroyMethod="close")
	public Cache<Long,Address> createAddressesCache(CacheManager cacheManager, @Qualifier("addressesCacheConfig")
	                                                              MutableConfiguration<Long,Address> config){		
		return cacheManager.createCache("addresses",config);
	}
	
	
	@Bean
	public MutableCacheEntryListenerConfiguration<Long,Customer> cacheEntryListener(){
		return new MutableCacheEntryListenerConfiguration<>(
				FactoryBuilder.factoryOf(CustomerCacheEntryListener.class),
				FactoryBuilder.factoryOf(CustomerCacheEntryEventFilter.class),
				true,
				true  // make this false for async process or timeconsuming tasks
				);
	}
	
	
}
