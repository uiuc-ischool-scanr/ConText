package context.core.entity;

import context.core.util.JavaIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani A data element consists of a single file
 */
public class FileData extends DataElement {

    private File file;

    /**
     *
     * @param name
     * @param path
     */
    public FileData(StringProperty name, StringProperty path) {
        super(name, path);
        file = new File(path.get());
    }

    /**
     *
     * @param name
     * @param file
     */
    public FileData(StringProperty name, File file) {
        super(name, new SimpleStringProperty(file.getPath()));
        this.file = file;
        this.createFile();
    }

    /**
     *
     * @return
     */
    public File getFile() {
        return this.file;
    }

    /**
     *
     */
    public void createFile() {

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     *
     * @param content
     * @param filepath
     */
    public static void writeDataIntoFile(String content, String filepath) {
        try {
            System.out.println("writeDataIntoFile filepath=" + filepath);
//            FileWriter fw = new FileWriter(filepath, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath, false), "UTF-8"));
            bw.write(content);
            bw.close();
            System.out.println("File saved in " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public String readFileIntoString() { //If it returns null it shows that there was an error
        try {
            return JavaIO.readFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
