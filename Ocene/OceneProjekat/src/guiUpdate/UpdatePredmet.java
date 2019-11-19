package guiUpdate;

import java.util.List;
import java.util.Optional;

import controller.Controller;
import controller.Wait;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Predmet;
import slikeKlasa.Images;

public class UpdatePredmet extends Application {
	
	private ComboBox<Predmet> predmeti = new ComboBox<>();
	private ComboBox<String> polja = new ComboBox<>();
	
	private TextField unos = new TextField();
	private TextField staro = new TextField();
	
	private Label lbl1 = new Label("Trenutna vrednost za odabranu akciju");
	private Label lbl2 = new Label("Unesite novu vrednost");
	
	private Button potvrdi = new Button("Ažuriraj predmet", Images.getImageUnesi());
	private Button ponisti = new Button("Poništi unose", Images.getImageObrisi());
	private Button izlaz = new Button("Izlaz", Images.getImageZatvori());
	
	private Separator linija = new Separator();
	private Separator linija2 = new Separator();
	
	private Stage stage = new Stage();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.stage = primaryStage;
		
		postaviVelicinu();
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackgroundKLKIspit());
		
		Wait.showDialog();
		bp.setCenter(initGui());
		Wait.closeDialog();
		
		izlaz.setOnAction(this::zatvaranje);
		potvrdi.setOnAction(this::update);
		ponisti.setOnAction(this::delete);
		
		polja.setOnAction(this::getValue);
		predmeti.setOnAction(this::akcija);
		
		Scene s = new Scene(bp);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.setTitle("Ažuriranje predmeta");
		stage.show();
		
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		
		gp.setHgap(10); gp.setVgap(10);
		gp.setPadding(new Insets(15));  gp.setAlignment(Pos.CENTER);
		
		fillComboPredmet();
		predmeti.setPromptText("Izaberite predmet");
		
		fillComboAkcija();
		polja.setPromptText("Izaberite akciju");
		
		gp.add(predmeti, 0, 0);
		gp.add(polja, 1, 0);
		
		gp.add(lbl1, 0, 1);
		gp.add(staro, 1, 1);
		
		gp.add(linija, 0, 3);
		
		gp.add(lbl2, 0, 5);
		gp.add(unos, 1, 5);
		
		gp.add(potvrdi, 2, 0);
		gp.add(ponisti, 1, 3);
		gp.add(izlaz, 2, 5);
		
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
			stage.close();
		} else {
			a.close();
			return;
		}
	}
	
	private void getValue(ActionEvent event) {
		
		Predmet p = predmeti.getSelectionModel().getSelectedItem();
		
		if(p == null) {
			
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setHeaderText("Nije izabran predmet!");
			a.initModality(Modality.APPLICATION_MODAL);
			((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
			a.showAndWait();
			return;
			
		}
		
		int broj;
		String vr;
		
		String kar = polja.getSelectionModel().getSelectedItem();
		char str = kar.charAt(0);
		
		broj = Character.getNumericValue(str);
		
		switch(broj) {
		
		case 1: 
			vr = p.getNazPred();
			staro.setText(vr);
			break;
		case 2:
			int br = p.getGodina();
			staro.setText(String.valueOf(br));	
			break;
		case 3:
			vr = p.getStatus();
			staro.setText(vr);
			break;
		case 4:
			vr = p.getNazProf();
			staro.setText(vr);
			break;
		case 5:
			int br1 = p.getBrEspb();
			staro.setText(String.valueOf(br1));
			break;
		}
		
	}
	
	private void akcija(ActionEvent event) {
		
//		polja.getSelectionModel().select(0);
		polja.setPromptText("Izaberite akciju");
		staro.setText("");
	}
	
	private void update(ActionEvent event) {
		
		Predmet p = predmeti.getSelectionModel().getSelectedItem();
		int broj;
		
		if(p == null) {
			
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setHeaderText("Nije izabran predmet!");
			a.initModality(Modality.APPLICATION_MODAL);
			((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
			a.showAndWait();
			return;
			
		}
		
		String kar = polja.getSelectionModel().getSelectedItem();
		char str = kar.charAt(0);
		
		broj = Character.getNumericValue(str);
		
		if("".equals(unos.getText())) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setHeaderText("Niste uneli novu vrednost");
			a.initModality(Modality.APPLICATION_MODAL);
			((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
			a.showAndWait();
			return;
		}
		
		String vrednost = unos.getText();
		
		switch(broj) {
		
		case 1: 
			
			if(Controller.changeNameSubject(p, vrednost)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predmet za odabranu akciju!");
				ale.initModality(Modality.APPLICATION_MODAL);
				((Stage) ale.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				ale.showAndWait();
				stage.close();
				return;
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
		case 2:
			
			int vr;
			
			try {
				vr = Integer.parseInt(vrednost);
			} catch(NumberFormatException nfe) {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Uneta vrednost mora biti numerièki podatak!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
			if(Controller.changeYearSubject(p, vr)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predmet za odabranu akciju!");
				ale.initModality(Modality.APPLICATION_MODAL);
				((Stage) ale.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				ale.showAndWait();
				stage.close();
				return;
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
		case 3:
			
			if(Controller.changeStatusSubject(p, vrednost)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predmet za odabranu akciju!");
				ale.initModality(Modality.APPLICATION_MODAL);
				((Stage) ale.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				ale.showAndWait();
				stage.close();
				return;
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
		case 4:
			
			if(Controller.changeProfessorSubject(p, vrednost)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predmet za odabranu akciju!");
				ale.initModality(Modality.APPLICATION_MODAL);
				((Stage) ale.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				ale.showAndWait();
				stage.close();
				return;
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
		case 5:
			
			int vr1;
			try {
				vr1 = Integer.parseInt(vrednost);
			} catch(NumberFormatException nfe) {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Uneta vrednost mora biti numerièki podatak!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
			if(Controller.changeESPBSubject(p, vr1)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predmet za odabranu akciju!");
				ale.initModality(Modality.APPLICATION_MODAL);
				((Stage) ale.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				ale.showAndWait();
				stage.close();
				return;
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
				al.showAndWait();
				return;
			}
			
		}
		
	}
	
	private void delete(ActionEvent event) {
		
		staro.setText("");
		unos.setText("");
		
		predmeti.getSelectionModel().select(0);
		polja.getSelectionModel().select(0);
		
	}
	
	private void fillComboPredmet() {
		
		ObservableList<Predmet> obv = predmeti.getItems();
		
		List<Predmet> lista = Controller.getSubjects();
		
		lista.forEach(e -> {
			obv.add(e);
		});
		
	}
	
	private void fillComboAkcija() {
		
		ObservableList<String> obv = polja.getItems();
		
		obv.add("1. Izmena naziva predmeta");
		obv.add("2. Izmena godine izuèavanja");
		obv.add("3. Izmena statusa predmeta");
		obv.add("4. Izmena profesora / asistenta");
		obv.add("5. Izmena ESPB bodova");
		
	}
	
	private void postaviVelicinu() {
		
		linija.setOrientation(Orientation.HORIZONTAL);
		linija.setStyle("-fx-background-color: red");
		
		linija2.setOrientation(Orientation.VERTICAL);
		linija2.setStyle("-fx-background-color: blue");
		
		staro.setEditable(false);
		
		potvrdi.setPrefSize(230, 30);
		izlaz.setPrefSize(230, 30);
		ponisti.setPrefSize(230,30);
		
		staro.setPrefSize(200, 30);
		unos.setPrefSize(200, 30);
		
		potvrdi.setStyle("-fx-background-color: #4a53ff; "
					   + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-color: blue;"
				       + "-fx-border-color: yellow;");
		
		izlaz.setStyle("-fx-cursor: hand;"
					   + "-fx-background-color: lightblue;"
					   + "-fx-border-style: solid;");
		
		ponisti.setStyle("-fx-background-color: #bfbb34;"
			       + "-fx-cursor: hand;"
			       + "-fx-border-style: solid;"
			       + "-fx-text-fill: blue;");
		
		predmeti.setStyle("-fx-cursor: hand;");
		polja.setPrefSize(230, 30);
		predmeti.setPrefHeight(30);
		
		polja.setStyle("-fx-cursor: hand;");
		
		lbl1.setStyle("-fx-text-fill: black;"
				    + "-fx-font-weight: bold;");
	
		lbl2.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");

	}

}
