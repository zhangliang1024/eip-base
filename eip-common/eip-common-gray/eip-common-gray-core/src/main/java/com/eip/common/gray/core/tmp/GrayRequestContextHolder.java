// package com.eip.common.gray;
//
// import com.alibaba.ttl.TransmittableThreadLocal;
//
// /**
//  * ClassName: GrayRequestContextHolder
//  * Function:
//  * Date: 2023年01月03 16:29:21
//  *
//  * @author 张良 E-mail:zhangliang01@jingyougroup.com
//  * @version V1.0.0
//  */
// public class GrayRequestContextHolder {
//
//
//     private static final TransmittableThreadLocal<GrayRequestContext> holder = TransmittableThreadLocal.withInitial(GrayRequestContext::new);
//
//     public static String getContext(String key){
//         return holder.get().get(key);
//     }
//
//     public static GrayRequestContext getGrayRequestContext(){
//         return holder.get();
//     }
//
//     public static void setContext(GrayRequestContext context) {
//         holder.set(context);
//     }
//
//     public static void cleanContext() {
//         holder.remove();
//     }
// }
