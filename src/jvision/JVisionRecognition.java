package jvision;

import enums.EnumRecognitionMode;
import exceptions.NoBehaviourDefined;
import exceptions.NoRecognitionModeDefined;
import exceptions.NoSourceDefined;
import exceptions.NotEnoughFacesLearned;
import interfaces.BehaviourRecognition;
import interfaces.Recognition;
import interfaces.Source;
import models.FaceRecognized;
import models.OutputJVisionImage;
import org.opencv.core.*;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * @author 8130031
 *
 * Handles the recognition mode, and controles between recognition mode or learning mode
 * Sends output to {@link BehaviourRecognition}
 *
 */
public class JVisionRecognition extends JVision implements Recognition {

    private static int NO_TRAINDATA_FILE = -1;
    private static int IMAGES_PER_PERSON_TO_LEARN = 300;
    private static String UNKNOWN_FACE = "Unknown";
    private static String DATASET_LOCATION = "resources/trainingset/";
    private static String TRAINDATA_NAME = "traineddata.yml";

    private String trainName;

    private static JVisionRecognition instance = new JVisionRecognition();

    private FaceRecognized faceRecognized;

    private String faceName;
    private int faceIndex;
    private int faceCounter = 0;
    private HashMap<Integer, String> names = new HashMap<>();

    private EnumRecognitionMode recognitionMode = null;

    private boolean isFaceRecognized = false;

    // face cascade classifier
    private CascadeClassifier faceCascade;

    //service
    private ScheduledExecutorService timer;

    private BehaviourRecognition behaviour = null;

    //weka
    private static long time;
    private FileWriter writerSaveTime;


    public JVisionRecognition() {
        //TODO definir localizacao das imagens
        //TODO definir localizacao de traineddata file

        faceCascade = new CascadeClassifier();
        faceCascade.load("resources/haarcascade_frontalface_alt.xml");
    }

    public static JVisionRecognition getInstance() {
        return instance;
    }

    public void setBehaviour(BehaviourRecognition behaviourRecognition) {
        this.behaviour = behaviourRecognition;
    }

    public void setSource(Source source) {
        super.setSource(source);
    }

    public void setVisionConfig(JVisionConfig config) {
        super.setVisionConfig(config);
    }

    public void updateSourceBuffered(BufferedImage bufferedImage) {
        super.updateSourceBuffered(bufferedImage);
    }

    private void loadImages() throws NotEnoughFacesLearned {
        // ler data do traning set
        File root = new File(DATASET_LOCATION);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);
        List<Mat> images = new ArrayList<Mat>();

        Mat labels = new Mat(imageFiles.length, 1, CvType.CV_32SC1);
        int numberOfFacesInFolder = 0;

        for (File image : imageFiles) {
            Mat img = Imgcodecs.imread(image.getAbsolutePath()); // Parse the training set folder files
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(img, img);

            int indexLabel = Integer.parseInt(image.getName().split("\\-")[0]);// extrair label do filename

            String labnname = image.getName().split("\\_")[0];
            String name = labnname.split("\\-")[1];
            names.put(indexLabel, name);
            images.add(img);// Add training set images to images Mat

            labels.put(numberOfFacesInFolder, 0, indexLabel);
            numberOfFacesInFolder++;
        }

