package logiciel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BibliothequeScans {

	// Database connection
	String url = "jdbc:mysql://localhost/projet?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
	String login = "root";
	String password = "";
	Connection conn = null;
	private Label label;
	int i = 0;
	String imglink = "";
	int total = 0;
	int total2 = 0;
	// Map for sql query
	Map<Integer, String> titre = new HashMap<Integer, String>();
	Map<Integer, String> lieux = new HashMap<Integer, String>();
	Map<Integer, Integer> nbPage = new HashMap<Integer, Integer>();
	Map<Integer, Integer> anneeEd = new HashMap<Integer, Integer>();
	Map<Integer, String> commentaire = new HashMap<Integer, String>();
	String imgf = "";

	@SuppressWarnings("unchecked")
	public GridPane pageBiblioScans() {

		GridPane root = new GridPane();


		// space between border and grid
		root.setPadding(new Insets(50));
		// space between each bloc of the grid, Horizontal and vertical
		root.setHgap(25);
		root.setVgap(15);

		// set visible the lign of the grid (remove at the end)
		root.setGridLinesVisible(false);

		// set colum's size of the grid
		ColumnConstraints colConstraint = new ColumnConstraints();

		// set size Width of the grid, which ajust with the size of the scene
		colConstraint.setPercentWidth(50);
		root.setMaxWidth(Double.MAX_VALUE);
		colConstraint.setHalignment(HPos.LEFT);

		RowConstraints rowConstraint = new RowConstraints();
		// set size Height of the grid, which ajust with the size of the scene
		rowConstraint.setPercentHeight(50);
	

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		// try catch for database connection
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, login, password);

		}

		catch (ClassNotFoundException e) {
			System.err.println("Erreur de chargement");
			e.printStackTrace();
		}

		catch (SQLException e) {
			System.err.println("Erreur de chargement");
			e.printStackTrace();
		}

		try {

			// count number of book query
			Statement stmt = conn.createStatement();
			String sql = "SELECT COUNT(idLivre) AS total FROM Livre";
			ResultSet rs = stmt.executeQuery(sql);
			Statement stmt2 = conn.createStatement();
			String sql4 = "SELECT COUNT(idPage) AS total2 FROM Page";
			ResultSet rs4 = stmt2.executeQuery(sql4);
			// location for root img
			int x = 0;
			int y = 0;
			// query result put in variable

			while (rs.next()) {
				total = rs.getInt("total");
			}
			while (rs4.next()) {
				total2 = rs4.getInt("total2");
			}

			// Creation Column and Row Constraints
			root.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint);
			root.getRowConstraints().addAll(rowConstraint, rowConstraint, rowConstraint, rowConstraint);

			// "for" loop which create all image button needed
			for (i = 1; i < total + 1; i++) {
				// SQL query which research book in terms of idLivre
				String sql2 = "SELECT titre, lieux, nbPage, anneeEd, commentaire, linkImg FROM livre WHERE idLivre = "
						+ i + "";
				// put result in a ResultSet
				ResultSet rs2 = stmt.executeQuery(sql2);
				while (rs2.next()) {
					titre.put(i, rs2.getString("titre"));
					lieux.put(i, rs2.getString("lieux"));
					nbPage.put(i, rs2.getInt("nbPage"));
					anneeEd.put(i, rs2.getInt("anneeEd"));
					commentaire.put(i, rs2.getString("commentaire"));
					imgf = rs2.getString("linkImg");
				}
				// properties for every image

				Label labelTitre = new Label(titre.get(i));
				Visuals.visualLabelsNameBook(labelTitre);
				Image image2 = new Image(imgf);
				ImageView im2 = new ImageView();
				im2.setFitHeight(180);
				im2.setFitWidth(150);
				im2.setImage(image2);
				
				// Use a button to show the pic that the user took
				Button afficheImage = new Button("", im2);
				root.add(afficheImage, x, y);
				root.add(labelTitre, x, y);
				GridPane.setHalignment(labelTitre, HPos.CENTER);
				GridPane.setHalignment(afficheImage, HPos.LEFT);

				// an if which give x and y location for every image like this : 0 0 / 1 0 / 2 0
				// / 0 1 / 1 1 / 1 2 / etc...
				if (i % 3 == 0) {
					y++;
					x = 0;
				} else {
					x++;
				}

				// dialog pop up creation
				Alert dialog = new Alert(AlertType.CONFIRMATION);
				dialog.setTitle(titre.get(i));
				dialog.setHeaderText("Liste des pages du livre : " + titre.get(i) + ".");

				// show pop up on click
				afficheImage.setOnMouseClicked((e) -> {
					//button initialisation for pop up
					ButtonType ok = new ButtonType("ok");
					ButtonType Supprimer = new ButtonType("Supprimer");
					ButtonType Annuler = new ButtonType("Annuler");
					
					HBox dialogPaneContent = new HBox();
					
					for (int z = 1; z < total2 + 1; z++) {

						try {
							String sql3 = "SELECT img FROM Page WHERE idPage = " + z + "";
							// put result in a ResultSet
							ResultSet rs3 = stmt.executeQuery(sql3);
							while (rs3.next()) {
								imglink = rs3.getString("img");

								Image image3 = new Image(imglink);
								ImageView im3 = new ImageView();
								im3.setFitHeight(100);
								im3.setFitWidth(100);
								im3.setImage(image3);
								Button imgPage = new Button("", im3);

								dialogPaneContent.getChildren().addAll(imgPage);

								

								imgPage.setOnMouseClicked((e1) -> {
									
									Tableau tableau = new Tableau();

									
									Scene sceneBiblio = new Scene(tableau.buildData(), 600, 300, Color.BEIGE);
									Stage stageBiblio = new Stage();
									stageBiblio.setTitle("Tableau des mesures");
									stageBiblio.setScene(sceneBiblio);
									stageBiblio.setMinWidth(600);
									stageBiblio.setMinHeight(900);
									stageBiblio.show();
									
								});

							}
						} catch (SQLException e1) {
							System.err.println("Erreur de chargement");
							e1.printStackTrace();
						}
					}

					// Set content for Dialog Pane
					dialog.getDialogPane().setContent(dialogPaneContent);

					// Remove default ButtonTypes
					dialog.getButtonTypes().clear();
					dialog.getButtonTypes().addAll(ok, Supprimer, Annuler);

					// option != null.
					Optional<ButtonType> option = dialog.showAndWait();

					if (option.get() == null) {
					} else if (option.get() == ok) {
					} else if (option.get() == Supprimer) {
						// String sql3 = "DELETE FROM `livre` WHERE `livre`.`idLivre` = 1";
						// ResultSet rs3 = stmt.executeQuery(sql3);
					} else if (option.get() == Annuler) {
					} else {
						this.label.setText("-");
					}
				});
			}

		} catch (SQLException e) {
			System.err.println("Erreur de chargement");
			e.printStackTrace();
		}

		return root;
	}
}
