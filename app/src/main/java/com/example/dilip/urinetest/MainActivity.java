package com.example.dilip.urinetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
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

    final Size kernelSize = new Size(7,7);

    int threshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing the variables
        btnCapture = findViewById(R.id.btnTakePicture);
        imgCapture = findViewById(R.id.capturedImage);


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
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(captureIntent, Image_Capture_Code); //Image_Capture_Code is a locally defined integer that must be greater than 0
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
                bitmapToMat(bp);
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
    public void bitmapToMat(Bitmap bitmap){
        //initialing the image holder
        initial_image = new Mat();
        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, initial_image);
//        original_image = initial_image;
        System.out.println("hamro maal = "+ initial_image.channels());
        System.out.println("hamro maal size = "+ initial_image.size());

        // converting 4 channel image to 3 channel image
        Imgproc.cvtColor(initial_image, initial_image, Imgproc.COLOR_BGRA2BGR);
        System.out.println("Converted channel = "+ initial_image.channels() + " type of image = "+ initial_image.type());

        // getting the rectangle
        getrectangle(initial_image);
    }

    // get rectangle from the Mat image
    public void getrectangle(Mat matImage){

        // saving the matImage
        original_image = new Mat();
        matImage.copyTo(original_image);

        // Defining color range for masking
        Scalar min_white = new Scalar(96, 13, 180);
        Scalar max_white = new Scalar(140, 100, 255);

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

        Rect rect = new Rect();

        //finding the biggest contour
        Double biggestContourArea = 0.0;
        for (int i = 0; i < contours.size(); i++) {
            System.out.println("Contour Area = " +Imgproc.contourArea(contours.get(i)));
            if (Imgproc.contourArea(contours.get(i)) > biggestContourArea){
                biggestContourArea = Imgproc.contourArea(contours.get(i));
                rect = Imgproc.boundingRect(contours.get(i));
            }
        }

        // overlay the biggest rectangle
        Imgproc.rectangle(original_image, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0,0,255), 5);



        // -----------------------------------------------------------------------------------------
        // convert to bitmap:
        Bitmap bm = Bitmap.createBitmap(original_image.cols(), original_image.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(original_image, bm);

        // find the imageview and draw it!
        imgCapture.setImageBitmap(bm);
        // -----------------------------------------------------------------------------------------



    }


}


