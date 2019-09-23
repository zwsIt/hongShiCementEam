package com.supcon.mes.hongShiCementEam.module_yhgl;

import com.supcon.mes.module_yhgl.Temp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Temp temp = new Temp();
        temp.a.str = "a";
        temp.b.str = "b";
        try {
            Temp clone = (Temp) temp.clone();
            clone.a.str = "b";
            clone.b.str = "a";
            System.out.println(temp.a.toString()+" "+temp.b.toString());
            System.out.println(clone.a.toString()+" "+clone.b.toString());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}