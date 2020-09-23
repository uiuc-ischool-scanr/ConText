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
package context.ui.control.tabular;

import context.core.entity.TabularData;
import java.net.URL;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class TabularViewController extends AnchorPane implements Initializable {

    /**
     *
     */
    public static String path = "/context/ui/control/tabular/TabularView.fxml";

    @FXML
    private Label titleLabel;
    @FXML
    private TableView<List<String>> tableView;
    private TabularData data;
    private boolean roundDoubles;

    /**
     *
     * @return
     */
    public boolean isRoundDoubles() {
        return roundDoubles;
    }

    /**
     *
     * @param roundDoubles
     */
    public void setRoundDoubles(boolean roundDoubles) {
        this.roundDoubles = roundDoubles;
    }

    /**
     *
     * @return
     */
    public TabularData getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(TabularData data) {
        this.data = data;
        initialTableData();
    }

    /**
     *
     * @return
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     *
     * @param columnIndex
     */
    public void setSortedColumn(int columnIndex) {
        if (tableView.getColumns() != null & columnIndex < tableView.getColumns().size()) {
            TableColumn tc = tableView.getColumns().get(columnIndex);
            tc.setSortType(TableColumn.SortType.DESCENDING);
            tableView.getSortOrder().add(tc);
        }
    }

    /**
     * Initializes the controller class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void initialTableData() {
        data.loadTableData();
//        double minWidth = tableView.getWidth() / data.getHeaders().size();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int index = 0;
        for (String header : data.getHeaders()) {
            final int j = index;
            TableColumn tableColumn = new TableColumn(header);

            tableColumn.setComparator(new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    if (NumberUtils.isNumber(s1) && NumberUtils.isNumber(s2)) {
                        return Double.compare(Double.parseDouble(s1), Double.parseDouble(s2));
                    }
                    return Collator.getInstance().compare(s1, s2);
                }
            });
            tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<List<String>, String> p) {
                    final String val = p.getValue().get(j);
                    if (isRoundDoubles() && NumberUtils.isNumber(val) && val.contains(".")) {
                        
                    	//2016.01.26 Julian Edited: Add decimal digits from 2 to 4 digits
                    	DecimalFormat df = new DecimalFormat("#.####");
                        Double d = Double.parseDouble(val);
                        return new SimpleStringProperty(df.format(d));
                    } else {
                        return new SimpleStringProperty(val);
                    }
                }
            });
            index++;
            tableView.getColumns().add(tableColumn);
//            if (index < data.getHeaders().size() - 1) {
//                tableColumn.setMinWidth(minWidth);
//            }
//            System.out.println("width=" + tableColumn.getMinWidth());
        }
        System.out.println("columns Count:" + tableView.getColumns().size());
        //  which will make your table view dynamic 
//        ObservableList<ObservableList> csvData = FXCollections.observableArrayList();
//
//        for (List<StringProperty> dataList : data.getRows()) {
//            ObservableList<String> row = FXCollections.observableArrayList();
//            for (StringProperty rowData : dataList) {
//                row.add(rowData.get());
//            }
//            csvData.add(row); // add each row to cvsData
//        }
        System.out.println("Rows Count=" + data.getRows().size());
        tableView.setItems(data.getRows()); // finally add data to tableview
        System.out.println("after Rows Count=" + tableView.getItems().size());

    }

}
