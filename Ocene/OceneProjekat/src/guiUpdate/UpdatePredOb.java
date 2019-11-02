package guiUpdate;

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

public class UpdatePredOb extends Application {
	
	private Label lbl1 = new Label("Izaberite predmet");
	private Label lbl2 = new Label("Izaberite predispitnu obavezu");
	private Label lbl3 = new Label("Unesite bodove");
	private Label lbl4 = new Label("Datum održavanja predispitne obaveze");
	private Label lbl5 = new Label("Unesite nove podatke za izabrani kolokvijum");
	
	private ComboBox<Predmet> comboP = new ComboBox<>();
	private ComboBox<Predispitnaobaveza> comboPredOb = new ComboBox<>();

	private TextField bodovi = new TextField();
	
	private DatePicker datum = new DatePicker();
	
	private Button potvrdi = new Button("Ažuriraj predispitnu obavezu", Images.getImageUnesi());
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
		stage.setTitle("Ažuriranje predispitne obaveze");
		stage.show();
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		gp.setHgap(15); 	gp.setVgap(15);
		gp.setPadding(new Insets(10));
		
		gp.add(lbl1, 0, 0);
		
		getPredmets();
		comboP.getSelectionModel().select(0);
		comboP.setOnAction(this::getObav);
		
		//getObav();
		comboPredOb.getSelectionModel().select(0);
		
		gp.add(comboP, 1, 0);
		
		gp.add(lbl2, 0, 1);
		gp.add(comboPredOb, 1, 1);
		
		gp.add(lbl5, 0, 2);
		gp.add(lbl3, 0, 3);
		gp.add(bodovi, 1, 3);
		
		gp.add(lbl4, 0, 4);
		gp.add(datum, 1, 4);
		
		gp.add(new Label("|"), 2, 0);
		gp.add(new Label("|"), 2, 1);
		gp.add(new Label("|"), 2, 2);
		gp.add(new Label("|"), 2, 3);
		gp.add(new Label("|"), 2, 4);
		
		gp.add(potvrdi, 3, 0);
		//gp.add(azz, 3, 2);
		gp.add(zatvori, 3, 4);
		
		return gp;
	}
	
	private void getPredmets() {
		
		List<Predmet> pred = Controller.getSubjects();
		
		ObservableList<Predmet> predmeti = comboP.getItems();
		
		pred.forEach(e -> {
			predmeti.add(e);
		});
	
	}
	
	private void getObav(ActionEvent event) {
		
		Predmet p = comboP.getSelectionModel().getSelectedItem();
		
		List<Predispitnaobaveza> lista = Controller.getColloquiums(p);
		
		ObservableList<Predispitnaobaveza> obaveze = comboPredOb.getItems();
		obaveze.clear();
		
		lista.forEach(e -> {
			obaveze.add(e);
		});
		
		
		comboPredOb.setPromptText("Izaberite predispitnu obavezu");
		
	}
	
	private void upis(ActionEvent event) {
		
		if(provera()) {
			
			Alert a = new Alert(Alert.AlertType.CONFIRMATION);
			a.setTitle("Ažuriranje");
			a.setHeaderText("Uneti podaci biæe ažurirani. Potvrðujem?");
			a.initModality(Modality.APPLICATION_MODAL);
		
			Optional<ButtonType> btn = a.showAndWait();
		
			if(btn.isPresent() && btn.get() == ButtonType.OK) {
				
				upisi();
			}
			
		}
		
	}
	
	private boolean provera() {
		
		if("".equals(bodovi.getText())) {
			return false;
		}
		
		return true;
	}
	
	private void upisi() {
		
		double bod;
		
		Predispitnaobaveza po = comboPredOb.getSelectionModel().getSelectedItem();
		
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
			
			if(Controller.updateColloquim(po, bod)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predispitnu obavezu!");
				ale.initModality(Modality.APPLICATION_MODAL);
				ale.showAndWait();
				pozornica.close();
				return;
				
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				al.showAndWait();
				return;
			}
			
		} else {
			
			Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			if(Controller.updateColloquim(po, date, bod)) {
				
				Alert ale = new Alert(Alert.AlertType.INFORMATION);
				ale.setTitle("Ažuriranje");
				ale.setHeaderText("Uspešno ste ažurirali predispitnu obavezu!");
				ale.initModality(Modality.APPLICATION_MODAL);
				ale.showAndWait();
				pozornica.close();
				return;
				
			} else {
				Alert al = new Alert(Alert.AlertType.ERROR);
				al.setTitle("Greška");
				al.setHeaderText("Došlo je do greške prilikom ažuriranja!");
				al.initModality(Modality.APPLICATION_MODAL);
				al.showAndWait();
				return;
			}
		}
	}
	
	private void postaviVelicinu() {
		
		potvrdi.setPrefSize(230, 30);
		zatvori.setPrefSize(230, 30);
		//azz.setPrefSize(230,30);
		
		potvrdi.setStyle("-fx-background-color: #4a53ff; "
					   + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-color: blue;"
				       + "-fx-border-color: yellow;");
		
		zatvori.setStyle("-fx-cursor: hand;"
					   + "-fx-background-color: lightblue;"
					   + "-fx-border-style: solid;");
		
		/*azz.setStyle("-fx-background-color: #4a53ff; "
				   + "-fx-cursor: hand;"
			       + "-fx-border-style: solid;"
			       + "-fx-color: blue;"
			       + "-fx-border-color: yellow;");*/
		
		comboP.setStyle("-fx-cursor: hand;");
		comboPredOb.setStyle("-fx-cursor: hand;");
		datum.setStyle("-fx-cursor: hand;");
		
		lbl1.setStyle("-fx-text-fill: black;"
				    + "-fx-font-weight: bold;");
	
		lbl2.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");
	
		lbl3.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");
	
		lbl4.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;");
		
		lbl5.setStyle("-fx-text-fill: red;"
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
