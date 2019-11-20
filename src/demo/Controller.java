package demo;

import enums.EnumColor;
import enums.EnumObjectTarget;
import enums.EnumShape;
import interfaces.Detection;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import jvision.*;
import models.ColorObject;
import models.SourceCamera;
import utils.CSVUtils;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Controller {

    @FXML
    private ImageView originalFrame, testFrame;

    //private ScheduledExecutorService droneControlService;

    @FXML
    protected void startCamera() {


        JVisionDetection vision = JVisionDetection.getInstance();

        vision.setBehaviour(new ImageViewerBot(originalFrame, testFrame));

        List<Detection> detections = new ArrayList<>();
        //detections.add(new DetectionShape(EnumShape.RECTANGLE));
        //detections.add(new DetectionShape(EnumShape.CIRCLE));
        detections.add(new DetectionObject(EnumObjectTarget.FACE));
        //detections.add(new DetectionObject(EnumObjectTarget.EYES));

        //detections.add(new DetectionObject(EnumObjectTarget.EYES));
        //detections.add(new DetectionCircle());
        //detections.add(new DetectionContour(EnumColor.YELLOW));
        //detections.add(new DetectionOpticalFlow());

        vision.setDetections(detections);

        vision.setSource(new SourceCamera(0));
        //vision.setSource(new SourceVideo("/home/navega/Documents/Projeto/video_low.avi"));
        //vision.setSource(new SourceImage("/home/navega/Documents/Projeto/shapes.png"));

        //JVisionConfig visionConfig = new JVisionConfig(new ColorObject(EnumColor.RED), 3);
        //vision.setVisionConfig(visionConfig);

        vision.start();

/*
        JVisionRecognition visionRecognition = JVisionRecognition.getInstance();
        visionRecognition.setBehaviour(new ImageViewerBot(originalFrame, testFrame));
        visionRecognition.setSource(new SourceCamera(0));
        //visionRecognition.setLearnMode("Celeste");
        visionRecognition.setRecognitionMode();
        visionRecognition.start();
*/
    }

}

        //Exemplo de comunicação com um ARDrone, usando a framework Yadrone

        /*IARDrone drone;
        drone = new ARDrone();
        drone.addExceptionListener(new IExceptionListener() {
            public void exeptionOccurred(ARDroneException exc) {
                exc.printStackTrace();
            }
        });

        drone.setHorizontalCamera();
        drone.getCommandManager().setOutdoor(false, false);
        drone.getCommandManager().setMaxAltitude(2000);
        drone.start();

        /*Runnable droneProcess = () -> {

            drone.getVideoManager().addImageListener(new ImageListener() {
                public void imageUpdated(BufferedImage newImage) {
                    vision.updateSourceBuffered(newImage);
                }
            });
        };

        droneControlService = Executors.newSingleThreadScheduledExecutor();
        droneControlService.scheduleAtFixedRate(droneProcess, 0, 500, TimeUnit.MILLISECONDS);
        */
