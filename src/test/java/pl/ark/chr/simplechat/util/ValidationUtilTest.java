package pl.ark.chr.simplechat.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by arek on 24.06.17.
 */
public class ValidationUtilTest {

    @Test
    public void testIsBlank__PassNull() throws Exception {
        //given
        String nil = null;

        //when
        boolean result = ValidationUtil.isBlank(nil);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsBlank__PassEmptyString() throws Exception {
        //given
        String nil = "";

        //when
        boolean result = ValidationUtil.isBlank(nil);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsBlank__PassWhiteSpaces() throws Exception {
        //given
        String nil = "         ";

        //when
        boolean result = ValidationUtil.isBlank(nil);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void testIsBlank__PassWhitespacesWithCharBetween() throws Exception {
        //given
        String nil = "      t           ";

        //when
        boolean result = ValidationUtil.isBlank(nil);

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void testIsBlank__PassContent() throws Exception {
        //given
        String nil = "test";

        //when
        boolean result = ValidationUtil.isBlank(nil);

        //then
        assertThat(result).isFalse();
    }
}