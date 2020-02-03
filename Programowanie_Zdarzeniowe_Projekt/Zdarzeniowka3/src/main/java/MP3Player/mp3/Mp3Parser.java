package MP3Player.mp3;

import MP3Player.dbConnection.Song;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp3Parser {
    public static Song createMp3Song(File file) throws IOException, TagException {
        MP3File mp3File = new MP3File(file);
        String absolutePath = file.getAbsolutePath();
        String title = mp3File.getID3v2Tag().getSongTitle();
        String author = mp3File.getID3v2Tag().getLeadArtist();
        return new Song(title, author, absolutePath);
    }

    public static List<Song> createMp3List(File dir) throws IOException, TagException {
        if(!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }
        List<Song> songList = new ArrayList<>();
        File[] files = dir.listFiles();
        for(File f: files) {
            String fileExtension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
            if(fileExtension.equals("mp3"))
                songList.add(createMp3Song(f));
        }

        return songList;
    }
}