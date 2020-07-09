package app.services;

import com.cloudsgen.system.core.AbstractServices;
import com.cloudsgen.system.core.Services;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

@Services(Name = "SimpleServices")
public class SimpleServices  extends AbstractServices {


    public SimpleServices() {

    }

    @Override
    public Handler<Object> Servo(Message message) {
        super.Servo(message);




        return Object::notifyAll;
    }
}
