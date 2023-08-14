package com.cominatyou;

import org.junit.Test;

import static org.junit.Assert.fail;

import com.cominatyou.commands.Submit;

public class SubmitTest {
    @Test
    public void ensureAllowedSubmitChannelsAreSorted() {
        final long[] allowedChannels = Submit.getAllowedChannels();
        for (int i = 0; i < allowedChannels.length - 1; i++) {
            if (allowedChannels[i] > allowedChannels[i + 1]) {
                fail(String.format("Channel ID %d is greater than channel ID %d", allowedChannels[i], allowedChannels[i + 1]));
            }
        }
    }
}
