package br.com.tmdev.pocrediscache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableCaching
public class PocRedisCacheApplication {

    private static final Logger LOG = LoggerFactory.getLogger(PocRedisCacheApplication.class);

    @Value("${spring.redis.host}")
    private String cacheHost;

    @Value("${spring.redis.port}")
    private String cachePort;

    @Value("${spring.redis.password}")
    private String cachePassword;

    @Value("${spring.redis.ssl}")
    private boolean useSsl;

    @Value("${spring.redis.expiretime}")
    private long defaultExpiration;

    private static final Map<String, Long> ttlConfigMethods = new HashMap<>();

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/redis-ttl.properties"));
            ttlConfigMethods.putAll(properties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> Long.parseLong(String.valueOf(e.getValue())))));
        } catch (IOException e) {
            LOG.error("Redis ttl configuration file load fail");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(PocRedisCacheApplication.class, args);
    }

    @Primary
    @Bean(name = "inMemoryCacheManager")
    public CacheManager inMemoryCacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }

    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        redisCacheManager.setDefaultExpiration(defaultExpiration);
        redisCacheManager.setExpires(ttlConfigMethods);
        return redisCacheManager;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setDefaultSerializer(jacksonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> jacksonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }


    private ObjectMapper objectMapper() {
        return new ObjectMapper()
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .enable(DeserializationFeature.USE_LONG_FOR_INTS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(cacheHost);
        jedisConnectionFactory.setPort(Integer.parseInt(cachePort));
        jedisConnectionFactory.setPassword(cachePassword);
        jedisConnectionFactory.setUseSsl(useSsl);
        return jedisConnectionFactory;
    }
}
