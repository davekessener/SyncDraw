package sd.ui;

import java.util.function.Consumer;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ConnectDialog extends Stage
{
	private AnchorPane mRoot;
	
	public ConnectDialog(Consumer<String> c)
	{
		mRoot = new AnchorPane();
		GridPane gp = new GridPane();
		HBox hbox = new HBox();
		
		TextField tfAddr = new TextField();
		TextField tfPort = new TextField();
		
		Button addBtn = new Button("Connect");
		Button cancelBtn = new Button("Cancel");
		
		addBtn.setOnAction(e -> {
			this.close();
			c.accept(tfAddr.getText() + ":" + tfPort.getText());
		});
		
		cancelBtn.setOnAction(e -> this.close());
		
		addBtn.setPrefWidth(80D);
		cancelBtn.setPrefWidth(80D);
		
		hbox.setSpacing(5D);
		hbox.getChildren().addAll(cancelBtn, addBtn);

		gp.setHgap(5D);
		gp.setVgap(3D);
		gp.addRow(0, new Label("Address"), tfAddr);
		gp.addRow(1, new Label("Port"), tfPort);

		GridPane.setHgrow(tfAddr, Priority.ALWAYS);
		GridPane.setHgrow(tfPort, Priority.ALWAYS);
		
		mRoot.getChildren().addAll(gp, hbox);

		AnchorPane.setBottomAnchor(hbox, 5D);
		AnchorPane.setRightAnchor(hbox, 5D);
		AnchorPane.setTopAnchor(gp, 5D);
		AnchorPane.setLeftAnchor(gp, 5D);
		AnchorPane.setRightAnchor(gp, 5D);
		
		setScene(new Scene(mRoot, 400, 200));
		
		this.showAndWait();
	}
}
