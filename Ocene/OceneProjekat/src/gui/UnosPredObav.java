package gui;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Predispitnaobaveza;
import model.Predmet;

import slikeKlasa.Images;

public class UnosPredObav extends Application {
	
	private Label lbl1 = new Label("Izaberite predmet");
	private Label lbl2 = new Label("Unesite naziv predispitne obaveze");
	private Label lbl3 = new Label("Unesite bodove");
	private Label lbl4 = new Label("Datum održavanja predispitne obaveze");
	
	private ComboBox<Predmet> comboP = new ComboBox<>();
	
	private TextField nazPO = new TextField();
	private TextField bodovi = new TextField();
	
	private DatePicker datum = new DatePicker();
	
	private Button potvrdi = new Button("Unesi predispitnu obavezu", Images.getImageUnesi());
	private Button zatvori = new Button("Zatvori", Images.getImageZatvori());
	
	private Stage pozornica = new Stage();
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.pozornica = stage;
		
		postaviVelicinu();
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackgroundKLKIspit());
		
		bp.setCenter(initGui());
		
		zatvori.setOnAction(this::zatvaranje);
		potvrdi.setOnAction(this::upis);
		
		Scene s = new Scene(bp);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setTitle("Unos predispitne obaveze");
		stage.show();
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		gp.setHgap(15); 	gp.setVgap(15);
		gp.setPadding(new Insets(20));
		
		gp.add(lbl1, 0, 0);
		
		getPredmets();
		comboP.getSelectionModel().select(0);
		
		gp.add(comboP, 1, 0);
		
		gp.add(lbl2, 0, 1);
		gp.add(nazPO, 1, 1);
		
		gp.add(lbl3, 0, 2);
		gp.add(bodovi, 1, 2);
		
		gp.add(lbl4, 0, 3);
		gp.add(datum, 1, 3);
		
		gp.add(new Label("|"), 2, 0);
		gp.add(new Label("|"), 2, 1);
		gp.add(new Label("|"), 2, 2);
		gp.add(new Label("|"), 2, 3);
		
		gp.add(potvrdi, 3, 0);
		gp.add(zatvori, 3, 3);
		
		return gp;
	}
	
	private void getPredmets() {
		
		List<Predmet> pred = Controller.getSubjects();
		
		ObservableList<Predmet> predmeti = comboP.getItems();
		
		pred.forEach(e -> {
			predmeti.add(e);
		});
	
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
		
		if("".equals(nazPO.getText())) {
			 
			nazPO.setStyle("-fx-background-color: #d63c3c;"
					   + "-fx-text-fill: white;");
			
			nazPO.setPromptText("Obavezno polje!");
		} 
		
		if("".equals(bodovi.getText())) {
			
			bodovi.setStyle("-fx-background-color: #d63c3c;"
					   + "-fx-text-fill: white;");
			
			bodovi.setPromptText("Obavezno polje!");
			
		}
		
		nazPO.setOnKeyTyped(e -> {
			
			nazPO.setStyle("-fx-background-color: white;"
					   + "-fx-text-fill: black;");
			
			if("".equals(nazPO.getText())) {
				
				nazPO.setStyle("-fx-background-color: #d63c3c;"
						   + "-fx-text-fill: white;");
				
				nazPO.setPromptText("Obavezno polje!");
			}
		});
		
		bodovi.setOnKeyTyped(e -> {
			
			bodovi.setStyle("-fx-background-color: white;"
					   + "-fx-text-fill: black;");
			
			if("".equals(bodovi.getText())) {
				
				bodovi.setStyle("-fx-background-color: #d63c3c;"
						   + "-fx-text-fill: white;");
				
				bodovi.setPromptText("Obavezno polje!");
			}
		});
		
	}
	
	private boolean provera() {
		
		if("".equals(bodovi.getText()) || "".equals(nazPO.getText())) {
			return false;
		}
		
		return true;
	}
	
	private void upisi() {
		
		double bod;
		
		Predmet p = comboP.getSelectionModel().getSelectedItem();
		
		String nazP = nazPO.getText();
		
		try {
			
			bod = Double.parseDouble(bodovi.getText());
			
		} catch(NumberFormatException nfe) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Greška");
			a.setHeaderText("Bodovi moraju biti numerièki podatak");
			a.initModality(Modality.APPLICATION_MODAL);
			a.showAndWait();
			return;
		}
		
		LocalDate ld = datum.getValue();
		
		if(ld == null) {
			ButtonType da = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
			ButtonType ne = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
			Alert a = new Alert(Alert.AlertType.WARNING);
			a.setTitle("Upozorenje");
			a.setHeaderText("Niste uneli datum predispitne obaveze. Ipak nastaviti?");
			a.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
			a.getButtonTypes().add(da);
			a.getButtonTypes().add(ne);
			a.initModality(Modality.APPLICATION_MODAL);
		
			Optional<ButtonType> btn = a.showAndWait();
			
			if(btn.isPresent() && btn.get() == da) {
				zavrsiUpisivanje(p, nazP, bod, ld);
				return;
			} else {
				a.close();
				return;
			}
		} else {
			
			zavrsiUpisivanje(p, nazP, bod, ld);
			return;
		}
	}
	
	private void zavrsiUpisivanje(Predmet p, String nazP, double bod, LocalDate ld) {
		
		Predispitnaobaveza po = new Predispitnaobaveza();
		
		if(ld == null) {
		
			po.setNazPredObav(nazP);
			po.setBrBodova(bod);
			po.setPredmet(p);
			po.setDatum(null);
			
		} else {
			
			Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
			po.setNazPredObav(nazP);
			po.setBrBodova(bod);
			po.setPredmet(p);
			po.setDatum(date);
			
		}
		
		if(Controller.insertColloquim(po) && Controller.connectSubjectColloquim(p, po)) {
			
			Alert ale = new Alert(Alert.AlertType.INFORMATION);
			ale.setTitle("Upisivanje");
			ale.setHeaderText("Uspešno ste dodali predispitnu obavezu za izabrani predmet!");
			ale.initModality(Modality.APPLICATION_MODAL);
			ale.showAndWait();
			pozornica.close();
			return;
		}
	}
	
	private void postaviVelicinu() {
		
		potvrdi.setPrefSize(200, 30);
		zatvori.setPrefSize(200, 30);
		
		potvrdi.setStyle("-fx-background-color: #4a53ff; "
					   + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-color: blue;"
				       + "-fx-border-color: yellow;");
		
		zatvori.setStyle("-fx-cursor: hand;"
					   + "-fx-background-color: lightblue;"
					   + "-fx-border-style: solid;");
		
		comboP.setStyle("-fx-cursor: hand;");
		datum.setStyle("-fx-cursor: hand;");
		
		lbl1.setStyle("-fx-text-fill: black;"
				    + "-fx-font-weight: bold;");
	
		lbl2.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");
	
		lbl3.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");
	
		lbl4.setStyle("-fx-text-fill: black;"
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
	
}
