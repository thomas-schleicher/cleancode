package at.aau.cleancode.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LinkValidatorTest {
    private LinkValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LinkValidator();
    }

    @Test
    void testValidHttpLink() {
        assertTrue(validator.isLinkValid("http://example.com"),
                "A simple http URL should be considered valid");
    }

    @Test
    void testValidHttpsLink() {
        assertTrue(validator.isLinkValid("https://www.example.com/path?query=param#fragment"),
                "A complex https URL with path, query and fragment should be valid");
    }

    @Test
    void testInvalidScheme() {
        assertFalse(validator.isLinkValid("ftp://example.com"),
                "Only http and https schemes are accepted");
    }

    @Test
    void testMissingScheme() {
        assertFalse(validator.isLinkValid("www.example.com"),
                "URLs without a scheme (http/https) should be invalid");
    }

    @Test
    void testMalformedUri() {
        // Unbalanced brackets → URISyntaxException
        assertFalse(validator.isLinkValid("http://exa[mple.com"),
                "Malformed URIs should be caught and return false");
    }

    @Test
    void testNullLink() {
        assertFalse(validator.isLinkValid(null),
                "Passing null should not throw, but return false");
    }

    @Test
    void testEmptyString() {
        assertFalse(validator.isLinkValid(""),
                "An empty string is not a valid URL");
    }

    @Test
    void testOnlyProtocol() {
        assertFalse(validator.isLinkValid("http://"),
                "A scheme with no host should be invalid");
    }
}
