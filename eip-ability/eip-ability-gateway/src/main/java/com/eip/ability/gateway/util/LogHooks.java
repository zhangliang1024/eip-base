package com.eip.ability.gateway.util;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Operators;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LogHooks {
    
    private static final String KEY = "logMdc";
    
    @PostConstruct
    @SuppressWarnings("unchecked")
    public void setHook() {
        reactor.core.publisher.Hooks.onEachOperator(KEY,
                Operators.lift((scannable, coreSubscriber) -> new MdcSubscriber(coreSubscriber)));
    }
    
    @PreDestroy
    public void resetHook() {
        reactor.core.publisher.Hooks.resetOnEachOperator(KEY);
    }
    
}