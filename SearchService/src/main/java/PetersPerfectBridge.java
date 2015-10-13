import org.hibernate.search.bridge.StringBridge;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Objects;

/**
 * @author !claus.straube
 */

public class PetersPerfectBridge implements StringBridge {

    /**
     * Apache-Mitarbeieter: "Warum sind wir nicht darauf gekommen?!"!
     *
     * @param object Das objekt.
     * @return Eine string-Repräsentation des Objekts (mit der ultrageheimen toString()-Methode. Magic!)
     */
    @Override
    public String objectToString(Object object) {
        if (object instanceof Date) {
            final LocalDate localDate = ((Date) object).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String build = localDate.toString();
            build += " " + localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
            build += " " + localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
            return build;
        }
        if (Objects.nonNull(object)) {
            return object.toString();
        } else {
            return "";
        }
    }
}