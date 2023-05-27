package me.bjtmastermind.iconextractor.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.bjtmastermind.iconextractor.Main;
import me.bjtmastermind.iconextractor.Utils;
import me.bjtmastermind.nbt.io.NBTUtil;
import me.bjtmastermind.nbt.io.NamedTag;
import me.bjtmastermind.nbt.tag.CompoundTag;
import me.bjtmastermind.nbt.tag.ListTag;

public class ServerIconExtractorController {
    @FXML
    private ListView<HBox> serverListlst;
    @FXML
    private ScrollBar scrollBar;
    @FXML
    private ImageView serverIconImg;
    @FXML
    private Text serverNameTxt;
    @FXML
    private Button extractBtn;

    public void onSelectServer(MouseEvent event) {
        if (serverListlst.getSelectionModel().selectedItemProperty().isNotNull().get()) {
            int selectedIndex = serverListlst.getSelectionModel().getSelectedIndex();

            setIcon(((ImageView) serverListlst.getItems().get(selectedIndex).getChildren().get(0)).getImage());
            setName(((Text) ((VBox) serverListlst.getItems().get(selectedIndex).getChildren().get(1)).getChildren().get(0)).getText());

            extractBtn.disableProperty().set(false);
        }
    }

    public void setIcon(Image icon) {
        serverIconImg.setImage(icon);
    }

    public void setName(String name) {
        serverNameTxt.setText(name);
    }

    public void importServers(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Server List files (servers.dat)", "servers.dat");
        chooser.getExtensionFilters().add(filter);
        File selectedFile = chooser.showOpenDialog(Window.getScene().getWindow());

        serverListlst.getItems().clear();
        populateServerList(selectedFile.toString());
    }

    public void extractIcon(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File outputPath = chooser.showDialog(Window.getScene().getWindow());
        BufferedImage bi = SwingFXUtils.fromFXImage(serverIconImg.getImage(), null);
        try {
            ImageIO.write(bi, "PNG", new File(outputPath.toString()+File.separator+serverNameTxt.getText()+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateServerList(String servers) {
        try {
            NamedTag namedtag = NBTUtil.read(servers, false);
            CompoundTag root = (CompoundTag) namedtag.getTag();
            ListTag<CompoundTag> serversList = (ListTag<CompoundTag>) root.getListTag("servers");

            int serverCount = serversList.size();
            for (int i = 0; i < serverCount; i++) {
                CompoundTag server = serversList.get(i);

                Image icon = new Image(new ByteArrayInputStream(Base64.getDecoder().decode(server.getString("icon"))));
                Text name = new Text(server.getString("name"));
                Text ip = new Text(server.getString("ip"));

                name.getStyleClass().add("text");
                ip.getStyleClass().add("text");

                HBox hbox = new HBox(5);
                VBox vbox = new VBox(2);

                if (!icon.isError()) {
                    hbox.getChildren().add(new ImageView(icon));
                } else {
                    hbox.getChildren().add(new ImageView(Main.class.getResource("images/no_icon.png").toString()));
                }
                vbox.getChildren().add(name);
                vbox.getChildren().add(ip);

                hbox.getChildren().add(vbox);

                serverListlst.getItems().add(hbox);
            }
        } catch (IOException e) {
            ;
        }
    }

    @FXML
    private void initialize() {
        populateServerList(Utils.getMCPath()+"servers.dat");
    }
}
