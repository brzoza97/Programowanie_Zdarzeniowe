package MP3Player.controller;

import MP3Player.dbConnection.DownloadBlob;
import MP3Player.dbConnection.InsertMp3;
import MP3Player.dbConnection.Song;
import MP3Player.dbConnection.SongEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import MP3Player.mp3.Mp3Parser;
import MP3Player.player.Mp3Player;

import java.io.File;

public class MainController {
    @FXML
    private ContentPaneController contentPaneController;
    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private MenuPaneController menuPaneController;

    private ObservableList songs;
    private Mp3Player player;


    public void initialize() throws Exception {
        createPlayer();
        configureTableClick();
        configureButtons();
        configureMenu();
    }

    private void createPlayer() {
        ObservableList<Song> items = contentPaneController.getContentTable().getItems();
        player = new Mp3Player(items);
    }

    private void configureTableClick() {
        TableView<Song> contentTable = contentPaneController.getContentTable();
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = contentTable.getSelectionModel().getSelectedIndex();
                playSelectedSong(selectedIndex);
            }
        });
    }

    private void playSelectedSong(int selectedIndex) {
        player.loadSong(selectedIndex);
        configureProgressBar();
        configureVolume();
        controlPaneController.getPlayButton().setSelected(true);
    }

    private void configureProgressBar() {
        Slider progressSlider = controlPaneController.getProgressSlider();
        player.getMediaPlayer().setOnReady(() -> progressSlider.setMax(player.getLoadedSongLength()));

        player.getMediaPlayer().currentTimeProperty().addListener((arg, oldVal, newVal) ->
                progressSlider.setValue(newVal.toSeconds()));

        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(progressSlider.isValueChanging()) {
                player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
            }

        });
    }

    private void configureVolume() {
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        volumeSlider.valueProperty().unbind();
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(player.getMediaPlayer().volumeProperty());
    }

    private void configureButtons() {
        TableView<Song> contentTable = contentPaneController.getContentTable();
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button prevButton = controlPaneController.getPreviousButton();
        Button nextButton = controlPaneController.getNextButton();

        playButton.setOnAction(event -> {
            if (playButton.isSelected()) {
                player.play();
            } else {
                player.stop();
            }
        });

        nextButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });

        prevButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });
    }


    private void configureMenu() throws Exception{
        MenuItem openFile = menuPaneController.getFileMenuItem();
        MenuItem openDir = menuPaneController.getDirMenuItem();
        MenuItem openDb = menuPaneController.getDbMenuItem();
        MenuItem insertIntoDb = menuPaneController.getInsertIntoDb();
        openFile.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
            File file = fc.showOpenDialog(new Stage());
            try {
                contentPaneController.getContentTable().getItems().add(Mp3Parser.createMp3Song(file));
                showMessage("Załadowano plik " + file.getName());
            } catch (Exception e) {
                showMessage("Nie można otworzyć pliku ");
            }
        });

        openDir.setOnAction(event -> {
            DirectoryChooser dc = new DirectoryChooser();
            File dir = dc.showDialog(new Stage());
            try {
                contentPaneController.getContentTable().getItems().addAll(Mp3Parser.createMp3List(dir));
                showMessage("Wczytano dane z folderu " + dir.getName());
            } catch (Exception e) {
                showMessage("Wystąpił błąd podczas odczytu folderu");
            }
        });
        openDb.setOnAction(event->{
            try {
                DirectoryChooser dc = new DirectoryChooser();
                File dir = dc.showDialog(new Stage());
                DownloadBlob.dir = dir;
                DownloadBlob.directory = dir.getAbsolutePath();
            }catch(Exception e)
            {
                showMessage("Wystąpił błąd podczas odczytu folderu");
            }
                Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
                    @Override
                    protected ObservableList<Song> call() throws Exception {
                        SongEntity s = new SongEntity();
                        return s.getSongs();
                    }
                };
                task.setOnSucceeded(e->{
                    if(task.getValue()!=null) {
                        contentPaneController.getDbTable().getItems().addAll(task.getValue());
                    }
                });
                Thread t = new Thread(task);
              ProgressIndicator progressIndicator = new ProgressIndicator();
                contentPaneController.getDbTable().setPlaceholder(progressIndicator);
                t.start();

        });
        insertIntoDb.setOnAction(e->{
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
            File file = fc.showOpenDialog(new Stage());
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String filePath = file.getAbsolutePath();
                    MP3File mp3File = new MP3File(file);
                    String title = mp3File.getID3v2Tag().getSongTitle();
                    String author = mp3File.getID3v2Tag().getLeadArtist();
                    InsertMp3 insertMp3 = new InsertMp3();
                    insertMp3.insert(filePath,author,title);

                    return null;
                }
            };
            task.setOnSucceeded(t->{contentPaneController.getDbTable().getPlaceholder().setVisible(false);});
            Thread t = new Thread(task);
            ProgressIndicator progressIndicator = new ProgressIndicator();
            contentPaneController.getDbTable().setPlaceholder(progressIndicator);
            t.start();
        });
    }
    private void showMessage(String message) {
        controlPaneController.getMessageTextField().setText(message);
    }
}