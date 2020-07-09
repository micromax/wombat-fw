package com.cloudsgen.system.core;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class AbstractServices {


    public Message message;

    public AbstractServices(){


    }


    public Handler<Object> Servo(Message message)
    {
        this.message = message;

        return Object::notifyAll;
    }





}
