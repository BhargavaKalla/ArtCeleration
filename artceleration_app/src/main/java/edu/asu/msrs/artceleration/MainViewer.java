package edu.asu.msrs.artceleration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import edu.asu.msrs.artcelerationlibrary.ArtLib;
import edu.asu.msrs.artcelerationlibrary.TransformHandler;
import edu.asu.msrs.artcelerationlibrary.TransformTest;

public class MainViewer extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }
    String GROUPNAME="RaoKalla";
    private Spinner spinner;
    private TextView status1;
    private TextView status2;
    private ArtView artview;
    private ArtLib artlib;
    private CaptureRequest cm;
    private CameraDevice cd;
    android.hardware.camera2.CaptureRequest cr;
    ArrayList<String> testsArray;
    TransformTest[] tests;
    String[] transforms;
    Bitmap src_img;
    private Bitmap tiny_img;
    private Bitmap small_img;
    String teststring;
    private long testtime;
    int processing=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewer);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        src_img = BitmapFactory.decodeResource(getResources(), R.drawable.asuhayden, opts);
        small_img = BitmapFactory.decodeResource(getResources(), R.drawable.asuhayden_small, opts);
        tiny_img = BitmapFactory.decodeResource(getResources(), R.drawable.asuhayden_tiny, opts);

        spinner = (Spinner) findViewById(R.id.spinner);
        status1 = (TextView) findViewById(R.id.statusText1);
        status2 = (TextView) findViewById(R.id.statusText2);
        artview = (ArtView) findViewById(R.id.artView);

        artlib = new ArtLib(this);

        artlib.registerHandler(new TransformHandler() {
            @Override
            public void onTransformProcessed(Bitmap img_out) {
                //teststring will be invalid for multiGaussian
                long testtime2=System.currentTimeMillis()-testtime;
                Log.d("RequestReceive TimeTest", "Processed!\t"+testtime2+"\t"+img_out.getByteCount()+"\t"+teststring+"\t"+GROUPNAME);
                artview.setTransBmp(img_out);
                artview.postInvalidate();
                processing--;// only useful for runAllTests
            }
        });

        initSpinner();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, testsArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (processing>0){
                    makeToast("Processing! Cannot launch!");
                    return;
                }
                Log.d("RequestReceive TimeTest","clicked");
                if (position==tests.length){
                    artlib.requestTransform(src_img, Battery.GAUSSIAN, new int[]{5}, new float[]{1.0f});
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    artlib.requestTransform(small_img, Battery.GAUSSIAN, new int[]{2}, new float[]{1.0f});
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    artlib.requestTransform(tiny_img, Battery.GAUSSIAN, new int[]{2}, new float[]{1.0f});
                    return;
                }
                if (position==tests.length+1){//failure test
                    Thread thread = new RunAllFailThread();
                    thread.start();
                    return;
                }
                if (position==tests.length+2) {//runAll test
                    Thread thread = new RunAllThread();
                    thread.start();
                    return;
                }
                if (position==tests.length+3) {//runAllMulti test
                    Thread thread = new RunAllMultiThread();
                    thread.start();
                    return;
                }
                TransformTest t = tests[position];
                if (artlib.requestTransform(src_img, t.transformType, t.intArgs, t.floatArgs)){
                    teststring=testsArray.get(position);
                    testtime=System.currentTimeMillis();
                    makeToast("Transform requested : "+ transforms[t.transformType]);
                    Log.d("RequestReceive", "Requested successfully!: "+teststring);

                }else{
                    makeToast("Transform request failed"+ transforms[t.transformType]);
                    Log.d("RequestReceive", "Request Failed!: "+teststring);
                }
            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){

            }
        });

    }

    private void initSpinner() {
        testsArray = new ArrayList<String>();
        tests = Battery.getTestsArray();
        transforms = Battery.getTransformsArray();

        for (TransformTest t : tests){
            String str = transforms[t.transformType] + " : " + Arrays.toString(t.intArgs)+" : "+ Arrays.toString(t.floatArgs);
            testsArray.add(str);
        }
        testsArray.add("multiGaussian");
        testsArray.add("failureTest");
        testsArray.add("allTest");
        testsArray.add("allTestMulti");
    }

    public void makeToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(MainViewer.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class RunAllThread extends Thread {
        RunAllThread() {

        }

        boolean running = true;

        public void run() {
            TransformTest[] testsArray = Battery.getTestsArray();
            Log.d("RequestReceive RunTests","Starting!");
            for (int i = 0; i < 2; i++) {
                for (TransformTest t : testsArray) {
                    processing = 1;
                    teststring = "\t" +transforms[t.transformType] + "\t : " + Arrays.toString(t.intArgs)+" : "+ Arrays.toString(t.floatArgs);
                    testtime=System.currentTimeMillis();
                    makeToast("Transform requested : "+ teststring);

                    artlib.requestTransform(src_img, t.transformType, t.intArgs, t.floatArgs);
                    Log.d("RequestReceive", "Requested successfully!: "+teststring);
                    try {
                        while (processing>0) {
                            Thread.sleep(20);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("RequestReceive RunTests","All done!");
            makeToast("All done!");

        }
    }
    class RunAllMultiThread extends Thread {
        RunAllMultiThread() {

        }

        boolean running = true;

        public void run() {
            TransformTest[] testsArray = Battery.getTestsArray();
            Log.d("RunTestsMulti","Starting!");
            for (int i = 0; i < 3; i++) {
                for (TransformTest t : testsArray) {
                    processing = 3;
                    teststring = "\t" +transforms[t.transformType] + "\t : " + Arrays.toString(t.intArgs)+" : "+ Arrays.toString(t.floatArgs);
                    testtime=System.currentTimeMillis();

                    artlib.requestTransform(src_img, t.transformType, t.intArgs, t.floatArgs);
                    artlib.requestTransform(small_img, t.transformType, t.intArgs, t.floatArgs);
                    artlib.requestTransform(tiny_img, t.transformType, t.intArgs, t.floatArgs);

                    try {
                        while (processing>0) {
                            Thread.sleep(20);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("RunTestsMulti","All done!");
            makeToast("All done!");

        }
    }
    class RunAllFailThread extends Thread {
        RunAllFailThread() {

        }

        boolean running = true;

        public void run() {
            TransformTest[] testsArray = Battery.getFailureArray();
            Log.d("RunTestsFail","Starting!");
            for (int i = 0; i < 1; i++) {
                for (TransformTest t : testsArray) {
                    teststring = "\t" +transforms[t.transformType] + "\t : " + Arrays.toString(t.intArgs)+" : "+ Arrays.toString(t.floatArgs);
                    testtime=System.currentTimeMillis();
                    makeToast("Transform requested : "+ teststring);

                    boolean truefalse = artlib.requestTransform(tiny_img, t.transformType, t.intArgs, t.floatArgs);

                    Log.d("RequestReceive TestFail",truefalse+"\t"+teststring);
                    try {
                        while (processing>0) {
                            Thread.sleep(20);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("RunTestsFail","All done!");
            makeToast("All done!");

        }
    }
}
