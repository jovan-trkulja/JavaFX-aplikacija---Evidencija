package guiUpdate;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import slikeKlasa.Images;

public class PozornicaUpdate extends Application {
	
	private Button updatePredOb = new Button("Ažuriraj podatke o predispitnim obavezama", Images.getImageUpdate());
	private Button updateIspit = new Button("Ažuriraj podatke o ispitu", Images.getImageUpdate());
	private Button updatePredmet = new Button("Ažuriraj podatke o predmetu", Images.getImageUpdate());
	private Button deletePredOb = new Button("Obriši predispitnu obavezu", Images.getImageUpdate());
	private Button close = new Button("Zatvori", Images.getImageZatvori());
	
	private Stage pozornica = new Stage();
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.pozornica = stage;
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackground());
		
		postaviVelicinu();
		
		bp.setCenter(initGui());
		
		close.setOnAction(this::zatvaranje);
		
		updatePredmet.setOnAction(e -> {
			try {
				
				Stage pozornica = new Stage();
				UpdatePredmet up = new UpdatePredmet();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		updatePredOb.setOnAction(e -> {
			try {
				
				Stage pozornica = new Stage();
				UpdatePredOb up = new UpdatePredOb();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		updateIspit.setOnAction(e -> {
			
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setContentText("Izabrana funkcija trenutno nije dostupna!");
			a.initModality(Modality.APPLICATION_MODAL);
			((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
			a.showAndWait();
			return;
			
		});
		
		
		Scene s = new Scene(bp);
		
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setTitle("Ažuriranje podataka");
		stage.show();
		
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		
		gp.setHgap(15);   gp.setVgap(15);
		gp.setPadding(new Insets(20));
		gp.setAlignment(Pos.CENTER);
		
		gp.add(updatePredmet, 0, 0);
		gp.add(updatePredOb, 0, 1);
		gp.add(updateIspit, 0, 2);
		gp.add(deletePredOb, 0, 3);
		gp.add(close, 0, 4);
		
		return gp;
	}
	
	private void zatvaranje(ActionEvent event) {
		
		Alert a = new Alert(Alert.AlertType.CONFIRMATION);
		a.setTitle("Napuštate prozor");
		a.setHeaderText("Sigurno napuštate aplikaciju?");
		a.initModality(Modality.APPLICATION_MODAL);
		((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
		Optional<ButtonType> btn = a.showAndWait();
		
		if(btn.isPresent() && btn.get() == ButtonType.OK) {
			a.close();
			pozornica.close();
		} else {
			a.close();
			return;
		}
		
	}
	
	private void postaviVelicinu() {
		
		updatePredOb.setPrefSize(300, 50);
		updateIspit.setPrefSize(300, 50);
		updatePredmet.setPrefSize(300, 50);
		deletePredOb.setPrefSize(300, 50);
		close.setPrefSize(300, 50);
		
		updatePredOb.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		updateIspit.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		updatePredmet.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		deletePredOb.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		close.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		
		String css = "-fx-background-color: lightblue; -fx-cursor: hand";
		
		updateIspit.setOnMouseEntered(e -> updateIspit.setStyle("-fx-background-color: #eb4034; "
															+ "-fx-cursor: hand;"
															+ "-fx-text-fill: white;"));
		
		updateIspit.setOnMouseExited(e -> updateIspit.setStyle(css));
		
		updatePredmet.setOnMouseEntered(e -> updatePredmet.setStyle("-fx-background-color: #eb4034; "
																  + "-fx-cursor: hand;"
		                                                          + "-fx-text-fill: white;"));

		updatePredmet.setOnMouseExited(e -> updatePredmet.setStyle(css));
		
		updatePredOb.setOnMouseEntered(e -> updatePredOb.setStyle("-fx-background-color: #eb4034; "
															   + "-fx-cursor: hand;"
															   + "-fx-text-fill: white;"));
		updatePredOb.setOnMouseExited(e -> updatePredOb.setStyle(css));
		
		deletePredOb.setOnMouseEntered(e -> deletePredOb.setStyle("-fx-background-color: #eb4034; "
																+ "-fx-cursor: hand;"
																+ "-fx-text-fill: white;"));
		deletePredOb.setOnMouseExited(e -> deletePredOb.setStyle(css));
		
		close.setOnMouseEntered(e -> close.setStyle("-fx-background-color: #eb4034; "
																  + "-fx-cursor: hand;"
																  + "-fx-text-fill: white;"));
		close.setOnMouseExited(e -> close.setStyle(css));

	}
	
}
