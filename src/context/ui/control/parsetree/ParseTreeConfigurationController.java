package context.ui.control.parsetree;

import static context.app.AppConfig.getUserDirLoc;
import context.core.util.JavaIO;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.PropertiesUtil;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Aale
 */
public class ParseTreeConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(ParseTreeConfigurationController.class);

    @FXML
    private Label titleLabel;
    @FXML
    private ComboBox<String> aggregationComboBox;
    @FXML
    private VBox gridContainerVBox;

    @FXML
    private CheckBox selectAllCheckBox;

    private CheckBox options[];

    @FXML
    private Font x1;
    @FXML
    private CheckBox advancedCheckBox;
    @FXML
    private Label typedependenciesLabel;

    /**
     *
     * @param title
     */
    @Override
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     *
     * @return
     */
    public int getAggregationType() { // 0 - per document 1- per corpus
        return aggregationComboBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectAllCheckBox.setVisible(false);
        gridContainerVBox.setVisible(false);
        typedependenciesLabel.setVisible(false);
        ObservableList<String> aggregationOptions
                = FXCollections.observableArrayList(
                        "Per Document",
                        "Per Corpus"
                );
        aggregationComboBox.getItems().addAll(aggregationOptions);
        aggregationComboBox.getSelectionModel().select(1);
        final List<String[]> typedDependanciesLabels = getTypedDependanciesLabels();

        gridContainerVBox.getChildren().add(generateTypedDependanciesOptions(typedDependanciesLabels));

        selectAllCheckBox.setTooltip(new Tooltip("Select All/None"));
        selectAllCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (options == null) {
                    return;
                }
                for (int ii = 0; ii < options.length; ii++) {
                    options[ii].setSelected(newValue);
                }
            }
        });
    }

    /**
     * @return the getFilteredLabels
     */
    public Set<String> getFilteredLabels() {
        Set<String> selectedTypes = new HashSet<>();
        if (options == null) {
            return null;
        }
        for (CheckBox option : options) {
            if (option.isSelected()) {
                selectedTypes.add((String) option.getUserData());
            }
        }
        return selectedTypes;
    }

    /**
     *
     * @return
     */
    public List<String[]> getTypedDependanciesLabels() {
        final String filepath = getUserDirLoc() + "/data/parsetree/typed-dependencies-metadata.csv";;
        List<String> lines = new ArrayList<>();
        List<String[]> labels = new ArrayList<>();
        JavaIO.readCSVFileIntoList(lines, new File(filepath), "\n", false);
        for (String l : lines) {
            String split[] = l.split(",");
            String abbr = split[0];
            String name = split[1];
            String label = name + " (" + abbr + ")";
            String[] arr = new String[2];
            arr[0] = label;
            arr[1] = abbr;
            labels.add(arr);
        }
        return labels;
    }

    /**
     *
     * @param labels
     * @return
     */
    public FlowPane generateTypedDependanciesOptions(List<String[]> labels) {
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(860); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        options = new CheckBox[labels.size()];
        for (int i = 0; i < options.length; i++) {
            options[i] = new CheckBox(labels.get(i)[0]);
            options[i].setMinWidth(280);
            options[i].setUserData(labels.get(i)[1]);
//            options[i].setTooltip();
            flow.getChildren().add(options[i]);
        }

        return flow;
    }

    /**
     *
     * @return
     */
    public boolean isAdvanced() {
        if (advancedCheckBox.isSelected()) {
            return true;
        }
        return false;
    }

    @FXML
    private void handleAdvanceCheckBox(ActionEvent event) {
        if (advancedCheckBox.isSelected()) {
            selectAllCheckBox.setVisible(true);
            gridContainerVBox.setVisible(true);
            typedependenciesLabel.setVisible(true);
        } else {
            selectAllCheckBox.setVisible(false);
            gridContainerVBox.setVisible(false);
            typedependenciesLabel.setVisible(false);
        }
    }

}
