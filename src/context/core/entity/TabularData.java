package context.core.entity;

import context.core.util.JavaIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.openide.util.Exceptions;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class TabularData extends CorpusData {

    private ObservableList<String> headers;
    private ObservableList<List<String>> rows;

    /**
     *
     * @param name
     * @param path
     */
    public TabularData(StringProperty name, StringProperty path) {
        super(name, path);
        headers = FXCollections.observableArrayList();
        rows = FXCollections.observableArrayList();
    }

    /**
     *
     * @return
     */
    public ObservableList<String> getHeaders() {
        return headers;
    }

    /**
     *
     * @param headers
     */
    public void setHeaders(ObservableList<String> headers) {
        this.headers = headers;
    }

    /**
     *
     * @return
     */
    public ObservableList<List<String>> getRows() {
        return rows;
    }

    /**
     *
     * @return
     */
    public String loadTableData() {
        try {
            headers = FXCollections.observableArrayList();
            rows = FXCollections.observableArrayList();
            System.out.println("path in laodTableData=" + this.getPath().get());
            String result = JavaIO.readFile(new File(this.getPath().get()));
            System.out.println("lenght of results:" + result.length());
            String[] lines = result.split("\\r?\\n");
            System.out.println("lines#:" + lines.length);
            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    headers = parseLine(lines[i]);
                } else {
                    rows.add(parseLine(lines[i]));
                }
            }
            return result;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private ObservableList<String> parseLine(String line) {
        String[] split = line.split(",");
        ObservableList<String> list = FXCollections.observableArrayList();
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        return list;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
