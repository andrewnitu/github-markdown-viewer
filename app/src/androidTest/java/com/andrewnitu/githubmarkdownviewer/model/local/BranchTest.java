package com.andrewnitu.githubmarkdownviewer.model.local;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Andrew Nitu on 7/17/2017.
 */
public class BranchTest {
    @Test
    public void getName() throws Exception {
        Branch t = new Branch("test");

        assertThat(t.getName().equals("test"), is(true));
    }
}