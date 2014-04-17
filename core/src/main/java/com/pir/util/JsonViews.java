package com.pir.util;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonViews {

    // fields included in the "light" version of classes should have this guy
    public static class Light {}

    // API: for anything going to/from the app (ideally, only need to use this, but may need to extend with specific classes if we need to include/not include some
    //      things for specific calls...
    public static class API extends Light{}

}
