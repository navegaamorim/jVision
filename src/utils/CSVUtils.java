package utils;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 8130031, 8130038, 8130258
 */
public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final String TABLE_EXTENSION = ".csv";
    public static final String CSV_FILE_PATH = "resources/trainingset/csvs/";

    /**
     * Retorna o caminho para os csvs de um determinado robot
     *
     * @param robotName
     * @return
     */
    public static String getCSVtabletPath(String namespace, String robotName) {
        return CSV_FILE_PATH + namespace + "/" + "precisao_per_round_" + robotName + TABLE_EXTENSION;
    }

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }


    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {
        boolean first = true;
        //default customQuote is empty
        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }


    /**
     * Tempo de treino conforme numero de faces
     *
     * @param num_faces
     * @param time
     */
    public static void saveTicTocCsv(Writer writer, int num_faces, long time) {
        try {
            CSVUtils.writeLine(writer, Arrays.asList(num_faces + "", time + ""));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveResults(Writer writer, boolean result, long time, double confidence) {
        try {
            CSVUtils.writeLine(writer, Arrays.asList(result + "", time + "", confidence + ""));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}