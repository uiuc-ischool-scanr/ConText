package context.core.entity;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class CorpusData extends DataElement {

    /**
     *
     */
    protected List<FileData> files;

    /**
     *
     */
    protected File dir;

    /**
     *
     * @param name
     * @param path
     */
    public CorpusData(StringProperty name, StringProperty path) {
        super(name, path);
        dir = new File(path.get());
        files = new ArrayList<FileData>();
    }

    /**
     *
     * @param outputDir
     * @param prefix
     * @param input
     */
    public static void createOutputDirectory(String outputDir, String prefix, CorpusData input) {

        File directory = new File(outputDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File newfile;
        for (int i = 0; i < input.files.size(); i++) {
            try {
                newfile = new File(directory, prefix + "-" + input.files.get(i).getFile().getName());
                newfile.createNewFile();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public int addFile(String name) {
        try {
            File newFile = new File(dir, name);
            if (newFile.exists()) {
                newFile.delete();
            }

            System.out.println("dir=" + dir);
            System.out.println("name=" + name);
            System.out.println("newfile=" + newFile.getAbsolutePath());
            newFile.createNewFile();
            String newName = this.name.get() + "-" + this.files.size();
            files.add(new FileData(new SimpleStringProperty(newName), newFile));
            return files.size() - 1;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return 0;
    }

    /**
     *
     * @param sourceDir
     */
    public void addAllFiles(File sourceDir) {

        if (!sourceDir.isDirectory()) {
            return;
        }

        FileFilter directoryFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        };

        File[] allfiles = dir.listFiles(directoryFilter);

        for (File f : allfiles) {
            if (!f.isDirectory() && !(f.getName().equals("desktop.ini")) && !(f.getName().startsWith("."))) {
                FileData fileData = new FileData(new SimpleStringProperty(name.get() + this.files.size()), f);
                files.add(fileData);
                System.out.println("FileData added: " + fileData.getName().get() + " path=" + fileData.getPath().get());
            }
        }

    }

    /**
     *
     * @return
     */
    public List<FileData> getFiles() {
        return this.files;
    }

    /**
     *
     * @param index
     * @return
     */
    public String readFile(int index) {
        if (index >= this.files.size()) {
            System.out.println("readFile: index is greater than files# ");
            return null;
        }
        return files.get(index).readFileIntoString();
    }

    /**
     *
     * @return
     */
    public String readAllFiles() {
        StringBuilder sb = new StringBuilder();
        System.out.println("files count:" + files.size());
        for (FileData f : files) {
            System.out.println("iterate over files=" + f.getFile().getAbsolutePath());
            String str = f.readFileIntoString();
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     *
     * @param index
     * @param content
     */
    public void writeFile(int index, String content) {
        if (index >= this.files.size()) {
            System.out.println("writeFile: index is greater than files# ");
            return;
        }
        FileData.writeDataIntoFile(content, files.get(index).getPath().get());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
