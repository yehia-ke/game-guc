package game.gui;

import java.io.File;
import java.io.IOException;
import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Main extends Application {
	Scene mainScene;
	Scene gameOverScene;
	Battle gameBattle;
	Pane[] lanetitans;
	Pane[] lanevalues;
	Pane[] laneweapons;
	FlowPane[] laneweaponsI;
	Stage primaryStage;
	boolean wasFullScreen;
	ImageView imageview;
	int prefPaneHeight;
	Pane panetest;
	int weapon;
	int vboxheight;
	Label ScoreL;
	Label TurnL;
	Label PhaseL;
	Label ResourcesL;
    public void start(Stage primaryStage) throws Exception {
    	primaryStage.setResizable(false);
    	Button startButton = createButton("",0,-100);
        Button instructionsButton = createButton("",0,0);
        Button settingsButton = createButton("",0,100);
        Button exitButton = createButton("Exit",0,0);
    	StackPane stackpane = new StackPane();
        Scene mainScene = new Scene(stackpane, 1920, 1080);
        primaryStage.setTitle("Attack on Titan utopia");
        primaryStage.setScene(mainScene);
        mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        ImageView BackgroundImage = new ImageView("file:assets/startmenu/background.png");
        BackgroundImage.fitWidthProperty().bind(mainScene.widthProperty()); 
        BackgroundImage.fitHeightProperty().bind(mainScene.heightProperty());
        MediaPlayer mediaplayer = BackgroundMusic("assets/music.mp3");
        stackpane.getChildren().add(BackgroundImage);
        stackpane.getChildren().addAll(startButton, instructionsButton, settingsButton,exitButton);
        StackPane.setAlignment(startButton, Pos.CENTER);
        StackPane.setAlignment(instructionsButton, Pos.CENTER);
        StackPane.setAlignment(settingsButton, Pos.CENTER);
        StackPane.setAlignment(exitButton, Pos.TOP_RIGHT);
        ImageView startbuttonView = new ImageView("file:assets/startmenu/Start.png");
        ImageView instructionsbuttonView = new ImageView("file:assets/startmenu/Instructions.png");
        ImageView settingsbuttonView = new ImageView("file:assets/startmenu/Settings.png");
        startButton.setGraphic(startbuttonView);
        startButton.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
        startButton.getStyleClass().add("hover-button");
        instructionsButton.setGraphic(instructionsbuttonView);
        instructionsButton.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
        instructionsButton.getStyleClass().add("hover-button");
        settingsButton.setGraphic(settingsbuttonView);
        settingsButton.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
        settingsButton.getStyleClass().add("hover-button");
        //-Setting popup
        Popup settingspopup = new Popup();
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mediaplayer.setVolume((double) newVal);
        });
        volumeSlider.setMajorTickUnit(0.1);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
        Button FullScreen = createButton("Fullscreen",0,0);
        VBox settingsmenu = new VBox(volumeSlider,FullScreen);
        settingsmenu.setAlignment(Pos.CENTER);
        settingsmenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 20;");
        settingsmenu.setSpacing(20);
        settingspopup.setAutoHide(true);
        settingspopup.getContent().add(settingsmenu);
        //-Start Button popup
        Popup startButtonPopup = new Popup();
        Button EasyButton = createButton("Easy",0,0);
        Button HardButton = createButton("Hard",0,0);
        HBox difficultyselect = new HBox(EasyButton,HardButton);
        difficultyselect.setAlignment(Pos.CENTER);
        difficultyselect.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 20;");
        difficultyselect.setSpacing(20);
        startButtonPopup.setAutoHide(true);
        startButtonPopup.getContent().add(difficultyselect);
       //- Button functions
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	Bounds StartPopupbuttonBounds = startButton.localToScreen(startButton.getBoundsInLocal());
            	startButtonPopup.setX(StartPopupbuttonBounds.getMaxX()-220);
            	startButtonPopup.setY(StartPopupbuttonBounds.getMinY());
            	if (!startButtonPopup.isShowing()) {
            		startButtonPopup.show(primaryStage);
                } else {
                	startButtonPopup.hide();
                }
            };
        });
        instructionsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showInstructionScene(primaryStage, mainScene);
            }
        });
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	Bounds settingsbuttonBounds = settingsButton.localToScreen(settingsButton.getBoundsInLocal());
            	settingspopup.setX(settingsbuttonBounds.getMaxX()-240);
                settingspopup.setY(settingsbuttonBounds.getMinY() + 100);
            	if (!settingspopup.isShowing()) {
                    settingspopup.show(primaryStage);
                } else {
                    settingspopup.hide();
                }
            };
        });
        FullScreen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	primaryStage.setFullScreen(true);
            }
        });
        HardButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	
            	startButtonPopup.hide();
            	int initialNumLanes = 5;
            	int initialResourceLane = 125;
            	showgame(primaryStage,initialNumLanes,initialResourceLane,primaryStage.isFullScreen(), mainScene);
            }
        });
        EasyButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	if(primaryStage.isFullScreen() == true)
            		primaryStage.setFullScreen(true);
            	startButtonPopup.hide();
            	int initialNumLanes = 3;
            	int initialResourceLane = 250;
            	showgame(primaryStage,initialNumLanes,initialResourceLane,primaryStage.isFullScreen(), mainScene);
            }
        });
        exitButton.setOnAction(event -> primaryStage.close());
        primaryStage.show();
    }
    public void showgame(Stage primaryStage, int initialNumLanes,int initialResourceLane,boolean wasFullScreen, Scene mainScene){
    	primaryStage.setResizable(false);
    	AnchorPane gamePane = new AnchorPane();
        Scene gameScene = new Scene(gamePane, 1920, 1080);
    	gameScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		try {
			gameBattle = new Battle(0,0,100,initialNumLanes,initialResourceLane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (initialNumLanes == 3) {
			prefPaneHeight = 250;
			vboxheight = 20;
		}
		else {
			prefPaneHeight = 150;
			vboxheight = 10;
		}
		VBox titanbox = new VBox();
		titanbox.setMinWidth(1732);
		titanbox.setMaxWidth(1732);
		titanbox.setPrefWidth(1732);
		titanbox.setSpacing(vboxheight);
		titanbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox lanevaluebox = new VBox();
		lanevaluebox.setMinWidth(90);
		lanevaluebox.setPrefWidth(90);
		lanevaluebox.setSpacing(vboxheight);
		lanevaluebox.setAlignment(Pos.BOTTOM_CENTER);
		VBox laneweaponbox = new VBox();
		laneweaponbox.setMinWidth(100);
		laneweaponbox.setMaxWidth(100);
		laneweaponbox.setPrefWidth(100);
		laneweaponbox.setSpacing(vboxheight);
		laneweaponbox.setAlignment(Pos.BOTTOM_CENTER);
		lanetitans = new Pane[initialNumLanes];
		lanevalues = new Pane[initialNumLanes];
		laneweapons = new Pane[initialNumLanes];
		laneweaponsI = new FlowPane[initialNumLanes];
		int Ylayout = 0;
		ImageView piercingcannon = new ImageView("file:assets/gamescene/weapons/piercingcannon.png");
		Tooltip piercing = new Tooltip("Name: Anti-Titan Shell\nType: Piercing Cannon\nPrice: 25\nDamage: 10");
		piercing.setShowDelay(Duration.seconds(0));
		Tooltip.install(piercingcannon, piercing);
		ImageView snipercannon = new ImageView("file:assets/gamescene/weapons/snipercannon.png");
		Tooltip sniper = new Tooltip("Name: Long Range Spear\nType: Sniper Cannon\nPrice: 25\nDamage: 35");
		sniper.setShowDelay(Duration.seconds(0));
		Tooltip.install(snipercannon, sniper);
		ImageView volleyspreadcannon = new ImageView("file:assets/gamescene/weapons/volleyspreadcannon.png");
		Tooltip volley = new Tooltip("Name: Wall Spread Cannon\nType: Volley Spread Cannon\nPrice: 100\nDamage: 5");
		volley.setShowDelay(Duration.seconds(0));
		Tooltip.install(volleyspreadcannon, volley);
		ImageView walltrap = new ImageView("file:assets/gamescene/weapons/walltrap.png");
		Tooltip wall = new Tooltip("Name: Proximity Trap\nType: Wall Trap\nPrice: 75\nDamage: 100");
		wall.setShowDelay(Duration.seconds(0));
		Tooltip.install(walltrap, wall);
		piercingcannon.setOnDragDetected(event -> {
		    Dragboard db = piercingcannon.startDragAndDrop(TransferMode.MOVE);
		    weapon = 1;
		    ClipboardContent content = new ClipboardContent();
		    content.putImage(piercingcannon.getImage());
		    db.setContent(content);
		    event.consume();
		});
		snipercannon.setOnDragDetected(event -> {
		    Dragboard db = snipercannon.startDragAndDrop(TransferMode.MOVE);
		    weapon = 2;
		    ClipboardContent content = new ClipboardContent();
		    content.putImage(snipercannon.getImage());
		    db.setContent(content);
		    event.consume();
		});
		volleyspreadcannon.setOnDragDetected(event -> {
		    Dragboard db = volleyspreadcannon.startDragAndDrop(TransferMode.MOVE);
		    weapon = 3;
		    ClipboardContent content = new ClipboardContent();
		    content.putImage(volleyspreadcannon.getImage());
		    db.setContent(content);
		    event.consume();
		});
		walltrap.setOnDragDetected(event -> {
		    Dragboard db = walltrap.startDragAndDrop(TransferMode.MOVE);
		    weapon = 4;
		    ClipboardContent content = new ClipboardContent();
		    content.putImage(walltrap.getImage());
		    db.setContent(content);
		    event.consume();
		});
		for (int i = 0; i < initialNumLanes;i++){
			lanetitans[i] = new Pane();
			lanetitans[i].setLayoutY(Ylayout+130);
			lanetitans[i].setLayoutX(-200);
			lanetitans[i].setMinWidth(1732);
			lanetitans[i].setMaxWidth(1732);
			lanetitans[i].setPrefWidth(1732);
			lanetitans[i].setPrefHeight(prefPaneHeight);
			ImageView Background = new ImageView("file:assets/gamescene/lane/Lane.png");
			Background.fitWidthProperty().bind(lanetitans[i].widthProperty());
			Background.fitHeightProperty().bind(lanetitans[i].heightProperty());
			lanetitans[i].getChildren().add(Background);
			titanbox.getChildren().add(lanetitans[i]);
			lanevalues[i] = new Pane();
			lanevalues[i].setPrefHeight(prefPaneHeight);
			lanevalues[i].setPrefWidth(90);
			lanevalues[i].setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 2px;");
			lanevaluebox.getChildren().add(lanevalues[i]);
			laneweapons[i] = new Pane();
			laneweapons[i].setMinHeight(prefPaneHeight);
			laneweapons[i].setMaxHeight(prefPaneHeight);
			laneweapons[i].setPrefHeight(prefPaneHeight);
			laneweapons[i].setPrefWidth(100);
			laneweapons[i].setStyle("-fx-background-color: green;");
			laneweaponsI[i] = new FlowPane();
			laneweaponsI[i].setMinHeight(prefPaneHeight);
			laneweaponsI[i].setMaxHeight(prefPaneHeight);
			laneweaponsI[i].setPrefHeight(prefPaneHeight);
			laneweaponsI[i].setPrefWidth(100);
			ImageView weaponwall = new ImageView("file:assets/gamescene/lane/weaponwall.png");
			weaponwall.fitWidthProperty().bind(laneweaponsI[i].widthProperty());
			weaponwall.fitHeightProperty().bind(laneweaponsI[i].heightProperty());
			laneweapons[i].getChildren().addAll(weaponwall,laneweaponsI[i]);
			laneweaponbox.getChildren().add(laneweapons[i]);
			int index = i;
			panetest = lanetitans[i];
			panetest.setOnDragOver(event -> {
			    if (event.getGestureSource() != panetest && event.getDragboard().hasImage()) {
			        event.acceptTransferModes(TransferMode.MOVE);
			    }
			    event.consume();
			});
			panetest.setOnDragDropped(event -> {
			    Dragboard db = event.getDragboard();
			    boolean success = false;
			    if (db.hasImage()) {
			        // Call your function here
			    	System.out.println(index);
			    	BuyWeaponOnDrag(weapon,index);
			        success = true;
			    }
			    event.setDropCompleted(success);
			    event.consume();
			});
			lanetitans[i] = panetest;
		}
		ScoreL = new Label("Score: "+"0");
		ScoreL.setFont(Font.font("Batsugun",30));
		TurnL = new Label("Turn: "+"0");
		TurnL.setFont(Font.font("Batsugun",30));
		HBox v1 = new HBox(ScoreL, TurnL);
		v1.setSpacing(15);
		PhaseL = new Label("Phase: "+"0");
		PhaseL.setFont(Font.font("Batsugun",30));
		ResourcesL = new Label("Resources: "+"0");
		ResourcesL.setFont(Font.font("Batsugun",30));
		HBox v2 = new HBox(PhaseL, ResourcesL);
		v2.setSpacing(15);
		VBox values = new VBox(v1,v2);
		values.setSpacing(35);
		values.setAlignment(Pos.TOP_CENTER);
    	HBox lanesBOX = new HBox(lanevaluebox,laneweaponbox,titanbox);
        StackPane center = new StackPane(values);
        StackPane.setAlignment(values,Pos.TOP_CENTER);
        primaryStage.setScene(gameScene);
        primaryStage.setFullScreen(wasFullScreen);
        Button perform = createButton("",0,100);
        ToggleButton AI = new ToggleButton("AI");
        AI.setFont(Font.font("Batsugun",30));
        AI.setOnAction(event -> {
            if (AI.isSelected()) {
            	ai(primaryStage, mainScene);
            } else {
                
            }
        });
        perform.setGraphic(new ImageView("file:assets/gamescene/performturn.png"));
        perform.setStyle("-fx-border-color: transparent;-fx-background-color: transparent;");
        perform.getStyleClass().add("hover-button");
        HBox weaponbox = new HBox(piercingcannon,snipercannon,volleyspreadcannon,walltrap);
        weaponbox.setStyle("-fx-background-image: url('file:assets/gamescene/weapons/download.png'); " +
                "-fx-background-size: cover;");
        weaponbox.setSpacing(10);
        weaponbox.setAlignment(Pos.TOP_CENTER);
        gamePane.getChildren().addAll(center,lanesBOX,weaponbox,perform,AI);
        perform.setAlignment(Pos.TOP_RIGHT);
        StackPane.setAlignment(values, Pos.TOP_CENTER);
        AnchorPane.setRightAnchor(AI, 0d);
        AnchorPane.setTopAnchor(AI,100d);
        AnchorPane.setRightAnchor(perform, 0d);
        AnchorPane.setTopAnchor(perform, -100.0);
        AnchorPane.setTopAnchor(center,0d);
        AnchorPane.setBottomAnchor(center,0d);
        AnchorPane.setLeftAnchor(center,450d);
        AnchorPane.setRightAnchor(center,0d);
        AnchorPane.setBottomAnchor(lanesBOX, 0d);
        AnchorPane.setLeftAnchor(lanesBOX, 0d);
        weaponbox.setAlignment(Pos.TOP_RIGHT);
        lanesBOX.setAlignment(Pos.BOTTOM_CENTER);
        updateValues();
        perform.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	if(!gameBattle.isGameOver()) {
            	updateValues();
            	gameBattle.passTurn();
            	updateLanes();
            	updateValues();
            	}
            	else {
            		ShowGameOver(primaryStage, mainScene);
            	}
            }
        }
        ); 
    }
    public void ai(Stage primaryStage, Scene mainScene) {
    	while(!gameBattle.isGameOver()) {
    		for(int i = 0;i < gameBattle.getOriginalLanes().size();i++) {
    			if(!gameBattle.getOriginalLanes().get(i).isLaneLost()){
    				if(gameBattle.getResourcesGathered()>=25) {
    					BuyWeaponOnDrag(2,i);
    					updateValues();
    					updateLanes();
    				}
    				else {
    					gameBattle.passTurn();
    					updateValues();
    					updateLanes();
    				}
    			}
    		}
    	}
    	ShowGameOver(primaryStage, mainScene);
    }
    public void showInstructionScene(Stage primaryStage, Scene mainScene) {
    	Button back = new Button("Back");
    	back.setFont(Font.font("Batsugun",30));
    	ImageView image = new ImageView("file:assets/startmenu/instructionsscene.png");
    	StackPane stackpane = new StackPane(image,back);
    	StackPane.setAlignment(back, Pos.TOP_LEFT);
    	Scene instructionscene = new Scene(stackpane, 1920,1080);
    	primaryStage.setScene(instructionscene);
    	back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	primaryStage.setScene(mainScene);
            }
        }
        ); 
    }
    public void ShowGameOver(Stage primaryStage, Scene mainScene) {
    	
    	Label finalscore = new Label("Score: "+gameBattle.getScore());
    	finalscore.setFont(Font.font("Minecraftia",25));
    	finalscore.setStyle("-fx-text-fill: WHITE;");
    	Button MainMenu = new Button("Main Menu");
    	MainMenu.setFont(Font.font("Batsugun",30));
    	MainMenu.setOnAction(event -> {
            // Switch to the game scene
            primaryStage.setScene(mainScene);
        });
    	VBox gameOver = new VBox(finalscore,MainMenu);
    	gameOver.setSpacing(500);
    	gameOver.setAlignment(Pos.CENTER);
        Scene gameOverScene = new Scene(gameOver, 1920, 1080);
        gameOver.setStyle("-fx-background-image: url('file:assets/GAME_OVER.png'); " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: 1920 1080; " +
                "-fx-background-position: center center;");
        primaryStage.setScene(gameOverScene);
    }
    public void BuyWeaponOnDrag(int weapon, int index) {
    	Lane lane = gameBattle.getOriginalLanes().get(index);
    	try {
    		gameBattle.purchaseWeapon(weapon, lane);
    		updateLanes();
    		updateValues();
    		addWeaponImageToLane(weapon,index);
    		System.out.println("Success");
    	}
    	catch(InsufficientResourcesException e){
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Insuffiect Resources");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();  
    	}
    	catch(InvalidLaneException e) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invalid Lane");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
    	}
    	if(gameBattle.isGameOver()) {
    		
    	}	
    }
    public void updateValues(){
    	for(int i = 0; i < gameBattle.getOriginalLanes().size();i++){
    		Lane lane = gameBattle.getOriginalLanes().get(i);
    			lanevalues[i].getChildren().clear();
    			Text text = new Text("DangerLevel: "+lane.getDangerLevel()+"\n"+"\n"+"Health: "+lane.getLaneWall().getCurrentHealth());
    			text.setY(20);
    			text.setX(3);
    			lanevalues[i].getChildren().add(text);	
    				
    		}
    	ScoreL.setText("Score:"+gameBattle.getScore()+"|");
		TurnL.setText("Turn:"+gameBattle.getNumberOfTurns());
		PhaseL.setText("Phase:"+gameBattle.getBattlePhase()+"|");
		ResourcesL.setText("Resources:"+gameBattle.getResourcesGathered());  	
    	}
    public void addWeaponImageToLane(int weapon,int index) {
    	ImageView imageView;
    	if(weapon == 1) {
    		imageView = new ImageView("file:assets/gamescene/weapons/piercingcannon.png");
    	}
    	else if(weapon == 2){
    		imageView = new ImageView("file:assets/gamescene/weapons/snipercannon.png");
    	}
    	else if(weapon == 3){
    		imageView =  new ImageView("file:assets/gamescene/weapons/volleyspreadcannon.png");
    	}
    	else{
    		imageView = new ImageView("file:assets/gamescene/weapons/walltrap.png");
    	}
    	imageView.setPreserveRatio(true);
    	imageView.setFitWidth(33);
    	imageView.setFitHeight(33);
    	laneweaponsI[index].getChildren().add(imageView);
    }
    public void updateLanes(){
    	double actualdistance = 1732/gameBattle.getTitanSpawnDistance();
    	for(int i = 0; i < gameBattle.getOriginalLanes().size();i++){
    		Lane lane = gameBattle.getOriginalLanes().get(i);
    		if(!lane.isLaneLost()){
    			lanetitans[i].getChildren().clear();
    			ImageView Background = new ImageView("file:assets/gamescene/lane/Lane.png");
    			Background.fitWidthProperty().bind(lanetitans[i].widthProperty());
    			Background.fitHeightProperty().bind(lanetitans[i].heightProperty());
    			lanetitans[i].getChildren().add(Background);
    			for (Titan t : lane.getTitans()){
    				String titan = "";
    				if(t instanceof PureTitan) {
    					imageview = new ImageView("file:assets/gamescene/titans/pure.png");
    					titan = "Pure Titan";
    				}
    				if(t instanceof ColossalTitan) {
    					imageview = new ImageView("file:assets/gamescene/titans/colossal.png");
    					titan = "Colossal Titan";
    				}
    				if(t instanceof ArmoredTitan) {
    					imageview = new ImageView("file:assets/gamescene/titans/armored.png");
    					titan = "Armored Titan";
    				}
    				if(t instanceof AbnormalTitan) {
    					imageview = new ImageView("file:assets/gamescene/titans/abnormal.png");
    					titan = "Abnormal Titan";
    				}
    				imageview.setFitWidth(50);
    				imageview.setFitHeight(50);
    				Tooltip tooltip = new Tooltip(titan+"\nHealth: "+t.getCurrentHealth()+"\nHeight: "+t.getHeightInMeters()+"\nDistance: "+t.getDistance()+"\nSpeed; "+t.getSpeed());
    		        tooltip.setShowDelay(Duration.seconds(0)); // Set the tooltip to show immediately
    		        Tooltip.install(imageview, tooltip);
    				Text text = new Text("Health: "+t.getCurrentHealth()+"\nHeight: "+t.getHeightInMeters()+"\nDistance: "+t.getDistance()+"\nSpeed; "+t.getSpeed()+t.getClass());
    				text.setFont(new Font(14));
    				text.setStyle("-fx-fill: Black;");
    				StackPane stackPane = new StackPane();
    				stackPane.setLayoutX(t.getDistance()*actualdistance);
    				stackPane.setLayoutY(Math.random()*(prefPaneHeight-20));
    				stackPane.getChildren().addAll(imageview);
    				lanetitans[i].getChildren().add(stackPane);
    			}
    				
    		}
    		if(lane.isLaneLost())
    			lanetitans[i].setStyle("-fx-opacity:0.3;");	
    	}
    }
    public Button createButton(String text, double x, double y) {
        Button button = new Button(text);
        button.setFont(new Font("Impact", 24));
        button.setTranslateX(x);
        button.setTranslateY(y);
        return button;
    }    
    public MediaPlayer BackgroundMusic(String Path){
    	Media song = new Media(new File(Path).toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(song);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0.5);
        return mediaPlayer;
    }
    public static void main(String[] args) {
        launch(args);
    }
}