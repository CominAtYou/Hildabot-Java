package com.cominatyou;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Test
    public void shouldAnswerWithTrue()
    {
        int i = 1;
        int index = 0;
        while (i <= 300) {
            System.out.printf("%d, %d\n", i, index);
            if (i == 1) i = 5;
            else if (i >= 5 && i < 50) i += 5;
            else if (i >= 50 && i <= 90) i += 10;
            else i += 20;
            index++;
        }
    }
}
