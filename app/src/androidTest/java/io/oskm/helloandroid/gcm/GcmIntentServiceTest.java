package io.oskm.helloandroid.gcm;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class GcmIntentServiceTest extends TestCase {

    public void test() {
        List<String> list = new ArrayList<String>();
        list.add("1");
        String[] lists = new String[0];
        lists = list.toArray(lists);

        System.out.print(lists);
    }
}