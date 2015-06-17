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
package context.ui.misc;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class CustomGridPane {

    /**
     *
     * @param names
     * @param abbr
     * @param checkboxes
     * @return
     */
    public static GridPane addGridPane(final List<String> names, List<String> abbr, final CheckBox[][] checkboxes) {
        final CheckBox[] columnChbx;
        final CheckBox[] rowChbx;
        final CheckBox allChbx;
        //checkboxes = new CheckBox[names.size()][names.size()];
        columnChbx = new CheckBox[names.size()];
        rowChbx = new CheckBox[names.size()];
        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(0, 0, 0, 0));
        final ColumnConstraints columnConstraints = new ColumnConstraints(30);
        columnConstraints.setHalignment(HPos.CENTER);
        final RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        grid.getColumnConstraints().add(0, columnConstraints);
        grid.getRowConstraints().add(0, rowConstraints);
        grid.getColumnConstraints().add(1, columnConstraints);
        grid.getRowConstraints().add(1, rowConstraints);
        allChbx = new CheckBox();
        allChbx.setSelected(true);
        grid.add(allChbx, 1, 1);
        for (int i = 0; i < names.size(); i++) {
            grid.getColumnConstraints().add(i + 1, columnConstraints);
            final CheckBox cb = new CheckBox();
            cb.setSelected(true);
            columnChbx[i] = cb;
            Tooltip tp = new Tooltip(names.get(i));
            cb.setTooltip(tp);
            grid.add(cb, i + 2, 0);
            Label title = new Label(abbr.get(i));
            Tooltip tooltip = new Tooltip(names.get(i));
            title.setTooltip(tooltip);
            grid.add(title, i + 2, 1);
        }
        for (int i = 0; i < names.size(); i++) {
            grid.getRowConstraints().add(i + 2, rowConstraints);
            final CheckBox cb_all = new CheckBox();
            cb_all.setSelected(true);

            rowChbx[i] = cb_all;
            Tooltip tp_all = new Tooltip(names.get(i));
            cb_all.setTooltip(tp_all);
            grid.add(cb_all, 0, i + 2);
            Label title = new Label(abbr.get(i));
            Tooltip tooltip = new Tooltip(names.get(i));
            title.setTooltip(tooltip);
            grid.add(title, 1, i + 2);
            for (int j = 0; j <= i; j++) {
                final CheckBox cb = new CheckBox();
                cb.setSelected(true);
                checkboxes[j][i] = cb;
                Tooltip tp = new Tooltip(names.get(j) + "-" + names.get(i));
                cb.setTooltip(tp);
                grid.add(cb, j + 2, i + 2);
            }
        }
        for (int i = 0; i < names.size(); i++) {
            final int i_index = i;
            rowChbx[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    for (int ii = 0; ii <= i_index; ii++) {
                        checkboxes[ii][i_index].setSelected(newValue);
                    }
                }
            });
        }

        for (int i = 0; i < names.size(); i++) {
            final int i_index = i;
            columnChbx[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    for (int ii = i_index; ii < names.size(); ii++) {
                        checkboxes[i_index][ii].setSelected(newValue);
                    }
                }
            });
        }

        allChbx.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (int i = 0; i < names.size(); i++) {
                    for (int j = 0; j <= i; j++) {
                        checkboxes[j][i].setSelected(newValue);
                    }
                }
                for (int i = 0; i < names.size(); i++) {
                    rowChbx[i].setSelected(newValue);
                    columnChbx[i].setSelected(newValue);
                }
            }
        });
        return grid;
    }

}
