//Chi Ngo
//cngongoc

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SearchView{

	ComboBox<String> gameComboBox = new ComboBox<>(); //shows drop down for filtering the tableView data
	TextField searchTextField = new TextField();  //for entering search letters
	TableView<Score> searchTableView = new TableView<>();  //displays data from scores.csv
	Callback<CellDataFeatures<Score,String>,ObservableValue<String>> gameNameCallBack;
	Callback<CellDataFeatures<Score,String>,ObservableValue<String>> scoreCallBack;
	
	/**setupView() sets up the GUI components
	 * for Search functionality
	 */
	void setupView() {
		
		VBox searchVBox = new VBox();  //searchVBox contains searchLabel and searchHBox
		Text searchLabel = new Text("Search");
		searchVBox.getChildren().add(searchLabel);

		HBox searchHBox = new HBox();  //searchHBox contain gameComboBox and searchTextField
		searchHBox.getChildren().add(gameComboBox);
		searchHBox.getChildren().add(new Text("Search letters"));
		searchHBox.getChildren().add(searchTextField);
		searchVBox.getChildren().add(searchHBox);
		
		searchLabel.setStyle( "-fx-font: 30px Tahoma;" + 
				" -fx-fill: linear-gradient(from 0% 50% to 50% 100%, repeat, lightgreen 0%, lightblue 50%);" +
				" -fx-stroke: gray;" +
				" -fx-background-color: gray;" +
				" -fx-stroke-width: 1;") ;
		searchHBox.setPrefSize(WordNerd.GAME_SCENE_WIDTH, WordNerd.GAME_SCENE_HEIGHT / 3);
		ObservableList<String> options = FXCollections.observableArrayList("All games","Hangman","Twister");
		gameComboBox.setPromptText("All games");
		gameComboBox.setItems(options);
		gameComboBox.setPrefWidth(200);
		
		searchTextField.setPrefWidth(300);
		searchHBox.setAlignment(Pos.CENTER);
		searchVBox.setAlignment(Pos.CENTER);
		searchHBox.setSpacing(10);
		
		setupSearchTableView();
		
		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setTop(searchVBox);
		WordNerd.root.setCenter(searchTableView);
		WordNerd.root.setBottom(WordNerd.exitButton);
	}

	@SuppressWarnings("unchecked")
	void setupSearchTableView() {
		//set up new columns
		TableColumn<Score, String> gameCol = new TableColumn<>("Game");
        gameNameCallBack = new Callback<CellDataFeatures<Score, String>, ObservableValue<String>> () {
			
			@Override
			//update game column to appropriate name
			public ObservableValue<String> call(CellDataFeatures<Score, String> param) {
				if (param.getValue().getGameId() == 0) {
					return (new SimpleStringProperty("Hangman"));
				}
				else if (param.getValue().getGameId() == 1) {
					return (new SimpleStringProperty("Twister"));
				}
				return null;
			}
		};
		gameCol.setCellValueFactory(gameNameCallBack);
		
		//set up puzzleWord column
        TableColumn<Score, String> wordCol = new TableColumn<>("Word");
        wordCol.setCellValueFactory(new PropertyValueFactory<Score, String>("puzzleWord"));
        
        //set up timeStamp column
        TableColumn<Score, Integer> timeCol = new TableColumn<>("Time (sec)");
        timeCol.setCellValueFactory(new PropertyValueFactory<Score, Integer>("timeStamp"));

        //set up score column
        TableColumn<Score, String> scoreCol = new TableColumn<>("Score");
        scoreCallBack = new Callback<CellDataFeatures<Score, String>, ObservableValue<String>> () {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Score, String> param) {
				//format score to 2 decimal places
				String score = String.format("%.2f", param.getValue().getScore());
				return (new SimpleStringProperty(score));
			}
		};
        scoreCol.setCellValueFactory(scoreCallBack);
        
        //add the columns to the table
        searchTableView.getColumns().setAll(gameCol, wordCol, timeCol, scoreCol);
		searchTableView.getColumns().get(0).setPrefWidth(170);
		searchTableView.getColumns().get(1).setPrefWidth(170);
		searchTableView.getColumns().get(2).setPrefWidth(170);
		searchTableView.getColumns().get(3).setPrefWidth(170);
	}
}
