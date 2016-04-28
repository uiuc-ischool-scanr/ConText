package context.core.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class JavaIO {

    /**
     *
     * @param fileDir
     * @return
     * @throws IOException
     */
    public static String readFile(File fileDir) throws IOException {

        try {
            String encoding = CharsetDetector.detectCharset(fileDir.getAbsolutePath());
            if (encoding == null) {
                encoding = "UTF-8";
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(fileDir), encoding));

            String str;
            StringBuilder strbf = new StringBuilder();
            while ((str = in.readLine()) != null) {
//                System.out.println(str);
                strbf.append(str).append("\n");
            }

            in.close();
            return strbf.toString();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile_old(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            System.out.println("Default Charset=" + Charset.defaultCharset());
            final String default_str = Charset.defaultCharset().decode(bb).toString();
            final String utf8_str = Charset.forName(StandardCharsets.UTF_16LE.displayName()).decode(bb).toString();
//            System.out.println("default_str=" + default_str);
            System.out.println("utf8_str= " + utf8_str);
            System.out.println("Done!");
            /* Instead of using default, pass in a decoder. */
            //return Charset.defaultCharset().decode(bb).toString();
            return utf8_str;
        } finally {
            stream.close();
        }
    }

    /**
     *
     * @param result
     * @param file
     * @param delim
     * @param isLowerCase
     * @return
     */
    public static int readCSVFileIntoList(List<String> result, File file, String delim, boolean isLowerCase) {

        String encoding = CharsetDetector.detectCharset(file.getAbsolutePath());
        if (encoding == null) {
            encoding = "UTF-8";
        }
        Scanner read = null;
        try {

            read = new Scanner(new FileInputStream(file), encoding);
            read.useDelimiter(delim);
            String word;

            while (read.hasNext()) {
                word = read.next();
                if (word.equals("")) {
                    continue;
                }
                word = word.replace("\n", "").replace("\r", "").trim();

                if (isLowerCase) {
                    result.add(word.toLowerCase());
                } else {
                    result.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;

        } finally {
            if (read != null) {
                read.close();
            }
        }

        return 1; //It was completed successfully
    }

    /**
     *
     * @param s
     * @param sourceEncoding
     * @param destEncoding
     * @return
     */
    public static String convertEncoding(String s, String sourceEncoding, String destEncoding) {
        String out = null;
        try {
            out = new String(s.getBytes(destEncoding), sourceEncoding);
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    /**
     *
     * @param dir
     */
    public static void mkdirIfnotExist(String dir) {
        File theDir = new File(dir);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + dir);
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }

    /**
     *
     * @param file
     * @return
     */
    public static boolean openDirectory(File file) {
        try {
            if (OSDetector.isWindows()) {
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler",
                    file.getAbsolutePath()});
                return true;
            } else if (OSDetector.isLinux() || OSDetector.isMac()) {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                    file.getAbsolutePath()});
                return true;
            } else {
                // Unknown OS, try with desktop
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    /**
     *
     * @param folder
     * @return
     */
    public static List<File> listFiles(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }

    /**
     *
     * @param content
     * @param filepath
     * @param append
     */
    public static void writeDataIntoFile(String content, String filepath, boolean append) {
        FileWriter fw = null;
        try {
            File file = new File(filepath);
//            System.out.println("writeDataIntoFile filepath=" + file.getAbsolutePath());
            fw = new FileWriter(file, append);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(JavaIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(JavaIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     *
     * @param file
     */
    public static void delete(String file) {
        try {
            File f = new File(file);
            if (!f.delete()) {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
