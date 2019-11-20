package demo;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;
import interfaces.BehaviourDetection;
import interfaces.BehaviourRecognition;
import javafx.scene.image.ImageView;
import models.FaceRecognized;
import models.OutputJVisionImage;
import utils.CSVUtils;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by navega on 26/07/17.
 */
public class ImageViewerBot implements BehaviourDetection, BehaviourRecognition {

    private ImageView originalImage;
    private ImageView testImage;

    private FileWriter writerResults;
    private int resultsCounter;

    public ImageViewerBot(ImageView image, ImageView test) {
        this.originalImage = image;
        this.testImage = test;

        try {
            writerResults = new FileWriter(CSVUtils.CSV_FILE_PATH + "results_lbph_300.csv", true);
            resultsCounter = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void actionTargetFound(OutputJVisionImage imageOut) {
        testImage.setImage(Utils.matToImage(imageOut.getPosProcessTest()));
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionContinuous(OutputJVisionImage imageOut) {
        originalImage.setImage(Utils.matToImage(imageOut.getFrame()));
        testImage.setImage(Utils.matToImage(imageOut.getPosProcessTest()));

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionRecognition(FaceRecognized faceRecognized) {
        boolean resultado = false;
        if (faceRecognized.getName().equals("Fernando")) {
            resultado = true;
        }

        System.out.println(resultado + " :: " + faceRecognized.getTime() + " :: " + faceRecognized.getConfidence());


        if (resultsCounter < 10) {

            try {
                CSVUtils.saveResults(writerResults, resultado, faceRecognized.getTime(), faceRecognized.getConfidence());
                writerResults.flush();
                ++resultsCounter;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("DONE");
        }


        originalImage.setImage(Utils.matToImage(faceRecognized.getImageOut().getFrame()));
    }

}
