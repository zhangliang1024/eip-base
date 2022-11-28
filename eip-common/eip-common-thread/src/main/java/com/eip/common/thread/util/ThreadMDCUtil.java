package com.eip.common.thread.util;

import com.eip.common.core.constants.GlobalConstans;
import com.eip.common.core.utils.uid.GenerateUtil;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

public class ThreadMDCUtil {

    public static void setTraceIdIfAbsent() {
        if (MDC.get(GlobalConstans.GLOBAL_TRACE_ID) == null) {
            String logTraceId = GenerateUtil.getLogTraceId();
            MDC.put(GlobalConstans.GLOBAL_TRACE_ID, logTraceId);
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}