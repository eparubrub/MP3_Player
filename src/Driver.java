import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class Driver {
	//main function
	public static void main(String[] args) throws CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		JFrame frame = new JFrame ("Mp3 player");
	    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	    SongDatabase songs  = new SongDatabase();
	    MPlayerPanel panel  = new MPlayerPanel(songs);
	    panel.setPreferredSize(new Dimension(600,400));
	    frame.add (panel);
	    frame.pack();
	    frame.setVisible(true);		
	}
}
