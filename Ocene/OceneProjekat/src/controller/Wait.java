package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import slikeKlasa.Images;

public class Wait {
	
	private static Alert a;

	private static void set() {
		
		a = new Alert(Alert.AlertType.INFORMATION);
		
		a.setTitle("Obaveštenje");
		a.setHeaderText("Trenutak");
		a.setContentText("Trenutno dobavljamo podatke iz baze, molimo saèekajte");
		a.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
		
		((Stage) a.getDialogPane().getScene().getWindow()).getIcons().add(Images.getImagePozornica());
		
	}
	
	public static void showDialog() {
		
		if(a == null) 
			set();
		a.show();
	}
	
	public static void closeDialog() {
		
		if(a.isShowing())
			a.close();
	}

}
