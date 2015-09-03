package de.muenchen.vaadin.ui.controller.factorys;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.MainView;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.RefreshEvent;
import de.muenchen.vaadin.ui.components.*;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.components.buttons.TableAction;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by rene.zarwel on 26.08.15.
 */
@Component
@UIScope
public class BuergerViewFactory implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewFactory.class);

    private BuergerViewController controller;

    /**Singeltons of Components. **/
    private transient Optional<BuergerSearchTable> searchTable = Optional.<BuergerSearchTable>empty();
    private transient Optional<ChildSearchTable> childSearchTable = Optional.empty();
    private transient Optional<BuergerChildTab> childTab = Optional.empty();
    private transient Optional<BuergerCreateForm> createForm = Optional.empty();
    private transient Optional<BuergerCreateForm> createChildForm = Optional.empty();
    private transient Optional<BuergerUpdateForm> updateForm = Optional.empty();
    private transient Optional<BuergerReadForm> readForm = Optional.empty();
    private transient Optional<HistoryForm> historyForm = Optional.empty();
    private transient Optional<BuergerPartnerSearchTable> partnerSearchTable = Optional.empty();
    private transient Optional<BuergerPartnerTab> partnerTab = Optional.empty();



    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        LOG.debug("creating 'create' buerger form");
        if(!createForm.isPresent()){
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, EventType.SAVE);
            createForm = Optional.of(form);}
        return createForm.get();
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        if(!createChildForm.isPresent()){
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, EventType.SAVE_CHILD);
            createChildForm=Optional.of(form);}
        return createChildForm.get();
    }

    /**
     * Erzeugt eine neue Instanz eines "Child" Tabs.
     *
     * @param navigateToForDetail Zielseite um sich die Details des 'Child' Objektes anzeigen zu lassen
     * @param navigateForCreate Zielseite um ein neues 'Child' Objekt zu erstellen
     * @param from Ausgangsseite zu der zurück navigiert werden soll
     * @return {@link TabSheet.Tab} das Tab
     */
    public BuergerChildTab generateChildTab(String navigateToForDetail, String navigateForCreate, String from) {
        if(!childTab.isPresent())
        {
            BuergerChildTab tab = new BuergerChildTab(controller, navigateToForDetail, navigateForCreate, from);
            getEventBus().register(tab);
            childTab = Optional.of(tab);}
        return childTab.get();
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String from) {
        if(!partnerTab.isPresent())
        {
            BuergerPartnerTab tab = new BuergerPartnerTab(controller, navigateToForDetail, navigateForCreate, navigateForAdd, from);
            getEventBus().register(tab);
            partnerTab = Optional.of(tab);}
        return partnerTab.get();
    }

    /**
     *Erzeugt eine Create Child Form.
     * @return BuergerCreateForm
     */
    public BuergerCreateForm generateCreateChildForm() {
        return this.generateCreateChildForm(controller.popFrom());
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String from) {
        LOG.debug("creating 'update' buerger form");
        if(!updateForm.isPresent()){
            BuergerUpdateForm form = new BuergerUpdateForm(controller, navigateTo, controller.popFrom(), from);
            getEventBus().register(form);
            getEventBus().post(new BuergerComponentEvent(controller.getCurrent(), EventType.SELECT2UPDATE));

            updateForm=Optional.of(form);}
        return updateForm.get();
    }

    public BuergerUpdateForm generateUpdateForm(String from) {

        return this.generateUpdateForm(controller.peekFrom(), from);
    }

    public BuergerReadForm generateReadForm(String navigateToUpdate, String from) {
        LOG.debug("creating 'read' buerger form");
        if(!readForm.isPresent()){
            BuergerReadForm form = new BuergerReadForm(controller, navigateToUpdate, controller.peekFrom(), from);
            getEventBus().register(form);
            readForm=Optional.of(form);}
        getEventBus().post(new BuergerComponentEvent(controller.getCurrent(), EventType.SELECT2READ));
        return readForm.get();
    }

    public BuergerSearchTable generateSearchTable(String navigateToForEdit, String navigateToForDetail, String navigateForCreate, String navigateFrom, String navigateToForHistory) {
        LOG.debug("creating 'search' table for buerger");
        if(!searchTable.isPresent()){
            LOG.debug("new searchtabel");
            TableActionButton.Builder detail = TableActionButton.Builder.<Buerger>make(controller, TableAction.tabledetail,navigateToForDetail,(container,id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2READ).navigateTo(navigateToForDetail).from(navigateFrom))
            );
            TableActionButton.Builder edit = TableActionButton.Builder.<Buerger>make(controller, TableAction.tableedit, navigateToForEdit, (container, id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2UPDATE).navigateTo(navigateToForEdit).from(navigateFrom))
            );
            TableActionButton.Builder copy = TableActionButton.Builder.<Buerger>make(controller, TableAction.tablecopy,null, (container, id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id),id,EventType.COPY))
            );
            TableActionButton.Builder delete = TableActionButton.Builder.<Buerger>make(controller, TableAction.tabledelete,navigateToForEdit, (container, id) ->
                    {
                        BeanItem<Buerger> item = container.getItem(id);
                        GenericConfirmationWindow win = new GenericConfirmationWindow(new BuergerAppEvent(item, id, EventType.DELETE), controller, SimpleAction.delete);
                        controller.getNavigator().getUI().addWindow(win);
                        win.center();
                        win.focus();
                    }
                            //getEventBus().post(new BuergerAppEvent(container.getItem(id),id,EventType.DELETE).navigateTo(navigateToForEdit).from(navigateFrom))
            );
            TableActionButton.Builder history = TableActionButton.Builder.<Buerger>make(controller, TableAction.tablehistory,navigateToForHistory, (container, id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id),id, HISTORY).navigateTo(navigateToForHistory).from(navigateFrom))
            );

            searchTable=Optional.of(new BuergerSearchTable(
                    controller,
                    navigateForCreate,
                    navigateFrom,
                    // Schaltflächen
                    detail,
                    edit,
                    copy,
                    delete,
                    history
            ));}
        return searchTable.get();}


    public HistoryForm generateHistoryTable(String navigateFrom){
        LOG.error("creating 'history' buerger form");
        if(!historyForm.isPresent()){
            HistoryForm form = new HistoryForm(controller, navigateFrom);
            historyForm=Optional.of(form);}
        return historyForm.get();
        }

    public ChildSearchTable generateChildSearchTable( String navigateFrom) {


        if(!childSearchTable.isPresent()){
            //BuergerTableButtonFactory detail = BuergerTableButtonFactory.getFactory(navigateToForDetail, BuergerTableDetailButton.class);
            TableActionButton.Builder select = TableActionButton.Builder.<Buerger>make(controller, TableAction.tableadd,null, (container, id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SAVE_AS_CHILD))
            );
            LOG.debug("creating 'search' table for buerger");
            childSearchTable = Optional.of(new ChildSearchTable(
                    controller,
                    navigateFrom,
                    // Schaltflächen
                    select
            ));}
        return childSearchTable.get();

    }
    public BuergerPartnerSearchTable generateBuergerPartnerSearchTable(String navigateFrom) {


        if(!partnerSearchTable.isPresent()){
            //BuergerTableButtonFactory detail = BuergerTableButtonFactory.getFactory(navigateToForDetail, BuergerTableDetailButton.class);
            TableActionButton.Builder select = TableActionButton.Builder.<Buerger>make(controller, TableAction.tableadd, null, (container, id) ->
                            getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SAVE_AS_PARTNER))
            );
            LOG.debug("creating 'partnerSearch' table for buerger");
            partnerSearchTable = Optional.of(new BuergerPartnerSearchTable(
                    controller,
                    navigateFrom,
                    // Schaltflächen
                    select
            ));}
        return partnerSearchTable.get();

    }

    public ChildTable generateChildTable(String navigateToForDetail, String from) {

        TableActionButton.Builder detail = TableActionButton.Builder.<Buerger>make(controller, TableAction.tabledetail, navigateToForDetail, (container, id) ->
                        getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2READ).navigateTo(navigateToForDetail).from(from))
        );

        TableActionButton.Builder delete = TableActionButton.Builder.<Buerger>make(controller, TableAction.tabledelete,navigateToForDetail, (container, id) ->
                {
                    BeanItem<Buerger> item = container.getItem(id);
                    GenericConfirmationWindow win = new GenericConfirmationWindow(new BuergerAppEvent(container.getItem(id), id, EventType.RELEASE_PARENT), controller, SimpleAction.release);
                    controller.getNavigator().getUI().addWindow(win);
                    win.center();
                    win.focus();
                }
        );

        LOG.debug("creating table for childs");
        ChildTable table = new ChildTable(controller, detail, delete);

        table.setFrom(from);
        List<Buerger> entities = controller.queryKinder(controller.getCurrent().getBean());
        getEventBus().register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.QUERY_CHILD);
        event.addEntities(entities);
        getEventBus().post(event);


        return table;
    }

    public PartnerTable generatePartnerTable(String navigateToForDetail, String from) {

        TableActionButton.Builder detail = TableActionButton.Builder.<Buerger>make(controller, TableAction.tabledetail, navigateToForDetail, (container, id) ->
                        getEventBus().post(new BuergerAppEvent(container.getItem(id), id, EventType.SELECT2READ).navigateTo(navigateToForDetail).from(from))
        );
        LOG.debug("creating table for partner");
        PartnerTable table = new PartnerTable(controller, detail);

        table.setFrom(from);
        List<Buerger> entities = controller.queryPartner(controller.getCurrent().getBean());
        getEventBus().register(table);
        BuergerComponentEvent event = new BuergerComponentEvent(EventType.QUERY_PARTNER);
        event.addEntities(entities);
        getEventBus().post(event);


        return table;
    }

    public GenericTable generateTable(String from, final TableActionButton.Builder... buttonBuilders) {
        return this.createTable(from, controller.queryBuerger(), buttonBuilders);
    }

    private GenericTable createTable(String from, List<Buerger> entities, final TableActionButton.Builder... buttonBuilders) {
        LOG.debug("creating table for buerger");
        GenericTable table = new BuergerTable(controller, buttonBuilders);

        table.setFrom(from);

        getEventBus().register(table);

        return table;
    }
    
    private EventBus getEventBus(){
        return controller.getBus();
    }

    public BuergerViewController getController() {
        return controller;
    }

    public void setController(BuergerViewController controller) {
        this.controller = controller;
    }

    @Subscribe
    public void refresh(RefreshEvent event){
        LOG.debug("RefreshEvent received");
        searchTable = Optional.<BuergerSearchTable>empty();
        childSearchTable = Optional.empty();
        childTab = Optional.empty();
        createForm = Optional.empty();
        createChildForm = Optional.empty();
        updateForm = Optional.empty();
        readForm = Optional.empty();
        historyForm = Optional.empty();

        //Work around to reload the current page for the changes to take effect.
        String old = controller.getNavigator().getState();
        controller.getNavigator().navigateTo(MainView.NAME);
        controller.getNavigator().navigateTo(old);
    }
}