        //read yml files in data set location
        FilenameFilter trainFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".yml");
            }
        };
        File[] trainFiles = root.listFiles(trainFilter);
        int numberOfFacesInDataSet = NO_TRAINDATA_FILE;
        if (trainFiles.length > 0) {
            numberOfFacesInDataSet = Integer.parseInt(trainFiles[0].getName().split("\\-")[0]); //read number of faces in traineddata file
        }

        trainName = DATASET_LOCATION + names.size() + "-" + TRAINDATA_NAME;
        boolean minimumFacesRequired = (numberOfFacesInFolder / IMAGES_PER_PERSON_TO_LEARN) >= 2;

        if (minimumFacesRequired) {
            if (numberOfFacesInDataSet == NO_TRAINDATA_FILE) {
                System.out.println("HA FACES SUFICIENTES MAS NAO EXISTE FICHEIRO DE TREINO - TREINAR");
                //train(images, labels);

            } else if (numberOfFacesInDataSet != (numberOfFacesInFolder / IMAGES_PER_PERSON_TO_LEARN)) {
                System.out.println("HA FACES SUFICIENTES MAS O FICHEIRO DE TREINO NÃO ESTA ATUALIZADO - TREINAR");
                //train(images, labels);
            }
        } else {
            throw new NotEnoughFacesLearned("Minimum Faces Required: 2");
        }

        faceIndex = names.size();
    }

    private void train(List<Mat> images, Mat labels) {
        try {
            //FaceRecognizer faceRecognizer = Face.createEigenFaceRecognizer();
            //FaceRecognizer faceRecognizer = Face.createFisherFaceRecognizer();
            FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();//

            //tic();

            faceRecognizer.train(images, labels);
            faceRecognizer.save(trainName);

            //writerSaveTime = new FileWriter(CSVUtils.CSV_FILE_PATH + "lbph_time_per_num_faces.csv", true);
            //CSVUtils.saveTicTocCsv(writerSaveTime, IMAGES_PER_PERSON_TO_LEARN, toc());
            //writerSaveTime.flush();

            System.out.println("TREINO - FINISH");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setLearnMode(String name) {
        recognitionMode = EnumRecognitionMode.LEARN;
        faceName = name;
    }

    @Override
    public void setRecognitionMode() {
        recognitionMode = EnumRecognitionMode.RECOGNITION;
    }


    @Override
    public void start() {
        boolean error = false;
        try {
            this.checkExceptions();
            this.loadImages();

        } catch (NoSourceDefined | NoBehaviourDefined | NoRecognitionModeDefined | NotEnoughFacesLearned e) {
            e.printStackTrace();
            error = true;
        }

        if (!error) {

            super.initSource();

            Runnable frameGrabber = () -> {
                if (!super.getFrame().empty()) {
                    faceRecognized = faceDetectAndTrack(super.getFrame());

                    try {
                        //if (faceRecognized.getImageOut().isTargetsFound()) {
                        behaviour.actionRecognition(faceRecognized);
                        //}

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            };
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(frameGrabber, 0, super.getVisionConfig().getFrameRate(), super.getVisionConfig().getTimeUnit());
        }

    }

    @Override
    public void stop() {
        if (!timer.isShutdown() || !timer.isTerminated()) {
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(super.getVisionConfig().getFrameRate(), super.getVisionConfig().getTimeUnit());
            } catch (InterruptedException e) {
                System.err.println("Exception in stopping the executor service... " + e);
            }
        }
    }

    private FaceRecognized faceDetectAndTrack(Mat frame) {
        if (null != frame) {
            MatOfRect faces = new MatOfRect();
            Mat grayFrame = new Mat();

            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(grayFrame, grayFrame);

            int absoluteFaceSize = 0;
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                absoluteFaceSize = Math.round(height * 0.2f);
            }

            // detect faces
            faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize), new Size());


            String name = "", box_text = ""; //name of future face recognized
            double pos_x, pos_y;
            double prediction = 0;
            double confidence = 0;

            Rect[] facesArray = faces.toArray();
            for (int i = 0; i < facesArray.length; i++) {

                //Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

                Rect rectCrop = new Rect(facesArray[i].tl(), facesArray[i].br());// Crop the detected faces
                Mat croppedImage = new Mat(frame, rectCrop);

                Imgproc.cvtColor(croppedImage, croppedImage, Imgproc.COLOR_BGR2GRAY);// Change to gray scale
                Imgproc.equalizeHist(croppedImage, croppedImage);   // Equalize histogram

                Mat resizeImage = new Mat();// Resize the image to a default size
                Size size = new Size(250, 250);
                Imgproc.resize(croppedImage, resizeImage, size);

                pos_x = frame.width() - 20;
                pos_y = frame.height() - 10;


                if (recognitionMode == EnumRecognitionMode.LEARN) { //learn mode

                    if (faceCounter < IMAGES_PER_PERSON_TO_LEARN) {
                        Imgcodecs.imwrite(DATASET_LOCATION + faceIndex + "-" + faceName + "_" + (faceCounter++) + ".png", resizeImage);
                        box_text = "Saving Images";
                        System.out.println(faceCounter + " - Saving images");
                    }

                } else {//recognize mode

                    tic();
                    double[] returnedResults = faceRecognition(resizeImage);
                    prediction = returnedResults[0];
                    confidence = returnedResults[1];

                    int label = (int) prediction;
                    if (names.containsKey(label)) {
                        name = names.get(label);
                        isFaceRecognized = true;
                    } else {
                        name = UNKNOWN_FACE;
                        isFaceRecognized = false;
                    }

                    box_text = name + " Confidence = " + confidence;
                    // Calculate the position for annotated text (make sure we don't put illegal values in there):

                    pos_x = Math.max(facesArray[i].tl().x - 10, 0);
                    pos_y = Math.max(facesArray[i].tl().y - 10, 0);
                }

                Imgproc.putText(frame, box_text, new Point(pos_x, pos_y), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0), 2);

                //Imgproc.putText(frame, box_text, new Point(15, 12), Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(0, 255, 0, 2.0), 1);
            }

            return new FaceRecognized(name, new OutputJVisionImage(frame, frame, isFaceRecognized), toc(), confidence);
        } else {
            return new FaceRecognized();
        }
    }

    private double[] faceRecognition(Mat currentFace) {
        int[] predLabel = new int[1];        // predict the label
        double[] confidence = new double[1];
        int result = -1;

        //FaceRecognizer faceRecognizer = Face.createEigenFaceRecognizer();//TODO eigenfaces
        //FaceRecognizer faceRecognizer = Face.createFisherFaceRecognizer();//
        FaceRecognizer faceRecognizer = Face.createLBPHFaceRecognizer();//


        faceRecognizer.load(trainName);
        faceRecognizer.predict(currentFace, predLabel, confidence);
        result = predLabel[0];

        return new double[]{result, confidence[0]};
    }

    private void checkExceptions() throws NoSourceDefined, NoBehaviourDefined, NoRecognitionModeDefined {
        if (super.getSource() == null) {
            throw new NoSourceDefined("No Source Image Defined");
        }

        if (behaviour == null) {
            throw new NoBehaviourDefined("No Behaviour Defined");
        }

        if (recognitionMode == null) {
            throw new NoRecognitionModeDefined("No Recognition Mode Defined");
        }
    }

    private static void tic() {
        time = System.currentTimeMillis();
    }

    private long toc() {
        return (System.currentTimeMillis() - time);
    }
}
