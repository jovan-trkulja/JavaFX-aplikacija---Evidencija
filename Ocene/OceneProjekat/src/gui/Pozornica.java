package gui;

import java.util.Optional;

import guiUpdate.PozornicaUpdate;
import javafx.application.Application;
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

public class Pozornica extends Application {
	
	private Button spisakPredmeta = new Button("Spisak predmeta", Images.getImagePredmet());
	private Button unesiPredmet = new Button("Unesi predmet", Images.getImagePredmet());
	private Button unesiBodove = new Button("Unesi kolokvijum", Images.getImageBodovi());
	private Button unesiIspit = new Button("Unesi ispit", Images.getImageBodovi());
	private Button pregledEv = new Button("Tabelarni prikazi", Images.getImagePregled());
	private Button update = new Button("Ažuriraj podatke", Images.getImageUpdate());
	private Button zatvori = new Button("Zatvori", Images.getImageZatvori());
	private Button statistika = new Button("Statistika o predmetu", Images.getImagePregled());

	@Override
	public void start(Stage stage) throws Exception {
		
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackground());
		
		postaviVelicinu();
		
		bp.setCenter(initGui());
		
		zatvori.setOnAction(e -> {
			
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Izlaz");
			a.setHeaderText("Sigurno zatvarate aplikaciju?");
			a.initModality(Modality.APPLICATION_MODAL);
			((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
			Optional<ButtonType> btn = a.showAndWait();
			
			if(btn.isPresent() && btn.get() == ButtonType.OK) {
				a.close();
				stage.close();
			} else {
				a.close();
				return;
			}
			
		});
		
		spisakPredmeta.setOnAction(e -> {
			try {
				
				Stage pozornica2 = new Stage();
				PrikazPredmeta sp = new PrikazPredmeta();
				sp.start(pozornica2);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		unesiPredmet.setOnAction(e -> {
			
			try {
				
				Stage pozornica = new Stage();
				UnosPredmeta up = new UnosPredmeta();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		unesiIspit.setOnAction(e -> {
			
			try {
				
				Stage pozornica = new Stage();
				UnosIspit up = new UnosIspit();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		unesiBodove.setOnAction(e -> {
			
			try {
				
				Stage pozornica = new Stage();
				UnosPredObav up = new UnosPredObav();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		pregledEv.setOnAction(e -> {
			
			try {
				
				Stage pozornica = new Stage();
				Statistike up = new Statistike();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		statistika.setOnAction(e -> {
			try {
				
				Stage pozornica = new Stage();
				StatistikaPredmeta up = new StatistikaPredmeta();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		update.setOnAction(e -> {
			try {
				
				Stage pozornica = new Stage();
				PozornicaUpdate up = new PozornicaUpdate();
				up.start(pozornica);
				
			} catch(Exception ex) {
				ex.printStackTrace();
				return;
			}
		});
		
		Scene s = new Scene(bp, 700, 350);
		stage.setResizable(false);
		stage.setScene(s);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setTitle("Evidencija");
		stage.show();
		
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		gp.setHgap(20);    gp.setVgap(20);
		gp.setAlignment(Pos.CENTER);
		gp.setPadding(new Insets(20));
		
		gp.add(spisakPredmeta, 0, 0);
		gp.add(unesiPredmet, 1, 0);
		gp.add(unesiBodove, 0, 1);
		gp.add(unesiIspit, 1, 1);
		gp.add(pregledEv, 0, 2);
		gp.add(statistika, 1, 2);
		gp.add(update, 0, 3);
		gp.add(zatvori, 1, 3);
		
		return gp;
	}
	
	private void postaviVelicinu() {
		
		spisakPredmeta.setPrefSize(200, 50);
		unesiBodove.setPrefSize(200, 50);
		unesiIspit.setPrefSize(200, 50);
		unesiPredmet.setPrefSize(200, 50);
		pregledEv.setPrefSize(200, 50);
		zatvori.setPrefSize(200, 50);
		update.setPrefSize(200, 50);
		statistika.setPrefSize(200, 50);
		
		zatvori.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		spisakPredmeta.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		unesiBodove.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		unesiIspit.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		unesiPredmet.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		pregledEv.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		update.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		statistika.setStyle("-fx-background-color: lightblue; -fx-cursor: hand");
		
		String css = "-fx-background-color: lightblue; -fx-cursor: hand";
		
		spisakPredmeta.setOnMouseEntered(e -> spisakPredmeta.setStyle("-fx-background-color: #eb4034; "
																	+ "-fx-cursor: hand;"
																	+ "-fx-text-fill: white;"));
		spisakPredmeta.setOnMouseExited(e -> spisakPredmeta.setStyle(css));
		
		update.setOnMouseEntered(e -> update.setStyle("-fx-background-color: #eb4034; "
															+ "-fx-cursor: hand;"
															+ "-fx-text-fill: white;"));
		update.setOnMouseExited(e -> update.setStyle(css));
		
		pregledEv.setOnMouseEntered(e -> pregledEv.setStyle("-fx-background-color: #eb4034; "
															   + "-fx-cursor: hand;"
															   + "-fx-text-fill: white;"));
		pregledEv.setOnMouseExited(e -> pregledEv.setStyle(css));
		
		unesiPredmet.setOnMouseEntered(e -> unesiPredmet.setStyle("-fx-background-color: #eb4034; "
																  + "-fx-cursor: hand;"
																  + "-fx-text-fill: white;"));
		unesiPredmet.setOnMouseExited(e -> unesiPredmet.setStyle(css));
		
		unesiIspit.setOnMouseEntered(e -> unesiIspit.setStyle("-fx-background-color: #eb4034; "
																+ "-fx-cursor: hand;"
																+ "-fx-text-fill: white;"));
		unesiIspit.setOnMouseExited(e -> unesiIspit.setStyle(css));
		
		unesiBodove.setOnMouseEntered(e -> unesiBodove.setStyle("-fx-background-color: #eb4034; "
																 + "-fx-cursor: hand;"
																 + "-fx-text-fill: white;"));
		unesiBodove.setOnMouseExited(e -> unesiBodove.setStyle(css));
		
		statistika.setOnMouseEntered(e -> statistika.setStyle("-fx-background-color: #eb4034; "
																+ "-fx-cursor: hand;"
																+ "-fx-text-fill: white;"));
		statistika.setOnMouseExited(e -> statistika.setStyle(css));
		
		zatvori.setOnMouseEntered(e -> zatvori.setStyle("-fx-background-color: #eb4034; "
															 + "-fx-cursor: hand;"
															 + "-fx-text-fill: white;"));
		zatvori.setOnMouseExited(e -> zatvori.setStyle(css));

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}