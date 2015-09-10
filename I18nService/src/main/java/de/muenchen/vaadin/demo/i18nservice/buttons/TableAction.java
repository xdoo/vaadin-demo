package de.muenchen.vaadin.demo.i18nservice.buttons;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;
import de.muenchen.vaadin.demo.i18nservice.ControllerContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a Simple enum for styled actions in a table.
 *
 * @author p.mueller
 * @version 1.0
 */
public enum TableAction implements Action{

    tablecopy(FontAwesome.COPY),
    tabledelete(FontAwesome.TRASH_O, ValoTheme.BUTTON_DANGER),
    tabledetail(FontAwesome.FILE_O),
    tableedit(FontAwesome.PENCIL),
    tableadd(FontAwesome.PLUS),
    tablehistory(FontAwesome.ARCHIVE);

    /** The potential empy set of {@link com.vaadin.ui.themes.ValoTheme} style Strings. */
    private final Set<String> styleNames;
    /** The icon for this action. */
    private final FontAwesome icon;

    /**
     * Create a new TableAction with the specified icon and poosible {@link com.vaadin.ui.themes.ValoTheme} styles.
     *
     * @param icon The icon for the action.
     * @param styleNames The style Strings for this action.
     */
    TableAction(FontAwesome icon, String... styleNames) {
        this.styleNames = Stream.of(styleNames).collect(Collectors.toSet());

        // Default icon only skin for all
        this.styleNames.add(ValoTheme.BUTTON_ICON_ONLY);

        this.icon = icon;
    }

    @Override
    public Set<String> getStyleNames() {
        return styleNames;
    }

    @Override
    public Optional<FontAwesome> getIcon() {
        return Optional.ofNullable(icon);
    }

    @Override
    public String getID(String navigateTo, ControllerContext context) {
        return String.format("%s_%s_%s_TABLE_BUTTON", navigateTo,this.name().toUpperCase(), context.getBasePath());
    }
}
