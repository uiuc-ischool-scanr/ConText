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
package context.core.task.erinferengine;

import context.core.util.JavaIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aale
 */
public class StateLabels {

    /**
     *
     */
    public static final String B_NO_ENTITY = "4";

    /**
     *
     */
    public static final String B_END = "0";

    /**
     *
     */
    public static final String B_INSIDE = "1";

    /**
     *
     */
    public static final String B_BEGIN = "2";

    /**
     *
     */
    public static final String B_UNIGRAM = "3";

    /**
     *
     */
    public static final String C_NO_ENTITY = "94";
    Map<String, CatData> map = new HashMap<>();

    class CatData {

        int id;
        String superType;
        String type;
        String subType;

        public CatData(int id, String superType, String type, String subType) {
            this.id = id;
            this.superType = superType;
            this.type = type;
            this.subType = subType;
        }

    }

    /**
     *
     * @param thesaurus_file
     */
    public StateLabels(String thesaurus_file) {
        String content = null;
        try {
            content = JavaIO.readFile(new File(thesaurus_file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String[] lines = content.split("\\n");
        for (String l : lines) {
            if (l.startsWith("#")) {
                continue;
            }
            String[] tokens = l.split("\\t");
            String all = tokens[0];
            Integer id = Integer.parseInt(tokens[1]);
            String superType = null;
            String type = null;
            String subType = null;
            String[] intok = all.split(":");
            if (intok.length >= 1) {
                superType = intok[0];
            }
            if (intok.length >= 2) {
                type = intok[1];
            }
            if (intok.length == 3) {
                subType = intok[2];
            }
            map.put(id + "", new CatData(id, superType, type, subType));
        }
    }

    /**
     *
     * @param typeList
     * @return
     */
    public String getBestLabel(List<String> typeList) {
        for (String type : typeList) {
            if (type.equals(C_NO_ENTITY)) {
                continue;
            }
            final CatData cat = map.get(type);
            if (cat.subType != null && cat.type != null) {
                return cat.subType + " (" + cat.type + ")";
            }
            if (cat.subType != null) {
                return cat.subType;
            }
            if (cat.type != null) {
                return cat.type;
            }
            if (cat.superType != null) {
                return cat.superType;
            }
        }
        return "UNKNOWN";
    }
}
