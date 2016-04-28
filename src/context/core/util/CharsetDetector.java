/*
 
 * Copyright (c) 2015 University of Illinois Board of Trustees, All rights reserved.   
 * Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Amirhossein Aleyasen,    
 * Chieh-Li Chin, Shubhanshu Mishra, Kiumars Soltani, and Liang Tao.     
 *   
 * This program is free software; you can redistribute it and/or modify it under   
 * the terms of the GNU General Public License as published by the Free Software   
 * Foundation; either version 2 of the License, or any later version.   
 *    
 * This program is distributed in the hope that it will be useful, but WITHOUT   
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or    
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for   
 * more details.   
 *    
 * You should have received a copy of the GNU General Public License along with   
 * this program; if not, see <http://www.gnu.org/licenses>.   
 *
 
 
 */
package context.core.util;

/**
 *
 * @author Aale
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import org.mozilla.universalchardet.UniversalDetector;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class CharsetDetector {

    /**
     *
     * @param filename
     * @return
     */
    public static String detectCharset(String filename) {
        return "UTF8"; //TODO: fix charset detection using new library
        /*
         java.io.FileInputStream fis = null;
         try {
         byte[] buf = new byte[4096];
         fis = new java.io.FileInputStream(filename);
         // (1)
         UniversalDetector detector = new UniversalDetector(null);
         // (2)
         int nread;
         while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
         detector.handleData(buf, 0, nread);
         }   // (3)
         detector.dataEnd();
         // (4)
         String encoding = detector.getDetectedCharset();
         // (5)
         detector.reset();
         if (encoding != null) {
         return encoding;
         } else {
         return null;
         }
         } catch (FileNotFoundException ex) {
         Exceptions.printStackTrace(ex);
         } catch (IOException ex) {
         Exceptions.printStackTrace(ex);
         } finally {
         try {
         fis.close();
         } catch (IOException ex) {
         Exceptions.printStackTrace(ex);
         }
         }
         return null;
         */
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String file = "C:\\Users\\Aale\\DBBACuP2\\Development\\Java-ws\\Context-FX\\data\\test-arabic\\test-arabic\\TEST\\arabictest.txt";
//        System.out.println(detectCharset(file));
        final String output = readFile(file);
        System.out.println("output=" + output);
    }

    /**
     *
     * @param filepath
     * @return
     */
    public static String readFile(String filepath) {

        try {
            File fileDir = new File(filepath);
            String encoding = detectCharset(filepath);
            if (encoding == null) {
                encoding = "UTF8";
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
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
