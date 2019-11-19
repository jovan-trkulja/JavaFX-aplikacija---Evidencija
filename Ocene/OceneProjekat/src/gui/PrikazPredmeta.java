package gui;

import java.util.List;

import controller.Controller;
import controller.Wait;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Predmet;
import slikeKlasa.Images;
import table.PredmetT;

public class PrikazPredmeta extends Application {
	
	private ObservableList<PredmetT> predmeti = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Wait.showDialog();
		ucitajPredmete();
		Wait.closeDialog();
		
		BorderPane bp = new BorderPane();
		bp.setBackground(Images.getBackground());
		
		bp.setCenter(initTabela());
		
		Scene s = new Scene(bp);
		stage.getIcons().add(Images.getImagePozornica());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(s);
		stage.setTitle("Prikaz predmeta");
		stage.show();
	}
	
	private void ucitajPredmete() {
		
		List<Predmet> lista = Controller.getSubjects();
		
		if(lista == null) {
			return;
		}
		
		lista.forEach(e -> {
			
			int id = e.getIdPredmet();
			int god = e.getGodina();
			int espb = e.getBrEspb();
			
			String naz = e.getNazPred();
			String prof = e.getNazProf();
			String stat = e.getStatus();
			String pol = e.getPolozen();
			
			PredmetT pt = new PredmetT(id,espb,god,naz,prof,pol,stat);
			
			predmeti.add(pt);
			
		});
		
	}

	@SuppressWarnings("unchecked")
	private TableView<PredmetT> initTabela() {
		
		TableColumn<PredmetT, Number> id = new TableColumn<>("ID predmeta");
		id.setCellValueFactory(e -> e.getValue().getIdPredmet());
		id.setMinWidth(50);
		id.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, Number> god = new TableColumn<>("Godina izuèavanja");
		god.setCellValueFactory(e -> e.getValue().getGodina());
		god.setMinWidth(150);
		god.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, String> naz = new TableColumn<>("Naziv predmeta");
		naz.setCellValueFactory(e -> e.getValue().getNazPred());
		naz.setMinWidth(300);
		naz.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, String> prof = new TableColumn<>("Profesor / Asistent");
		prof.setCellValueFactory(e -> e.getValue().getNazProf());
		prof.setMinWidth(300);
		prof.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, String> stat = new TableColumn<>("Status predmeta");
		stat.setCellValueFactory(e -> e.getValue().getStatus());
		stat.setMinWidth(200);
		stat.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, String> pol = new TableColumn<>("Predmet položen");
		pol.setCellValueFactory(e -> e.getValue().getPolozen());
		pol.setMinWidth(150);
		pol.setStyle(" -fx-alignment: CENTER;");
		
		TableColumn<PredmetT, Number> espb = new TableColumn<>("ESPB bodovi");
		espb.setCellValueFactory(e -> e.getValue().getBrEspb());
		espb.setMinWidth(150);
		espb.setStyle(" -fx-alignment: CENTER;");
		
		TableView<PredmetT> tabela = new TableView<>(predmeti);
		tabela.getColumns().addAll(id,god,naz,prof,stat,pol,espb);
		
		return tabela;
	}
	
}
