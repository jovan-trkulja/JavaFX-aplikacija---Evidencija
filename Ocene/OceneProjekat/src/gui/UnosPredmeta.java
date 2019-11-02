package gui;

import java.util.Optional;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Predmet;

import slikeKlasa.Images;

public class UnosPredmeta extends Application {
	
	private Label lbl1 = new Label("Unesite naziv predmeta i godinu izuèavanja");
	private Label lbl2 = new Label("Odaberite status predmeta");
	private Label lbl3 = new Label("Unesite profesora / asistenta");
	private Label lbl4 = new Label("Da li ste položili predmet?");
	private Label lbl5 = new Label("Unesite broj ESPB bodova");
	
	private TextField naz = new TextField();
	private ComboBox<Integer> god = new ComboBox<>();
	private ComboBox<String> status = new ComboBox<>();
	private TextField prof = new TextField();
	private ComboBox<String> polozen = new ComboBox<>();
	private TextField espb = new TextField();
	
	private Button potvrdi = new Button("Unesi predmet u bazu", Images.getImageUnesi());
	private Button obrisi = new Button("Obriši unete podatke", Images.getImageObrisi());
	private Button zatvori = new Button("Zatvori", Images.getImageZatvori());
	
	private Stage pozornica = new Stage();
	
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.pozornica = stage;
		
