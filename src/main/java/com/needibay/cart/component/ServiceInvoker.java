package com.needibay.cart.component;

import java.util.concurrent.CompletableFuture;


public class ServiceInvoker {
    private IServiceCommand serviceCommand;

    public ServiceInvoker(IServiceCommand serviceCommand) {
        this.serviceCommand = serviceCommand;
    }

    public Object ExecuteRequest() {
        System.out.println("Start: Service Request");
        return serviceCommand.execute();
    }
    public CompletableFuture<Object> ExecuteAsyncRequest() {
        return serviceCommand.executeAsync();
    }

}
