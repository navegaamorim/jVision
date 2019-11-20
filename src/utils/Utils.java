package utils;

import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

/**
 * @author 8130031
 */
public class Utils {

    private static String FILE_NAME_N_FACES = "resources/trainingset/num_faces.txt";


    /**
     * Convert a object Mat (OpenCV) to Image(JavaFX)
     *
     * @param frame - current OpenCV frame
     * @return objecto Image(JavaFX)
     */
    public static Image matToImage(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }


    /**
     * Convert a {@link BufferedImage} to {@link Mat})
     *
     * @param bi - buffered image
     * @return - Mat object
     */
    public static Mat grabFrameFromBuffered(BufferedImage bi) {
        //buffered image to mat
        Mat frame = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        frame.put(0, 0, data);
        return frame;
    }


    public static void saveNumberOfFacesinFile(int num_faces) {
        try {

            String str = num_faces + "";

            FileWriter fw = new FileWriter(new File(FILE_NAME_N_FACES));
            fw.write(str);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readNumberOfFacesinFile() {
        try {

            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_N_FACES));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String s = sb.toString();



            return Integer.parseInt(s.replaceAll("\\r\\n|\\r|\\n", ""));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }




}
