package bahar.swing;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import javax.swing.*;

public class StatusLabel extends JLabel {
    public StatusLabel() {
        this.setFont(ComponentFactory.VERDANA);
        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = StatusEvent.class)
    public void onEvent(StatusEvent event) {
        this.setText(event.statusText);
        validate();
    }
}
