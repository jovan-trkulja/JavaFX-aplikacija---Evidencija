package gui;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Ispit;
import model.Predispitnaobaveza;
import model.Predmet;
import slikeKlasa.Images;

public class StatistikaPredmeta extends Application {
	
	private Stage pozornica = new Stage();
	
	private ComboBox<Predmet> comboP = new ComboBox<>();
	
	private Button potvrdi = new Button("Prikaži", Images.getImageUnesi());
	private Button zatvori = new Button("Zatvori", Images.getImageZatvori());
	
	private TextArea podaci = new TextArea();
	
	private Label lbl1 = new Label("Izaberite predmet");
	
	private String oPredmetu = "";
	private String predispitne = "";
	private String ispit = "";
	
	@Override
	public void start(Stage stage) throws Exception {
		
		this.pozornica = stage;
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackgroundStatistika());
		
		postaviVelicinu();
		
		bp.setCenter(initGui());
		
		zatvori.setOnAction(this::zatvaranje);
		potvrdi.setOnAction(this::obrada);
		
		Scene s = new Scene(bp);
		
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.getIcons().add(Images.getImagePozornica());
		stage.setTitle("Statistièki podaci");
		stage.show();
	}
	
	private GridPane initGui() {
		
		GridPane gp = new GridPane();
		gp.setHgap(15); 	gp.setVgap(15);
		gp.setPadding(new Insets(25));
		gp.setAlignment(Pos.BASELINE_CENTER);
		
		getPredmets();
		comboP.getSelectionModel().select(0);
		
		gp.add(lbl1, 0, 0);
		gp.add(comboP, 0, 1);
		gp.add(potvrdi, 0, 2);
		gp.add(podaci, 0, 3);
		
		return gp;
	}
	
	private void getPredmets() {
		
		List<Predmet> pred = Controller.getSubjects();
		
		ObservableList<Predmet> predmeti = comboP.getItems();
		
		pred.forEach(e -> {
			predmeti.add(e);
		});
	
	}
	
	private void zatvaranje(ActionEvent event) {
		
		Alert a = new Alert(Alert.AlertType.CONFIRMATION);
		a.setTitle("Napuštate prozor");
		a.setHeaderText("Sigurno napuštate aplikaciju?");
		a.initModality(Modality.APPLICATION_MODAL);

		Optional<ButtonType> btn = a.showAndWait();
		
		if(btn.isPresent() && btn.get() == ButtonType.OK) {
			a.close();
			pozornica.close();
		} else {
			a.close();
			return;
		}
	}
	
	private void obrada(ActionEvent event) {
		
		Predmet p = comboP.getSelectionModel().getSelectedItem();
		
		oPredmetu = "Naziv predmeta -> " + p.getNazPred() + "\n" + 
					"Godina izuèavanja -> " + p.getGodina() + "\n" + 
					"Profesor / Asistent -> " + p.getNazProf() + "\n" + 
					"Obavezni / izborni -> " + p.getStatus() + "\n" + 
					"ESPB bodovi -> " + p.getBrEspb() + "\n" +
					"--------------------------------------";
		
		List<Predispitnaobaveza> lista = Controller.getColloquiums(p);
		
		if(lista == null || lista.size() == 0) {
			
			predispitne = "Za izabrani predmet ne postoje predispitne obaveze! \n" + 
			      	  	  "--------------------------------------";
			
		} else {	
			
			double bodovi = lista.stream()
					             .collect(Collectors.summingDouble(Predispitnaobaveza::getBrBodova));

			Predispitnaobaveza najbolja = lista.stream()
								   			   .max(Comparator.comparing(Predispitnaobaveza::getBrBodova))
								               .orElse(null);
			
			predispitne = "Ukupno predispitnih obaveza -> " + lista.size() + "\n" + 
						  "Ukupno bodova ostvarenih na predispitnim obavezama -> " + bodovi + "\n" + 
		                  "Najbolje uraðena predispitna obaveza -> " + najbolja.toString() + "\n" + 
		                  "--------------------------------------";
		}
			
		List<Ispit> isp = Controller.getExams(p);
		
		if(isp == null || isp.size() == 0) {
			
			ispit = "Za izabrani predmet ne postoje ispiti! \n" + 
			        "-------------------------------------- \n" +  
					"Predmet položen -> " + p.getPolozen() + "\n" + 
					"Ocena na predmetu -> " + "5" + "\n";
			
		} else {
			
			Ispit i = isp.stream()
					     .max(Comparator.comparing(Ispit::getOcena))
					     .orElse(null);
		
			
			ispit = "Predmet položen -> " + p.getPolozen() + "\n" + 
					"Predmet polagan ukupno -> " + isp.size() + ". puta" + "\n" + 
					"Ocena na predmetu -> " + i.getOcena() + "\n" + 
					"Ukupno bodova ostvareno na predmetu -> " + i.getUkupnoBodova();
			
		}
			
		podaci.setText(oPredmetu + "\n" + predispitne + "\n" + ispit); 
				      
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
		
		lbl1.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;"
			        + "-fx-font-size: 15;");

		podaci.setEditable(false);
		podaci.setStyle("-fx-background-color: lightblue;"
					  + "-fx-text-fill: blue;"
					  + "-fx-font-size: 13;");
		
	}

}
