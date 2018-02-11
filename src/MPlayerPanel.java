import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
/**
 *  The GUI Panel for the MPlayer project
 */

public class MPlayerPanel extends JPanel {

	
	private static final long serialVersionUID = 1L;

	private SongDatabase songDatabase; // the class that contains songs

	// panels
	JPanel topPanel, bottomPanel;
	JScrollPane centerPanel;
	Thread currThread = null;

	// buttons and search box
	JButton playButton, stopButton, exitButton, loadMp3Button;
	JTextField searchBox;
	JButton searchButton;
	int selectedSong = -1;
	JTable table = null;
	private final JFileChooser fc = new JFileChooser();

	MPlayerPanel(SongDatabase songCol) {
		this.songDatabase = songCol;
		this.setLayout(new BorderLayout());
		// Create panels: top, center, bottom
		
		// Create the top panel that has the Load mp3 button, the textfield and
		// the Search button to search for a song
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		// create buttons
		loadMp3Button = new JButton("Load mp3");
		searchBox = new JTextField(5);
		searchButton = new JButton("Search");
		exitButton = new JButton("Exit");
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");

		// add a listener for each button
		loadMp3Button.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());
		playButton.addActionListener(new ButtonListener());
		stopButton.addActionListener(new ButtonListener());
		searchButton.addActionListener(new ButtonListener());

		// add buttons and the textfield to the top panel
		topPanel.add(loadMp3Button);
		topPanel.add(searchBox);
		topPanel.add(searchButton);

		this.add(topPanel, BorderLayout.NORTH); // add the top panel to this
												// panel (MPlayer panel)

		// create the bottom panel and add three buttons to it
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3));
		bottomPanel.add(playButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(exitButton);

		this.add(bottomPanel, BorderLayout.SOUTH);

		// the panel in the center that shows mp3 songs
		centerPanel = new JScrollPane();
		this.add(centerPanel, BorderLayout.CENTER);
		
		// file chooser (opens another window that allows the user to select a folder with files)
		// Set the default directory to the current directory
		fc.setCurrentDirectory(new File("."));

	}

	// An inner listener class for buttons
	class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) throws NullPointerException {

			//button responder for the loadMp3 button.
			//first it clears the Lists of the SongDatabase
			//then it asks the user to load a folder
			//then it loads the folder, makes a table
			//then uploads it to the center panel
			if (e.getSource() == loadMp3Button) {
				System.out.println("Load mp3 button");
				songDatabase.clearSongDatabaseList();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Select a directory with mp3 songs");

				int returnVal = fc.showOpenDialog(MPlayerPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				File dir = fc.getSelectedFile();
				String absolutePath = dir.getAbsolutePath();
				Path path = Paths.get(absolutePath);
				try {
					songDatabase.importFilesAndFolders(path);
				} catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e1) {
					System.out.println("Could not add selected mp3 files");
					e1.printStackTrace();
				}
				remove(centerPanel);
				String columnNames[] = { "Title", "Artist"};
				String dataValues[][] = songDatabase.getDataValuesFromSongList();
				table = new JTable(dataValues, columnNames);
				centerPanel = new JScrollPane(table);
				
				add(centerPanel, BorderLayout.CENTER);
					updateUI();

				}
			}
			
			//play button listener which first checks if there is 
			//a thread already made, if it is playing a song then it
			//stops it then adds a new thread and plays it, if there 
			//is a thread and it is not playing something it makes
			//the new thread. If there is no current thread it makes
			//a new thread. It does this all by seeing if the panel 
			//shows the searched song list or the regular one
			else if (e.getSource() == playButton) {
				try{
				int tableRows = table.getRowCount();
				if (table == null)
					 return;
				else if (tableRows < songDatabase.getSongListLength()){
					if (currThread != null){
						if (currThread.isAlive()){
							currThread.stop();
							selectedSong = table.getSelectedRow();
							currThread = new PlayerThread(songDatabase.getPathFromListForSearchedSongs(selectedSong));
							currThread.start();
						}
						else{
							selectedSong = table.getSelectedRow();
							currThread = new PlayerThread(songDatabase.getPathFromListForSearchedSongs(selectedSong));
							currThread.start();
						}
					}
					else{
						selectedSong = table.getSelectedRow();
						currThread = new PlayerThread(songDatabase.getPathFromListForSearchedSongs(selectedSong));
						currThread.start();
					}
				}
				else if (tableRows == songDatabase.getSongListLength()){
					if (currThread != null){
						if (currThread.isAlive()){
							currThread.stop();
							selectedSong = table.getSelectedRow();
							currThread = new PlayerThread(songDatabase.getPathFromListForSongs(selectedSong));
							currThread.start();
						}
						else{
							selectedSong = table.getSelectedRow();
							currThread = new PlayerThread(songDatabase.getPathFromListForSongs(selectedSong));
							currThread.start();
						}
					}
					else{
						selectedSong = table.getSelectedRow();
						currThread = new PlayerThread(songDatabase.getPathFromListForSongs(selectedSong));
						currThread.start();
					}
				}
				}catch (NullPointerException z){
					System.out.println("nothing in center panel");
				}
			//Stops the thread if the thread is playing something
			} else if (e.getSource() == stopButton) {
				try{
				if (currThread.isAlive()){
					currThread.stop();
				}
				}catch (NullPointerException z){
					System.out.println("nothing in center panel");
				}

			//exits program
			} else if (e.getSource() == exitButton) {
				System.exit(0);
			}

			//makes a new songList based on the search string
			//then it removes the center panel and updates it 
			//with the search results. If the searched string
			//is empty it gives the regular imported list.
			else if (e.getSource() == searchButton) {
				songDatabase.clearSearchedSongsList();
				remove(centerPanel);
				if (!searchBox.getText().equals("")){
					songDatabase.importSearchedSongs(searchBox.getText());
					String columnNames[] = { "Title", "Artist"};
					String dataValues[][] = songDatabase.getDataValuesForSearchedValues();
					table = new JTable(dataValues, columnNames);
					centerPanel = new JScrollPane(table);
					
					add(centerPanel, BorderLayout.CENTER);
						updateUI();
				}
				else{
					String columnNames[] = { "Title", "Artist"};
					String dataValues[][] = songDatabase.getDataValuesFromSongList();
					table = new JTable(dataValues, columnNames);
					centerPanel = new JScrollPane(table);
					
					add(centerPanel, BorderLayout.CENTER);
						updateUI();
				}

			}
		}
	}
}
