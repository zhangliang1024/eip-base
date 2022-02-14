package com.eip.common.core.auth;

/**
 * ClassName: AuthUserContext
 * Function:
 * Date: 2022年02月14 13:57:12
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class AuthUserContext {

    private static class ThreadLocalHolder{
        private static final ThreadLocal<AuthUserDetail> INSTANCE = new ThreadLocal<>();
    }

    public static AuthUserDetail get(){
        return ThreadLocalHolder.INSTANCE.get();
    }

    public static void set(AuthUserDetail userDetail){
        ThreadLocalHolder.INSTANCE.set(userDetail);
    }

    public static void remove(){
        ThreadLocalHolder.INSTANCE.remove();
    }
}
