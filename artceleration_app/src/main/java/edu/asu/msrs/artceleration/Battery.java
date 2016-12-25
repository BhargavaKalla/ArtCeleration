package edu.asu.msrs.artceleration;

import java.util.ArrayList;

import edu.asu.msrs.artcelerationlibrary.TransformTest;

/**
 * Created by roblkw on 12/8/16.
 */

public class Battery {
    public static int COLOR = 1;//0;
    public static int MOTION = 2;//1;
    public static int ASCII = -1;//2;
    public static int GAUSSIAN = 0;//3;
    public static int TILT = 0;
    public static int UNSHARP = 3;
    public static int SOBEL = 4;
    public static int NEONEDGE = -1;
    public static int PENCIL = 0;
    static String[] transforms = {"GAUSSIAN", "COLOR","MOTION","UNSHARP","SOBEL"};


    public static TransformTest[] getTestsArray() {
        TransformTest[] transforms = new TransformTest[18];
        ArrayList<TransformTest> transformsList = new ArrayList<TransformTest>();
        if (COLOR!=-1) {
            transformsList.add(new TransformTest(COLOR,
                    new int[]{0, 255, 1, 254, 2, 253, 255, 0,
                            0, 255, 1, 254, 2, 253, 255, 0,
                            0, 255, 1, 254, 2, 253, 255, 0,
                    }, new float[]{}));//INVERSION
            transformsList.add(new TransformTest(COLOR,
                    new int[]{0, 0, 1, 1, 2, 2, 255, 255,
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 0, 2, 0, 255, 0,
                    }, new float[]{}));//RED
            transformsList.add(new TransformTest(COLOR,
                    new int[]{0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 1, 2, 2, 255, 255,
                            0, 0, 1, 0, 2, 0, 255, 0,
                    }, new float[]{}));//BLUE
            transformsList.add(new TransformTest(COLOR,
                    new int[]{
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 1, 2, 2, 255, 255,
                    }, new float[]{}));//GREEN
            transformsList.add(new TransformTest(COLOR,
                    new int[]{
                            0, 0, 1, 1, 2, 2, 180, 255,
                            0, 0, 1, 1, 2, 2, 255, 255,
                            0, 0, 1, 1, 2, 2, 255, 255,
                    }, new float[]{}));//REDDEN
        }
        if (MOTION!=-1) {
            transformsList.add(new TransformTest(MOTION, new int[]{0, 3}, new float[]{0.5f}));
            transformsList.add(new TransformTest(MOTION, new int[]{0, 5}, new float[]{0.5f}));
            transformsList.add(new TransformTest(MOTION, new int[]{0, 10}, new float[]{0.5f}));
            transformsList.add(new TransformTest(MOTION, new int[]{1, 3}, new float[]{0.5f}));
            transformsList.add(new TransformTest(MOTION, new int[]{1, 5}, new float[]{0.5f}));
            transformsList.add(new TransformTest(MOTION, new int[]{1, 10}, new float[]{0.5f}));
        }
        if (ASCII!=-1) {
            transformsList.add(new TransformTest(ASCII, new int[]{}, new float[]{}));
        }
        if (GAUSSIAN!=-1) {
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{2}, new float[]{1.0f}));
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{4}, new float[]{2.0f}));
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{6}, new float[]{3.0f}));
        }
        if (UNSHARP!=-1) {
            transformsList.add(new TransformTest(UNSHARP, new int[]{}, new float[]{3f,0.5f}));
            transformsList.add(new TransformTest(UNSHARP, new int[]{}, new float[]{2f,0.25f}));
            transformsList.add(new TransformTest(UNSHARP, new int[]{}, new float[]{1f,0.1f}));
        }

        if (SOBEL!=-1) {
            transformsList.add(new TransformTest(SOBEL, new int[]{0}, new float[]{}));
            transformsList.add(new TransformTest(SOBEL, new int[]{1}, new float[]{}));
            transformsList.add(new TransformTest(SOBEL, new int[]{2}, new float[]{}));
        }
        if (NEONEDGE!=-1) {
            transformsList.add(new TransformTest(NEONEDGE, new int[]{}, new float[]{3f, 3.5f, 0.25f}));
            transformsList.add(new TransformTest(NEONEDGE, new int[]{}, new float[]{2f, 10f, 0.1f}));
            transformsList.add(new TransformTest(NEONEDGE, new int[]{}, new float[]{1f, 5f, 0.9f}));
        }

        return transformsList.toArray(new TransformTest[transformsList.size()]);
    }
    public static TransformTest[] getFailureArray() {
        TransformTest[] transforms = new TransformTest[18];
        ArrayList<TransformTest> transformsList = new ArrayList<TransformTest>();
        if (COLOR!=-1) {
            transformsList.add(new TransformTest(COLOR,
                    new int[]{8, 255, 1, 254, 2, 253, 255, 0,
                            0, 255, 1, 254, 2, 253, 255, 0,
                            0, 255, 1, 254, 2, 253, 255, 0,
                    }, new float[]{}));//increasing pvalue
            transformsList.add(new TransformTest(COLOR,
                    new int[]{0, 0, 1, 1, 2, 2, 255, 255,
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 0, 2, 0, 255, -1,
                    }, new float[]{}));//OOR
            transformsList.add(new TransformTest(COLOR,
                    new int[]{0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 1, 2, 2, 255, 255,
                            0, 0, 1, 0, 2, 0, 255, 0,5
                    }, new float[]{}));//NUMINT
            transformsList.add(new TransformTest(COLOR,
                    new int[]{
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 0, 2, 0, 255, 0,
                            0, 0, 1, 1, 2, 2, 255, 255,
                    }, new float[]{1.0f}));//NUMFLOAT
        }
        if (MOTION!=-1) {
            transformsList.add(new TransformTest(MOTION, new int[]{-2, 3}, new float[]{}));//OOR a0
            transformsList.add(new TransformTest(MOTION, new int[]{0, -5}, new float[]{}));//OOR a1
            transformsList.add(new TransformTest(MOTION, new int[]{0, 10,5}, new float[]{}));//NUMINT
            transformsList.add(new TransformTest(MOTION, new int[]{1, 3}, new float[]{0.5f}));//NUMFLOAT
        }
        if (ASCII!=-1) {
            transformsList.add(new TransformTest(ASCII, new int[]{}, new float[]{1.0f}));//NUMFLOAT
            transformsList.add(new TransformTest(ASCII, new int[]{15}, new float[]{}));//NUMINT
        }
        if (GAUSSIAN!=-1) {
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{-1}, new float[]{1.0f}));//out of range int
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{2}, new float[]{-5f}));//out of range float
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{6,5}, new float[]{-5f}));//wrong number of ints
            transformsList.add(new TransformTest(GAUSSIAN, new int[]{2}, new float[]{5f,65f}));//wrong number of float
        }
        if (UNSHARP!=-1) {
            transformsList.add(new TransformTest(UNSHARP, new int[]{1}, new float[]{3f,5f}));//wrong number of ints
            transformsList.add(new TransformTest(UNSHARP, new int[]{}, new float[]{2f,10f,15f}));//wrong number floats
        }
        if (SOBEL!=-1) {
            transformsList.add(new TransformTest(SOBEL, new int[]{4}, new float[]{}));//out of range int
            transformsList.add(new TransformTest(SOBEL, new int[]{1,2}, new float[]{}));//num int
            transformsList.add(new TransformTest(SOBEL, new int[]{2}, new float[]{5f}));//num float
        }

        if (NEONEDGE!=-1) {
            transformsList.add(new TransformTest(NEONEDGE, new int[]{1}, new float[]{3f, 3.5f, 0.25f}));//num int
            transformsList.add(new TransformTest(NEONEDGE, new int[]{}, new float[]{2f, 10f, 0.1f,5f}));//num float
        }
        return transformsList.toArray(new TransformTest[transformsList.size()]);
    }

    public static String[] getTransformsArray() {
        //String[] transforms = {"GAUSSIAN", "UNSHARP", "COLOR", "SOBEL", "MOTION"};
        return transforms;
    }
}
