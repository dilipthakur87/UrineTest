package com.example.dilip.urinetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    // defining the variables
    private Button btnCapture;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;

    //view holder
    CameraBridgeViewBase cameraBridgeViewBase;

    //camera listener callback
    BaseLoaderCallback baseLoaderCallback;

    //image holder
    public Mat original_image, initial_image;

    // initial contours
    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

    // colorlist for the patches
    ArrayList<Scalar> color_list;

    final Size kernelSize = new Size(7,7);

    PatchClassifier patchClassifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing the variables
        btnCapture = findViewById(R.id.btnTakePicture);
        imgCapture = findViewById(R.id.capturedImage);

        // initializing PatchClassifier class
        patchClassifier = new PatchClassifier();

        // initializing the image holder
//        original_image = new Mat();


        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        // setting onClickListener for the button
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(captureIntent, Image_Capture_Code); //Image_Capture_Code is a locally defined integer that must be greater than 0

                // calling the function to load the image from resources and convert it to Mat
                bitmapToMat();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Image_Capture_Code){
            // if image is captured
            if (resultCode == RESULT_OK){
                //setting the captures image to the image view
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);
//                bitmapToMat(bp);
            }
            else if (resultCode == RESULT_CANCELED) {
                //on cancel
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show();
        }
    }

    // converting bitmap to Mat
    public void bitmapToMat(){

        //initialing the image holder
        initial_image = new Mat();
//        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, initial_image);
//
////        original_image = initial_image;
//        System.out.println("hamro maal = "+ initial_image.channels());
//        System.out.println("hamro maal size = "+ initial_image.size());
//
//        // converting 4 channel image to 3 channel image
//        Imgproc.cvtColor(initial_image, initial_image, Imgproc.COLOR_BGRA2BGR);
//        System.out.println("Converted channel = "+ initial_image.channels() + " type of image = "+ initial_image.type());

        // ---------------------------------------------------------------------------------------------------------------------

        // loading the image from resources and converting it to mat
//        InputStream stream = null;
//        Uri uri = Uri.parse("android.resource://com.example.dilip.urinetest/drawable/strip");
//        try {
//            stream = getContentResolver().openInputStream(uri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//        Bitmap bmp = BitmapFactory.decodeStream(stream, null, bmpFactoryOptions);
////        Mat ImageMat = new Mat();
//        Utils.bitmapToMat(bmp, initial_image);
        // ---------------------------------------------------------------------------------------------------------------------
//        initial_image = Imgcodecs.imread("android.resource://com.example.dilip.urinetest/drawable/strip.jpg");

        // getting imgage from the drawable in Mat format
        try {
            initial_image = Utils.loadResource(this, R.drawable.strip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hamro maal = "+ initial_image.channels());
        System.out.println("hamro maal size = "+ initial_image.size());

        // getting the rectangle
        getrectangle(initial_image);

//        imgCapture.setImageMatrix(initial_image);


    }

    // get rectangle from the Mat image
    public void getrectangle(Mat matImage){

        // saving the matImage
        original_image = new Mat();
        matImage.copyTo(original_image);

        // Defining color range for masking
        Scalar min_white = new Scalar(0, 0, 50);
        Scalar max_white = new Scalar(255, 255, 180);

        // converting BGR to RGB
        Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_BGR2RGB);
        System.out.println("Type here = "+ matImage.type());

        // applying gaussian filter
        Imgproc.GaussianBlur(matImage, matImage, kernelSize, 0);

        // converting to hsv
        Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_RGB2HSV);

        // masking
        Core.inRange(matImage, min_white, max_white, matImage);



        // -----------------------------------------------------------------------------------------
//        // convert to bitmap:
//        Bitmap bm = Bitmap.createBitmap(matImage.cols(), matImage.rows(),Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(matImage, bm);
//
//        // find the imageview and draw it!
//        imgCapture.setImageBitmap(bm);
        // -----------------------------------------------------------------------------------------


        // thresholding
        Imgproc.threshold(matImage, matImage, 127, 255, Imgproc.THRESH_BINARY);

        System.out.println("After threshold = "+ matImage);




        // finding the contour of the thresholded image
        findContour(matImage, original_image);
    }

    // fing contour
    public void findContour(Mat matImage, Mat original_image){

        Imgproc.findContours(matImage, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Rect rectContour = new Rect();

        //finding the biggest contour
        Double biggestContourArea = 0.0;
        for (int i = 0; i < contours.size(); i++) {
            System.out.println("Contour Area = " +Imgproc.contourArea(contours.get(i)));
            if (Imgproc.contourArea(contours.get(i)) > biggestContourArea){
                biggestContourArea = Imgproc.contourArea(contours.get(i));
                rectContour = Imgproc.boundingRect(contours.get(i));
            }
        }

        // overlay the biggest rectangle
        Imgproc.rectangle(original_image, new Point(rectContour.x, rectContour.y), new Point(rectContour.x+rectContour.width, rectContour.y+rectContour.height), new Scalar(0,0,255), 5);


        // getting bounding points of the contour
        int x1 = rectContour.x , y1 = rectContour.y, x2 = rectContour.x+rectContour.width, y2 = rectContour.y+rectContour.height;
        System.out.println("x1 = "+ x1+" y1 = "+y1+" x2 = "+x2+" y2 = "+y2);

        // getting the points of center line of the contour
        int centerx1 = (x1 + x2)/2;
        int centery1 = y1;
        int centerx2 = (x1 + x1 + rectContour.width)/2;
        int centery2 = y1 + rectContour.height;

        // drawing the center line
        Imgproc.line(original_image, new Point(centerx1, centery1), new Point(centerx2, centery2),new Scalar(0,255,0), 5);

        // distance between the pathces
        int length = 80;

        // length of the center line
        int distance = getDistance(centerx1, centery1, centerx2, centery2);
        System.out.println("Distance initially = "+distance);

        // length of a single patch
        double step = distance/14;

        // plotting rectangle in each patch
        plotPatch(length, distance, centerx1, centery1, centerx2, centery2 , step, original_image);
    }

    // getting the length of the center line of the contour
    public int getDistance(int x1, int y1, int x2, int y2){
        //distance betweent the center points
        System.out.println("Point in distance function = "+x1 +","+y1+" and "+x2+","+y2 );
        return (int) Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

    // plotting rectangle on each patch
    public void plotPatch(int length, int distance, int x1, int y1, int x2, int y2, double step, Mat patchImage){
        int new_length = length;
        Point newPoint;
        Mat newCroppedPatch = new Mat();
        color_list = new ArrayList<Scalar>();
        int x=x1,y=y1,xx=x2,yy=y2;
        for (int i = 0 ; i < 11; i++){
            newPoint = pointOnLine(distance, x1, y1, x2, y2, new_length);
            System.out.println("Points on the center line = "+ newPoint);
            x = (int) (newPoint.x-distance/70);
            y = (int) (newPoint.y-distance/70);
            yy = (int) (newPoint.y+distance/70);
            xx = (int) (newPoint.x+distance/70);

            Imgproc.rectangle(patchImage, new Point(x,y), new Point(xx,yy), new Scalar(255,0,0), 3);

            // geting new patch of the plotted image
            patchImage.copyTo(newCroppedPatch);
            Rect roi = new Rect(x, y, xx-x, yy-y);
            Mat cropped = new Mat(newCroppedPatch, roi);

            // adding the extracted colors to the color list
            color_list.add(getMeanRGB(cropped));


            // getting the length of another patch
            new_length = (int) (new_length + step);

            System.out.println("New added color here = "+color_list);

        }

        // -----------------------------------------------------------------------------------------
        // convert to bitmap:
        Bitmap bm = Bitmap.createBitmap(original_image.cols(), original_image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(original_image, bm);

        // find the imageview and draw it!
        imgCapture.setImageBitmap(bm);
        // -----------------------------------------------------------------------------------------

        // processing the identified patch color for classification
        patchClassifier.classifyData(color_list, this);
    }

    //getting points on the center line
    public Point pointOnLine(int distance, int x1, int y1, int x2, int y2, double step_length){

        System.out.println("Step length here = "+step_length);
        System.out.println("Distance = "+distance);
        // getting the step to line ratio
        double ratio = step_length/distance;
        System.out.println("New ratio = "+ratio);

        double xt = (x1*(1-ratio))+(x2*ratio);
        double yt = (y1*(1-ratio))+(y2*ratio);
        System.out.println("New y coordinate = "+(y1*(1-ratio)) + " arko point"+(y2*ratio));

        Point newPoint = new Point(xt, yt);
        System.out.println("Center point here = "+xt+" and "+yt);
        return newPoint;
    }

    // get mean rgb
    public Scalar getMeanRGB(Mat matImage){

        return Core.mean(matImage);
    }



}


