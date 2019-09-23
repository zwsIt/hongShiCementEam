package com.supcon.mes.middleware.model.bean;

/**
 * @Author xushiyun
 * @Create-time 7/26/19
 * @Pageage com.supcon.mes.middleware.ui.fragment
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public interface IDoFilter {
    default void doFilter(){
    
    }
    default void doFilter(String mes) {
    
    }
    default void doFilter(int page){
    
    }
    
    default void doFilter(int page, String filterType, String filterValue){
    
    }
}