		postaviVelicinu();
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackgroundPredmet());
		bp.setLeft(initGui());
		
		zatvori.setOnAction(this::zatvaranje);
		obrisi.setOnAction(this::brisanje);
		potvrdi.setOnAction(this::upis);
		
		Scene s = new Scene(bp);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.setTitle("Unos predmeta");
		stage.show();
	}
	
	private GridPane initGui() {
		GridPane gp = new GridPane();
		gp.setHgap(20);   gp.setVgap(20);
		gp.setPadding(new Insets(25));
		
		gp.add(lbl1, 0, 0);
		gp.add(lbl2, 0, 1);
		gp.add(lbl3, 0, 2);
		gp.add(lbl4, 0, 3);
		gp.add(lbl5, 0, 4);
		
		gp.add(naz, 1, 0);
		
		ObservableList<Integer> comboGod = god.getItems();
		comboGod.add(1);
		comboGod.add(2);
		comboGod.add(3);
		comboGod.add(4);
		god.getSelectionModel().select(0);
		
		gp.add(god, 2, 0);
		
		ObservableList<String> comboStatus = status.getItems();
		comboStatus.add("Obavezni");
		comboStatus.add("Izborni");
		status.getSelectionModel().select(0);
		
		gp.add(status, 1, 1);
		gp.add(prof, 1, 2);
		
		ObservableList<String> comboPolozen = polozen.getItems();
		comboPolozen.add("Da");
		comboPolozen.add("Ne");
		polozen.getSelectionModel().select(0);
		
		gp.add(polozen, 1, 3);
		gp.add(espb, 1, 4);
		
		gp.add(potvrdi, 4, 0);
		gp.add(obrisi, 4, 2);
		
		gp.add(zatvori, 4, 4);
		
		gp.add(new Label("|"), 3, 0);
		gp.add(new Label("|"), 3, 1);
		gp.add(new Label("|"), 3, 2);
		gp.add(new Label("|"), 3, 3);
		gp.add(new Label("|"), 3, 4);
		
		return gp;
	}
	
	private void postaviVelicinu() {
		
		potvrdi.setPrefSize(200, 30);
		obrisi.setPrefSize(200, 30);
		zatvori.setPrefSize(200, 30);
		
		potvrdi.setStyle("-fx-background-color: #4a53ff; "
					   + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-color: blue;"
				       + "-fx-border-color: yellow;");
		
		obrisi.setStyle("-fx-background-color: #bfbb34;"
					  + "-fx-cursor: hand;"
					  + "-fx-border-style: solid;"
					  + "-fx-text-fill: blue;");
		
		zatvori.setStyle("-fx-cursor: hand;"
					   + "-fx-background-color: lightblue;"
					   + "-fx-border-style: solid;");
		
		status.setStyle("-fx-cursor: hand;");
		polozen.setStyle("-fx-cursor: hand;");
		
		lbl1.setStyle("-fx-text-fill: black;"
					+ "-fx-font-weight: bold;");
		
		lbl2.setStyle("-fx-text-fill: black;"
				+ "-fx-font-weight: bold;");
		
		lbl3.setStyle("-fx-text-fill: black;"
				+ "-fx-font-weight: bold;");
		
		lbl4.setStyle("-fx-text-fill: black;"
				+ "-fx-font-weight: bold;");
		
		lbl5.setStyle("-fx-text-fill: black;"
				+ "-fx-font-weight: bold;");
	}
	
	private void zatvaranje(ActionEvent event) {
		
		Alert a = new Alert(Alert.AlertType.CONFIRMATION);
		a.setTitle("Napuštate prozor");
		a.setHeaderText("Sigurno napuštate aplikaciju?");
		a.initModality(Modality.APPLICATION_MODAL);
		a.initStyle(StageStyle.UNDECORATED);
		Optional<ButtonType> btn = a.showAndWait();
		
		if(btn.isPresent() && btn.get() == ButtonType.OK) {
			a.close();
			pozornica.close();
		} else {
			a.close();
			return;
		}
	}
	
	private void brisanje(ActionEvent event) {
		
		naz.setText("");
		prof.setText("");
		espb.setText("");
		status.getSelectionModel().select(0);
		polozen.getSelectionModel().select(0);
		god.getSelectionModel().select(0);
	}
	
	private void upis(ActionEvent event) {
		
		if(provera()) {
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Upisivanje");
			a.setHeaderText("Uneti podaci biæe upisani u bazu. Potvrðujem?");
			a.initModality(Modality.APPLICATION_MODAL);
		
			Optional<ButtonType> btn = a.showAndWait();
		
			if(btn.isPresent() && btn.get() == ButtonType.OK) {
				
				upisi();
			}
		} else {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setHeaderText("Niste uneli tražene podatke");
			a.initModality(Modality.APPLICATION_MODAL);
			a.showAndWait();
			oboj();
			return;
		}
		
	}
	
	private void oboj() {
		
		if("".equals(naz.getText())) {
			 
			naz.setStyle("-fx-background-color: #d63c3c;"
					   + "-fx-text-fill: white;");
			
			naz.setPromptText("Obavezno polje!");
		} 
		
		if("".equals(espb.getText())) {
			
			espb.setStyle("-fx-background-color: #d63c3c;"
					   + "-fx-text-fill: white;");
			
			espb.setPromptText("Obavezno polje!");
			
		}
		
		if("".equals(prof.getText())) {
			
			prof.setStyle("-fx-background-color: #d63c3c;"
					   + "-fx-text-fill: white;");
			
			prof.setPromptText("Obavezno polje!");
			
		}
		
		naz.setOnKeyTyped(e -> {
			
			naz.setStyle("-fx-background-color: white;"
					   + "-fx-text-fill: black;");
			
			if("".equals(naz.getText())) {
				
				naz.setStyle("-fx-background-color: #d63c3c;"
						   + "-fx-text-fill: white;");
				
				naz.setPromptText("Obavezno polje!");
			}
		});
		
		prof.setOnKeyTyped(e -> {
			
			prof.setStyle("-fx-background-color: white;"
					   + "-fx-text-fill: black;");
			
			if("".equals(prof.getText())) {
				
				prof.setStyle("-fx-background-color: #d63c3c;"
						   + "-fx-text-fill: white;");
				
				prof.setPromptText("Obavezno polje!");
			}
		});
		
		espb.setOnKeyTyped(e -> {
			
			espb.setStyle("-fx-background-color: white;"
					   + "-fx-text-fill: black;");
			
			if("".equals(espb.getText())) {
				
				espb.setStyle("-fx-background-color: #d63c3c;"
						   + "-fx-text-fill: white;");
				
				espb.setPromptText("Obavezno polje!");
			}
		});
		
	}
	
	private void upisi() {
		
		String nazP = naz.getText();
		String godina = god.getSelectionModel().getSelectedItem().toString();
		
		int god = Integer.parseInt(godina);
		
		String statusP = status.getSelectionModel().getSelectedItem();
		String polozenP = polozen.getSelectionModel().getSelectedItem();
		
		String profesor = prof.getText();
		String bodovi = espb.getText();
		
		int ESPB = Integer.parseInt(bodovi);
		
		Predmet p = new Predmet();
	
		p.setNazPred(nazP);
		p.setGodina(god);
		p.setStatus(statusP);
		p.setBrEspb(ESPB);
		p.setNazProf(profesor);;
		p.setPolozen(polozenP);
		
		boolean vr = Controller.insertSubject(p);
		
		if(vr) {
			
			Alert ale = new Alert(Alert.AlertType.INFORMATION);
			ale.setTitle("Upisivanje");
			ale.setHeaderText("Uspešno ste upisali predmet u bazu!");
			ale.initModality(Modality.APPLICATION_MODAL);
			ale.showAndWait();
			pozornica.close();
			return;
			
		} else {
			Alert al = new Alert(Alert.AlertType.ERROR);
			al.setTitle("Greška");
			al.setHeaderText("Predmet nije upisan u bazu. Došlo je do greške!");
			al.initModality(Modality.APPLICATION_MODAL);
			al.showAndWait();
			return;
		}
	}
	
	private boolean provera() {
		
		if("".equals(naz.getText()) || "".equals(espb.getText()) || "".equals(prof.getText())) {
			return false;
		}
		
		return true;
	}

}
