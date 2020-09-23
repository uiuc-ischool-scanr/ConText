/*
 
* Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
* Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
* Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
* Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang..     
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
package context.core.task.erinferengine;

import context.core.util.JavaIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aale
 */
public class AnnotatedWords {

    int index;
    String word;
    String type;

    /**
     *
     * @param index
     * @param word
     * @param type
     */
    public AnnotatedWords(int index, String word, String type) {
        this.index = index;
        this.word = word;
        this.type = type;
    }

    /**
     *
     * @param file
     * @return
     */
    public static List<AnnotatedWords> readV2File(String file) {
        List<AnnotatedWords> list = new ArrayList<>();
        String str = null;
        try {
            str = JavaIO.readFile(new File(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (str != null) {
            String[] lines = str.split("\\n");
            for (String l : lines) {
                String[] tokens = l.split("\\t");
                int _index = Integer.parseInt(tokens[0]);
                String _word = tokens[1];
                String _type = tokens[3];
                AnnotatedWords word = new AnnotatedWords(_index, _word, _type);
                list.add(word);
            }
        }
        return list;
    }
}
