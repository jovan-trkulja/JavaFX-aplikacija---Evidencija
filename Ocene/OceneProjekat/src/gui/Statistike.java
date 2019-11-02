package gui;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Ispit;
import model.Predispitnaobaveza;
import model.Predmet;
import slikeKlasa.Images;
import table.IspitT;
import table.PredObavT;

public class Statistike extends Application {
	
	private Stage pozornica = new Stage();
	
	private ComboBox<Predmet> comboP = new ComboBox<>();
	
	private RadioButton prikazKLK = new RadioButton("Prikaži predispitne obaveze");
	private RadioButton prikaziIspite = new RadioButton("Prikaži ispite");
	
	private Button potvrdi = new Button("Prikaži", Images.getImageUnesi());
	private Button zatvori = new Button("Zatvori", Images.getImageZatvori());
	private Button ponisti = new Button("Poništi", Images.getImageObrisi());
	
	private Label lbl1 = new Label("Izaberite predmet, a nakon toga izaberite akciju");
	private Label lbl2 = new Label("Trenutno ostvaren broj ESPB bodova -> ");
	
	private List<Predmet> pred = Controller.getSubjects();
	
	@Override
	public void start(Stage stage) throws Exception {
		
		postaviVelicinu();
		izracunaj();
		
		this.pozornica = stage;
		
		BorderPane bp = new BorderPane();
		bp.setCenter(initGui());
		
		bp.setBackground(Images.getBackgroundStatistika());
		
		ponisti.setOnAction(this::upali);
		
		prikazKLK.setOnAction(this::ugasi1);
		prikaziIspite.setOnAction(this::ugasi2);
		
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
		gp.setHgap(15);    gp.setVgap(15);
		gp.setPadding(new Insets(20));
		
		gp.add(lbl1, 0, 0);
		
		getPredmets();
		comboP.getSelectionModel().select(0);
		
		gp.add(comboP, 0, 1);
		
		gp.add(prikazKLK, 0, 2);
		gp.add(prikaziIspite, 0, 3);
		gp.add(lbl2, 0, 4);
		
		gp.add(new Label("|"), 1, 0);
		gp.add(new Label("|"), 1, 1);
		gp.add(new Label("|"), 1, 2);
		gp.add(new Label("|"), 1, 3);
		gp.add(new Label("|"), 1, 4);
		
		gp.add(potvrdi, 2, 0);
		gp.add(ponisti, 2, 2);
		gp.add(zatvori, 2, 4);
		
		return gp;
	}
	
	private void getPredmets() {
		
		ObservableList<Predmet> predmeti = comboP.getItems();
		
		pred.forEach(e -> {
			predmeti.add(e);
		});
	
	}
	
	private void izracunaj() {
		
		long broj = pred.stream()
						.filter(p -> p.getPolozen().equals("Da"))
				        .collect(Collectors.summarizingInt(Predmet::getBrEspb))
				        .getSum();
		
		String text = lbl2.getText() + broj;
		
		lbl2.setText(text);
		
	}
	
	private void upali(ActionEvent event) {
		
		prikazKLK.setDisable(false);
		prikaziIspite.setDisable(false);
		
		prikazKLK.setSelected(false);
		prikaziIspite.setSelected(false);
		
	}
	
	private void ugasi1(ActionEvent event) {
		
		prikaziIspite.setDisable(true);
		
	}
	
