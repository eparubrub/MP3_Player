import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

public class SongDatabase {
	
	//two song lists, one for the initial import
	//another for the search import
	private SongList songs = new SongList();
	private SongList searchedSongs = new SongList();
	
	//recursive import method that looks at all the 
	//files in the folder and if it is another folder
	//it calls the method again if not it checks if
	//it is an mp3 file and if it is it uses jaudiotagger
	//to get the artist and title and it stores those
	//strings into the song list along with the path
	public void importFilesAndFolders(Path path) throws CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		if (!Files.isDirectory(path)) {
			System.out.println("Path is not a directory. ");
			return;
		}
		try {
			DirectoryStream<Path> listing = Files.newDirectoryStream(path);
			for (Path file: listing) {
				String fileName = file.getFileName().toString();
				String filePath = file.toString();
				if (Files.isDirectory(file)){
					importFilesAndFolders(file);
				}
				else if (fileName.endsWith(".mp3")){
					AudioFile f = AudioFileIO.read(new File(filePath)); 
					org.jaudiotagger.tag.Tag tag = f.getTag(); 
					String artist = tag.getFirst(FieldKey.ARTIST);
					String title = tag.getFirst(FieldKey.TITLE);
					songs.insertInPlace(filePath, title, artist);
				}
			}
			listing.close();
		} catch (IOException e) {
			System.out.println("Could not get the list of files/subfolders inside this folder");
		}	songs.printNodes();
	}
	
	//imports the searched song into the search list
	//if the songs begin with the selected string 
	public void importSearchedSongs(String s){
		searchedSongs.searchedSongs(songs, s);
	}
	
	//gets the length of the list
	public int getSongListLength(){
		return songs.getListLength();
	}
	
	//gets the length of the searchedSong list
	public int getSearchedSongListLength(){
		return searchedSongs.getListLength();
	}
	
	//gets the data strings from SongList in order to 
	//print the table
	public String[][] getDataValuesFromSongList(){
		return songs.getDataValues();
	}
	
	//gets the path of a song in the list with a given
	//int
	public String getPathFromListForSongs(int songSelected){
		return songs.getPathWithInt(songSelected);
	}
	
	//gets the path of a song in the Searched list with a given
	//int
	public String getPathFromListForSearchedSongs(int songSelected){
		return searchedSongs.getPathWithInt(songSelected);
	}
	
	//gets the data strings from searchedSongsList in order to 
	//print the table
	public String[][] getDataValuesForSearchedValues(){
		return searchedSongs.getDataValues();
	}
	
	//clears the songLists
	public void clearSongDatabaseList(){
		songs.clearList();
		searchedSongs.clearList();
	}
	
	//clears only the searchedSongs list
	public void clearSearchedSongsList(){
		searchedSongs.clearList();
	}
}
