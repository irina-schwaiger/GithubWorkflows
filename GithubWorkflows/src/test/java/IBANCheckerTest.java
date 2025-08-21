import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class IBANCheckerTest {

    @ParameterizedTest
    @CsvSource({
            "DE22790200760027913168, true",
            "DE21790200760027913173, false",
            "DE227902007600279131, false",
            "XX22790200760027913168, false",
            // zusätzliche Robustheitsfälle
            "'DE22 7902 0076 0027 9131 68', true",
            "'de22790200760027913168', true",
            "'DE22790200760027913168!', false",
            "'', false"
    })
    void testIbanValidation(String iban, boolean expectedValid) {
        assertEquals(expectedValid, IBANChecker.validate(iban));
    }
}