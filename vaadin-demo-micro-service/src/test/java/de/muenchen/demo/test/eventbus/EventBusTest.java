package de.muenchen.demo.test.eventbus;

import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import reactor.bus.Event;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class EventBusTest {

    private final EventBus eventBus = new EventBus();

    @Before
    public void before(){
        eventBus.on(new RequestEntityKey(RequestEvent.CREATE, Buerger.class).toSelector(), this::create);
    }

    private void create(Event<?> event) {
        String vorname;
        if (event.getData() instanceof Buerger)
            vorname = ((Buerger) event.getData()).getVorname();
        else if (event.getData() instanceof Link)
            vorname = ((Link) event.getData()).getHref();
        else
            throw new AssertionError();

        System.out.println(vorname);
        Assert.assertEquals(vorname, "hans");
    }

    @Test
    public void buergerTest(){
        Buerger b = new Buerger();
        b.setVorname("hans");
        eventBus.notify(new RequestEntityKey(RequestEvent.CREATE, Buerger.class), Event.wrap(b));
    }

    @Test
    public void linkTest(){
        Link link = new Link("http://www.google.com");
        eventBus.notify(new RequestEntityKey(RequestEvent.CREATE, Buerger.class), Event.wrap(link));
    }

}
