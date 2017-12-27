package sd.ui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppUI implements UI
{
	private final Stage mStage;
	private final Scene mScene;
	private final BorderPane mRoot;
	private final VBox mControlPane;
	private final Property<String> mTitle, mStatus;
	
	public AppUI(Stage primary, Canvas canvas)
	{
		mStage = primary;
		mRoot = new BorderPane();
		mControlPane = new VBox();
		mScene = new Scene(mRoot, 800, 600);
		mTitle = new SimpleStringProperty();
		mStatus = new SimpleStringProperty();
		
		mTitle.setValue("SyncDraw");
		
		mStage.titleProperty().bind(mTitle);
		mStage.setScene(mScene);
		
		StackPane sp = new StackPane();
		
		sp.getChildren().add(canvas);
		canvas.widthProperty().bind(sp.widthProperty());
		canvas.heightProperty().bind(sp.heightProperty());
		
		HBox hbox = new HBox();
		Label status = new Label();
		
		hbox.getChildren().add(status);
		mStatus.addListener((ob, o, n) -> status.setText(mStatus.getValue()));
		
		mRoot.setRight(mControlPane);
		mRoot.setBottom(hbox);
		mRoot.setCenter(sp);
	}
	
	public Property<String> statusProperty() { return mStatus; }
	
	public void registerControl(String name, Runnable action)
	{
		Button btn = new Button(name);
		
		btn.setOnAction(e -> action.run());
		btn.setPrefWidth(80D);
		
		mControlPane.getChildren().add(btn);
	}

	@Override
	public void show()
	{
		mStage.show();
	}
}
