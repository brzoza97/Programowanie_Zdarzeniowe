package MP3Player.controller;

import MP3Player.dbConnection.DownloadBlob;
import MP3Player.dbConnection.Song;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.farng.mp3.TagException;
import MP3Player.mp3.Mp3Parser;

import java.io.IOException;

public class ContentPaneController {

    private static final String TITLE_COLUMN = "Tytuł";
    private static final String AUTHOR_COLUMN = "Autor";

    @FXML
    private TableView<Song> contentTable;

    @FXML
    private TableView<Song> dbTable;

    public TableView<Song> getDbTable() {
        return dbTable;
    }

    public TableView<Song> getContentTable() {
        return contentTable;
    }

    public void initialize() {
        configureTableColumns();
    }

    private void configureTableColumns() {
        TableColumn<Song, String> titleColumn = new TableColumn<>(TITLE_COLUMN);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Song, String> authorColumn = new TableColumn<>(AUTHOR_COLUMN);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));


        TableColumn<Song, String> title = new TableColumn<>("Tytuł");
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Song, String> author = new TableColumn<>("Autor");

        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Song,Void> download = new TableColumn("Pobierz");
        Callback<TableColumn<Song,Void>, TableCell<Song,Void>> cellbtn = new Callback<TableColumn<Song, Void>, TableCell<Song, Void>>() {
            @Override
            public TableCell<Song, Void> call(final TableColumn<Song, Void> param) {
                final TableCell<Song, Void> cell = new TableCell<Song, Void>() {

                    private final Button btn = new Button("Pobierz");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Song data = getTableView().getItems().get(getIndex());
                            Task<Void> downloadTask = new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    DownloadBlob.download(data.getTitle());
                                    return null;
                                }
                            };
                            downloadTask.setOnSucceeded(e->{
                                try {
                                    getContentTable().getItems().addAll(Mp3Parser.createMp3List(DownloadBlob.dir));
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                } catch (TagException ex) {
                                    ex.printStackTrace();
                                }
                            });
                            Thread t1 = new Thread(downloadTask);
                            ProgressIndicator progressIndicator = new ProgressIndicator();
                            dbTable.setPlaceholder(progressIndicator);
                            t1.start();

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        download.setCellFactory(cellbtn);
        dbTable.getColumns().add(title);
        dbTable.getColumns().add(author);
        dbTable.getColumns().add(download);
        contentTable.getColumns().add(titleColumn);
        contentTable.getColumns().add(authorColumn);

    }
}