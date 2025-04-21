package at.aau.cleancode.utility;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkValidator {

    private static final Logger LOGGER = Logger.getLogger(LinkValidator.class.getName());

    public boolean isLinkValid(String link) {
        try {
            URI _ = new URI(link);
        } catch (NullPointerException | URISyntaxException e) {
            LOGGER.log(Level.INFO, "URI syntax exception: {0}", link);
            return false;
        }

        Pattern pattern = Pattern.compile("^(https?)://.*"); //"^(https?):\\/\\/.*"
        Matcher matcher = pattern.matcher(link);

        if (!matcher.matches()) {
            LOGGER.log(Level.INFO, "Link does not adhere to correct format: {0}", link);
            return false;
        }

        return true;
    }
}