	private void ugasi2(ActionEvent event) {
	
		prikazKLK.setDisable(true);
		
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
	
	private void obrada(ActionEvent event) {
		
		if(provera()) {
			
			Predmet p = comboP.getSelectionModel().getSelectedItem();
			
			if(prikazKLK.isSelected()) {
				
				List<Predispitnaobaveza> lista = Controller.getColloquiums(p);
				
				if(lista.size() == 0) {
					
					Alert a = new Alert(Alert.AlertType.ERROR);
					a.setTitle("Greška");
					a.setHeaderText("Nije moguæe prikazati predispitne obaveze za izabrani predmet!");
					a.initModality(Modality.APPLICATION_MODAL);
					a.showAndWait();
					return;
					
				} else {
				
					napraviTabeluKLK(p,lista);
					return;
				}
				
			} else if(prikaziIspite.isSelected()) {
				
				List<Ispit> lista = Controller.getExams(p);
				
				if(lista.size() == 0) {
					
					Alert a = new Alert(Alert.AlertType.ERROR);
					a.setTitle("Greška");
					a.setHeaderText("Nije moguæe prikazati ispite za izabrani predmet!");
					a.initModality(Modality.APPLICATION_MODAL);
					a.showAndWait();
					return;
					
				} else {
				
					napraviTabeluIspit(p,lista);
					return;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void napraviTabeluKLK(Predmet predmet, List<Predispitnaobaveza> lista) {
		
		ObservableList<PredObavT> predObaveze = FXCollections.observableArrayList();
		
		lista.forEach(e -> {
			
			String date = "";
			String naz = e.getNazPredObav();
			double bodovi = e.getBrBodova();
			Date datum = e.getDatum();
			
			if(datum == null)
				date = "Nepoznato vreme održavanja";
			else
				date = datum.toString();
			
			PredObavT po = new PredObavT(naz,bodovi,date);
			
			predObaveze.add(po);
		});
		
		TableColumn<PredObavT, String> nazPr = new TableColumn<>("Naziv predispitne obaveze");
		nazPr.setMinWidth(350);
		nazPr.setStyle("-fx-alignment: CENTER;"
					 + "-fx-font-size: 16;"
					 + "-fx-text-fill: blue;"
					 + "-fx-font-weight: bold;");
		
		TableColumn<PredObavT, Number> bod = new TableColumn<>("Ostvaren broj bodova");
		bod.setMinWidth(250);
		bod.setStyle("-fx-alignment: CENTER;" 
				   + "-fx-font-size: 16;"
				   + "-fx-text-fill: green");
		
		TableColumn<PredObavT, String> dat = new TableColumn<>("Datum održavanja");
		dat.setMinWidth(230);
		dat.setStyle("-fx-alignment: CENTER;"
				   + "-fx-font-size: 16;"
				   + "-fx-text-fill: green;");
		
		lista.forEach(po -> { 
			
			nazPr.setCellValueFactory(p -> p.getValue().getNaziv());
			bod.setCellValueFactory(p -> p.getValue().getBodovi());
			dat.setCellValueFactory(p -> p.getValue().getDatum());
			
		});
		
		TableView<PredObavT> tabela = new TableView<>(predObaveze);
		tabela.getColumns().addAll(nazPr, bod, dat);
		
		String text = "Prikaz predispitnih obaveza za predmet -> " + predmet.getNazPred();
		
		Prikazi prikazi = new Prikazi();
		prikazi.setTabela(tabela);
		prikazi.setText(text);
		
		Stage bina = new Stage();
		try {
			prikazi.start(bina);
			return;
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void napraviTabeluIspit(Predmet predmet, List<Ispit> lista) {
		
		ObservableList<IspitT> ispiti = FXCollections.observableArrayList();
		
		lista.forEach(e -> {
			
			String date = "";
			double brBodova = e.getBrBodova();
			double ukupno = e.getUkupnoBodova();
			int ocena = e.getOcena();
			Date datum = e.getDatum();
			
			if(datum == null)
				date = "Nepoznato vreme održavanja";
			else
				date = datum.toString();
			
			IspitT i = new IspitT(date,brBodova,ukupno,ocena);
			
			ispiti.add(i);
		});
		
		TableColumn<IspitT, String> dat = new TableColumn<>("Datum polaganja predmeta");
		dat.setMinWidth(250);
		dat.setStyle("-fx-alignment: CENTER;"
					 + "-fx-font-size: 16;"
					 + "-fx-text-fill: blue;"
					 + "-fx-font-weight: bold;");
		
		TableColumn<IspitT, Number> bodU = new TableColumn<>("Ostvaren broj bodova na usmenom ispitu");
		bodU.setMinWidth(350);
		bodU.setStyle("-fx-alignment: CENTER;" 
				   + "-fx-font-size: 16;"
				   + "-fx-text-fill: green;"
				   + "-fx-font-weight: bold;");
		
		TableColumn<IspitT, Number> bodUkupno = new TableColumn<>("Ukupno ostvarenih bodova na predmetu");
		bodUkupno.setMinWidth(350);
		bodUkupno.setStyle("-fx-alignment: CENTER;"
				   + "-fx-font-size: 16;"
				   + "-fx-text-fill: green;"
				   + "-fx-font-weight: bold;");
		
		TableColumn<IspitT, Number> oc = new TableColumn<>("Zakljuèna ocena");
		oc.setMinWidth(220);
		oc.setStyle("-fx-alignment: CENTER;"
				   + "-fx-font-size: 16;"
				   + "-fx-text-fill: blue;"
				   + "-fx-font-weight: bold;");
		
		lista.forEach(po -> { 
			
			dat.setCellValueFactory(i -> i.getValue().getDatum());
			bodU.setCellValueFactory(i -> i.getValue().getBrBodova());
			bodUkupno.setCellValueFactory(i -> i.getValue().getUkupnoBodova());
			oc.setCellValueFactory(i -> i.getValue().getOcena());
			
		});
		
		TableView<IspitT> tabela = new TableView<>(ispiti);
		tabela.getColumns().addAll(dat, bodU, bodUkupno, oc);
		
		String text = "Prikaz ispita za predmet -> " + predmet.getNazPred();
		
		Prikazi prikazi = new Prikazi();
		prikazi.setTabela(tabela);
		prikazi.setText(text);
		
		Stage bina = new Stage();
		try {
			prikazi.start(bina);
			return;
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
	}
	
	private boolean provera() {
		
		if(prikazKLK.isSelected() || prikaziIspite.isSelected()) {
			return true;
		}
		
		return false;
	}

	private void postaviVelicinu() {
		
		potvrdi.setPrefSize(200, 30);
		zatvori.setPrefSize(200, 30);
		ponisti.setPrefSize(200, 30);
		
		potvrdi.setStyle("-fx-background-color: #4a53ff; "
					   + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-color: blue;"
				       + "-fx-border-color: yellow;");
		
		zatvori.setStyle("-fx-cursor: hand;"
					   + "-fx-background-color: lightblue;"
					   + "-fx-border-style: solid;");
		
		ponisti.setStyle("-fx-background-color: #bfbb34;"
				       + "-fx-cursor: hand;"
				       + "-fx-border-style: solid;"
				       + "-fx-text-fill: blue;");
		
		comboP.setStyle("-fx-cursor: hand;");
		
		prikazKLK.setStyle("-fx-cursor: hand;"
				         + "-fx-font-weight: bold;"
				         + "-fx-font-size: 15;");
		
		prikaziIspite.setStyle("-fx-cursor: hand;"
							 + "-fx-font-weight: bold;"
							 + "-fx-font-size: 15;");
		
		lbl1.setStyle("-fx-text-fill: black;"
			        + "-fx-font-weight: bold;"
			        + "-fx-font-size: 15;");
		
		lbl2.setStyle("-fx-text-fill: red;"
		        	+ "-fx-font-weight: bold;"
		        	+ "-fx-font-size: 16;");
		
	}

}
