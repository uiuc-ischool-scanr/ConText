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
package context.core.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class EntityCategory {

    Map<Integer, CatData> map = new HashMap<>();

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
     */
    public EntityCategory() {
        String file = "C:\\Users\\Aale\\DBBACuP2\\Development\\Java-ws\\Context-FX\\src\\test\\resources\\thesaurus_category_test.txt";
        String content = null;
        try {
            content = JavaIO.readFile(new File(file));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        String[] lines = content.split("\\n");
        for (String l : lines) {
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
            map.put(id, new CatData(id, superType, type, subType));
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public String getBestType(int id) {
        final CatData cat = map.get(id);
        if (cat.subType != null) {
            return cat.subType;
        }
        if (cat.type != null) {
            return cat.type;
        }
        if (cat.superType != null) {
            return cat.superType;
        }
        return null;
    }
}
