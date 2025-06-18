

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.stage.FileChooser;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * @author Jm Molomo
 * 
 */

public class MyPane extends Pane {
    public TextField H_ID;
    public TextField P_ID;
    public TextField F_ID;
    public TextField T_ID;
    public TextArea M_ID;  // Changed to TextArea
    public TextField N_ID;

    public MyPane() {
        this.setPrefSize(600, 400);

        Pane topPane = new Pane();
        topPane.setPrefSize(600, 27);
        topPane.setStyle("-fx-background-color: lightblue;");

        Label titleLabel = new Label("MyEmail");
        titleLabel.setFont(Font.font("FontAwesome Regular", 22));
        titleLabel.setLayoutX(256);
        titleLabel.setLayoutY(-2);

        topPane.getChildren().add(titleLabel);

        Pane mainPane = new Pane();
        mainPane.setLayoutX(163);
        mainPane.setLayoutY(56);
        mainPane.setPrefSize(298, 288);
        mainPane.setStyle("-fx-background-radius: 15px; -fx-background-color: #ccc;");

        Label hostLabel = new Label("Host:");
        hostLabel.setLayoutX(22);
        hostLabel.setLayoutY(21);
        hostLabel.setPrefSize(42, 34);

        H_ID = new TextField();
        H_ID.setLayoutX(52);
        H_ID.setLayoutY(26);
        H_ID.setPrefSize(123, 25);

        Label portLabel = new Label("Port:");
        portLabel.setLayoutX(187);
        portLabel.setLayoutY(22);
        portLabel.setPrefSize(33, 34);

        P_ID = new TextField();
        P_ID.setLayoutX(220);
        P_ID.setLayoutY(27);
        P_ID.setPrefSize(65, 9);

        Label fromLabel = new Label("From:");
        fromLabel.setLayoutX(14);
        fromLabel.setLayoutY(57);
        fromLabel.setPrefSize(42, 34);

        F_ID = new TextField();
        F_ID.setLayoutX(52);
        F_ID.setLayoutY(61);
        F_ID.setPrefSize(123, 25);

        Label toLabel = new Label("To:");
        toLabel.setLayoutX(14);
        toLabel.setLayoutY(97);
        toLabel.setPrefSize(42, 34);

        T_ID = new TextField();
        T_ID.setLayoutX(52);
        T_ID.setLayoutY(102);
        T_ID.setPrefSize(123, 25);

        Label messageLabel = new Label("Message:");
        messageLabel.setLayoutX(14);
        messageLabel.setLayoutY(144);
        messageLabel.setPrefSize(58, 34);

        M_ID = new TextArea();  // Changed to TextArea
        M_ID.setLayoutX(11);
        M_ID.setLayoutY(178);
        M_ID.setPrefSize(260, 78);

        Button attachButton = new Button("Attach");
        attachButton.setLayoutX(11);
        attachButton.setLayoutY(262);
        attachButton.setPrefSize(65, 25);
        attachButton.setStyle("-fx-background-color: lightblue; -fx-background-radius: 14px;");
        attachButton.setOnAction(event -> AttOnbtn());

        mainPane.getChildren().addAll(hostLabel, H_ID, portLabel, P_ID, fromLabel, F_ID, toLabel, T_ID, messageLabel, M_ID, attachButton);

        Button sendButton = new Button("Send");
        sendButton.setLayoutX(381);
        sendButton.setLayoutY(353);
        sendButton.setPrefSize(80, 34);
        sendButton.setStyle("-fx-background-color: lightblue;");
        sendButton.setOnAction(event -> SendOnbtn());

        N_ID = new TextField();
        N_ID.setLayoutY(27);
        N_ID.setPrefSize(148, 43);
        N_ID.setStyle("-fx-background-color: white;");
        
        F_ID.setText("sender@csc2b.uj.ac.za");
        T_ID.setText("receipt@csc2b.uj.ac.za");

        this.getChildren().addAll(topPane, mainPane, sendButton, N_ID);
    }

    private void AttOnbtn() {
        FileChooser fc = new FileChooser();
        fc.setTitle("choose file");
        fc.setInitialDirectory(new File("data"));
        File file = fc.showOpenDialog(null);
        N_ID.setText("File Attached.");
    }

    private void SendOnbtn() {
        String Host = this.H_ID.getText();
        int Port = Integer.parseInt(this.P_ID.getText());
        String From = this.F_ID.getText();  // Corrected to F_ID
        String To = this.T_ID.getText();
        String Msg = this.M_ID.getText();  // Updated to getText from TextArea

        try (Socket ss = new Socket(Host, Port);
             PrintWriter writer = new PrintWriter(ss.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(ss.getInputStream(), "UTF-8"))) {

            System.out.println("Connected to server");

            // Read and print the server's initial response
            String response = reader.readLine();
            System.out.println("Conversation initiated status: " + response);

            // HELO / EHLO
            writer.println("HELO " + ss.getInetAddress().getHostName());
            response = reader.readLine();
            System.out.println("HELO response: " + response);

            // MAIL FROM
            writer.println("MAIL FROM:<" + From + ">");
            response = reader.readLine();
            System.out.println("MAIL FROM response: " + response);

            // RCPT TO
            writer.println("RCPT TO:<" + To + ">");
            response = reader.readLine();
            System.out.println("RCPT TO response: " + response);

            // DATA
            writer.println("DATA");
            response = reader.readLine();
            System.out.println("DATA response: " + response);

            // Sending Subject and Message
            writer.println("From: " + From);
            writer.println("To: " + To);
            writer.println("Date: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new Date()));
            writer.println("Subject: Test message");
            writer.println();
            writer.println(Msg);
            writer.println();
            writer.println(".");

            // This will read the response after sending the email content
            response = reader.readLine();
            System.out.println("Email sent status: " + response);

            // QUIT
            writer.println("QUIT");
            response = reader.readLine();
            System.out.println("Program termination status: " + response);

            N_ID.setText("Message Sent.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
