package MP3Player.dbConnection;

import com.mysql.cj.jdbc.Blob;

import javax.persistence.*;


@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSong;

    @Column
    private String title;
    @Column
    private String author;

    public String getPath() {
        return path;
    }
    public Song(){

    }
    public Song(String title,String author,String path)
    {
        this.title=title;
        this.author=author;
        this.path = path;
    }

    @Column
    private String path;

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
