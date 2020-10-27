//Chi Ngo
//cngongoc

package hw3;

import java.util.List;
import javafx.scene.chart.LineChart;

public class ScoreController extends WordNerdController{
	ScoreView scoreView;
	
	@Override
	void startController() {
		//Create a new view and clear WordNerd
		scoreView = new ScoreView();
		scoreView.setupView();
		WordNerd.root.getChildren().clear();
		setupBindings();
		
		//Create a new score chart
		ScoreChart sc = new ScoreChart();
		//Read all scores
		wordNerdModel.readScore();
		//Draw new charts
		List<LineChart<Number,Number>> lineChart = sc.drawChart(wordNerdModel.scoreList);
		
		//Add charts to WordNerd
		scoreView.scoreGrid.add(lineChart.get(0), 0, 1);
		scoreView.scoreGrid.add(lineChart.get(1), 0, 2);
		WordNerd.root.setCenter(scoreView.scoreGrid);
	}

	@Override
	void setupBindings() {
		//there was no need for bindings for this program to work properly
	}
}
