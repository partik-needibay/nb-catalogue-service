package com.needibay.cart.component;

import java.util.concurrent.CompletableFuture;

public interface IServiceCommand {
    public Object execute();
    public CompletableFuture<Object> executeAsync();

    public default String executeString(){
        return "";
    }

}
