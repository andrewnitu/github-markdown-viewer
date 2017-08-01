package com.andrewnitu.githubmarkdownviewer.utility;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Andrew Nitu on 7/31/2017.
 */
public class ExtractIntFromEndTest {
    @Test
    public void extractEndIntBlank() throws Exception {
        String test = "";

        assertThat(ExtractIntFromEnd.extractEndInt(test) == -1, is(true));
    }

    @Test
    public void extractEndIntOnlyPositiveNumber() throws Exception {
        String test = "12345";

        assertThat(ExtractIntFromEnd.extractEndInt(test) == 12345, is(true));
    }

    @Test
    public void extractEndIntOnlyNegativeNumber() throws Exception {
        String test = "-12345";

        assertThat(ExtractIntFromEnd.extractEndInt(test) == -12345, is(true));
    }

    @Test
    public void extractEndIntWithPositiveNumber() throws Exception {
        String test = "test12345";

        assertThat(ExtractIntFromEnd.extractEndInt(test) == 12345, is(true));
    }

    @Test
    public void extractEndIntWithNegativeNumber() throws Exception {
        String test = "test-12345";

        assertThat(ExtractIntFromEnd.extractEndInt(test) == -12345, is(true));
    }
}