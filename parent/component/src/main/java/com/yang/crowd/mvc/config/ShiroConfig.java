package com.yang.crowd.mvc.config;

import com.yang.crowd.mvc.realm.Realm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    public SystemLogOutFilter systemLogOutFilter;
    //不加这个注解不生效，具体不详
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    //将自己的验证方式加入容器
    @Bean
    public Realm myShiroRealm() {
        Realm realms=new Realm();
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        //名称
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //加密次数
//        hashedCredentialsMatcher.setHashIterations(1024);
        realms.setCredentialsMatcher(hashedCredentialsMatcher);
        return realms;
    }
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
//        若想要配置Authenticator，此配置必须在setRealms前
//        securityManager.setAuthenticator(modularRealmAuthenticator());
//        List<Realm> list=new ArrayList<>();
//        list.add(myShiroRealm());
//        list.add(secondRealm());
//        securityManager.setRealms(list);//底层会调用modularRealmAuthenticator.setRealms();将realms加进Authenticator
//        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        System.out.println(securityManager);
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        //在此处的配置url权限采用第一次匹配优先（即若两次配置冲突，如/jsp/login.jsp=anon /**=authc login.jsp可访问，但/**=authc /jsp/login.jsp=anon login.jsp不可访问）
        //anon：为可被匿名访问  authc：需要认证  logout：登出
        map.put("/admin/to/login/page.html","anon");
        map.put("/admin/do/login.html","anon");
        map.put("/admin/do/logout.html","logout");
        map.put("/**","authc");
        Map<String, Filter> logOutFilter=new HashMap<>();
        logOutFilter.put("logout",systemLogOutFilter);
        shiroFilterFactoryBean.setFilters(logOutFilter);
        shiroFilterFactoryBean.setLoginUrl("/admin/to/login/page.html");
        shiroFilterFactoryBean.setSuccessUrl("/admin/to/main/page.html");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
}

