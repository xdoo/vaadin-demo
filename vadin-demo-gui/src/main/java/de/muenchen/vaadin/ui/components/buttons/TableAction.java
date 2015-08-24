package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by p.mueller on 20.08.15.
 */
public enum TableAction implements Action{

    tablecopy(FontAwesome.COPY),
    tabledelete(FontAwesome.TRASH_O, ValoTheme.BUTTON_DANGER),
    tabledetail(FontAwesome.FILE_O),
    tableedit(FontAwesome.PENCIL);

    private final Set<String> styleNames;
    private final Optional<FontAwesome> icon;

    TableAction(FontAwesome icon, String... styleNames) {
        this.styleNames = Stream.of(styleNames).collect(Collectors.toSet());
        this.styleNames.add(ValoTheme.BUTTON_ICON_ONLY);

        this.icon = Optional.ofNullable(icon);
    }

    @Override
    public Set<String> getStyleNames() {
        return styleNames;
    }

    @Override
    public Optional<FontAwesome> getIcon() {
        return icon;
    }

    @Override
    public String getID(String navigateTo, ControllerContext context) {
        return String.format("%s_%s_%s_TABLE_BUTTON", navigateTo,this.name().toUpperCase(), context.getBasePath());
    }
}
