package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("shipOrderDelegate")
public class ShipOrderDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        // Add your custom order shipping logic here
    }

}