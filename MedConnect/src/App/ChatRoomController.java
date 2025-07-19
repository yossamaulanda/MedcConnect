package App;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatRoomController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Label chatRoomTitle;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatBox;

    @FXML
    private TextField inputMessage;

    @FXML
    private Button sendButton;

    @FXML
    private Button clearChatButton;

    @FXML
    private Button janjiTemuButton;

    // Data properties
    private ArrayList<Chat> messages = new ArrayList<>();
    private final String FILE_PATH = "chat_room.xml";
    private String currentUser = "User";
    private boolean autoSaveEnabled = true;
    private boolean isRightAlign = true; // Alternating alignment

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing ChatRoomController...");
        
        Platform.runLater(() -> {
            try {
                setupUI();
                messages = loadMessages();
                refreshChat();
                setupEventHandlers();
                
                // Auto-scroll ke bawah saat ada pesan baru
                if (chatBox != null) {
                    chatBox.heightProperty().addListener((obs, oldVal, newVal) -> {
                        Platform.runLater(() -> {
                            if (chatScrollPane != null) {
                                chatScrollPane.setVvalue(1.0);
                            }
                        });
                    });
                }
                
                // Load welcome message jika belum ada chat
                if (messages.isEmpty()) {
                    addSystemMessage("Selamat datang di Chat Room! Mulai percakapan Anda.");
                }
                
                System.out.println("ChatRoomController initialized successfully.");
                
            } catch (Exception e) {
                System.err.println("Error during initialization: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void setupUI() {
        try {
            // Setup initial UI
            if (chatRoomTitle != null) {
                chatRoomTitle.setText("Chat Room");
            }
            
            if (statusLabel != null) {
                statusLabel.setText("● Online");
                statusLabel.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 10; -fx-font-size: 10px;");
            }
            
            if (inputMessage != null) {
                inputMessage.setPromptText("Ketik pesan Anda...");
            }
            
            System.out.println("UI setup completed successfully.");
            
        } catch (Exception e) {
            System.err.println("Error in setupUI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        try {
            // Setup key event handler untuk TextField
            if (inputMessage != null) {
                inputMessage.setOnKeyPressed(event -> {
                    if (event.getCode().toString().equals("ENTER")) {
                        handleSendMessage();
                    }
                });
            }
            
            System.out.println("Event handlers setup completed.");
            
        } catch (Exception e) {
            System.err.println("Error in setupEventHandlers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendMessage() {
        try {
            if (inputMessage == null) {
                System.err.println("inputMessage is null");
                return;
            }
            
            String text = inputMessage.getText();
            if (text != null) {
                text = text.trim();
            } else {
                text = "";
            }
            
            if (!text.isEmpty()) {
                Chat msg = new Chat(currentUser, text);
                messages.add(msg);
                
                // Alternating alignment - kanan kiri bergantian
                addMessageToUI(msg, isRightAlign);
                isRightAlign = !isRightAlign; // Toggle untuk pesan berikutnya
                
                if (autoSaveEnabled) {
                    saveMessages();
                }
                
                inputMessage.clear();
                
            } else {
                showAlert("Pesan Kosong", "Silakan ketik pesan terlebih dahulu.");
            }
        } catch (Exception e) {
            System.err.println("Error in handleSendMessage: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan saat mengirim pesan: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Keluar dari Chat Room");
            alert.setContentText("Yakin ingin keluar dari chat room?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (autoSaveEnabled) {
                    saveMessages();
                }
                navigateToKslPage();
            }
        } catch (Exception e) {
            System.err.println("Error in handleBack: " + e.getMessage());
            e.printStackTrace();
            navigateToKslPage(); // Tetap navigasi meski ada error
        }
    }

    private void navigateToKslPage() {
        try {
            // Load FXML file ksl.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanksl.fxml"));
            Parent root = loader.load();
            
            // Get current stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            
            // Set scene baru
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Opsional: Set title baru untuk window
            stage.setTitle("KSL - Halaman Utama");
            
            System.out.println("Successfully navigated to ksl.fxml");
            
        } catch (IOException e) {
            System.err.println("Error loading ksl.fxml: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Tidak dapat memuat halaman ksl.fxml: " + e.getMessage());
            
            // Fallback: tutup window jika tidak bisa load halaman ksl
            closeWindow();
        } catch (Exception e) {
            System.err.println("Unexpected error in navigateToKslPage: " + e.getMessage());
            e.printStackTrace();
            closeWindow();
        }
    }

    @FXML
    private void handleClearChat() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Chat");
            alert.setHeaderText("Konfirmasi Clear Chat");
            alert.setContentText("Yakin ingin menghapus semua pesan? Tindakan ini tidak bisa dibatalkan.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                messages.clear();
                refreshChat();
                addSystemMessage("Chat telah dibersihkan pada " + getCurrentTimestamp());
                isRightAlign = true; // Reset alignment
                if (autoSaveEnabled) {
                    saveMessages();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in handleClearChat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleJanjiTemu() {
        try {
            // Create custom dialog for appointment scheduling
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Buat Janji Temu");
            dialog.setHeaderText("Buat jadwal janji temu baru");

            // Set the button types
            ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the appointment form
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField titleField = new TextField();
            titleField.setPromptText("Judul janji temu");

            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Deskripsi (opsional)");
            descriptionArea.setMaxHeight(60);

            TextField dateField = new TextField();
            dateField.setPromptText("Tanggal (dd/MM/yyyy)");

            TextField timeField = new TextField();
            timeField.setPromptText("Waktu (HH:mm)");

            TextField locationField = new TextField();
            locationField.setPromptText("Lokasi");

            ComboBox<String> priorityBox = new ComboBox<>();
            priorityBox.getItems().addAll("Rendah", "Normal", "Tinggi", "Urgent");
            priorityBox.setValue("Normal");

            // Add labels and fields to grid
            grid.add(new Label("Judul:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Deskripsi:"), 0, 1);
            grid.add(descriptionArea, 1, 1);
            grid.add(new Label("Tanggal:"), 0, 2);
            grid.add(dateField, 1, 2);
            grid.add(new Label("Waktu:"), 0, 3);
            grid.add(timeField, 1, 3);
            grid.add(new Label("Lokasi:"), 0, 4);
            grid.add(locationField, 1, 4);
            grid.add(new Label("Prioritas:"), 0, 5);
            grid.add(priorityBox, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Enable/Disable save button depending on whether title is entered
            Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            titleField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            // Request focus on title field
            Platform.runLater(() -> titleField.requestFocus());

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == saveButtonType) {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                String date = dateField.getText().trim();
                String time = timeField.getText().trim();
                String location = locationField.getText().trim();
                String priority = priorityBox.getValue();

                // Create appointment message
                StringBuilder appointmentMsg = new StringBuilder();
                appointmentMsg.append("📅 JANJI TEMU DIBUAT\n");
                appointmentMsg.append("━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                appointmentMsg.append("📋 Judul: ").append(title).append("\n");
                
                if (!description.isEmpty()) {
                    appointmentMsg.append("📝 Deskripsi: ").append(description).append("\n");
                }
                if (!date.isEmpty()) {
                    appointmentMsg.append("📅 Tanggal: ").append(date).append("\n");
                }
                if (!time.isEmpty()) {
                    appointmentMsg.append("⏰ Waktu: ").append(time).append("\n");
                }
                if (!location.isEmpty()) {
                    appointmentMsg.append("📍 Lokasi: ").append(location).append("\n");
                }
                
                String priorityIcon = getPriorityIcon(priority);
                appointmentMsg.append("🎯 Prioritas: ").append(priorityIcon).append(" ").append(priority).append("\n");
                appointmentMsg.append("👤 Dibuat oleh: ").append(currentUser).append("\n");
                appointmentMsg.append("🕐 Dibuat pada: ").append(getCurrentTimestamp());

                // Add as system message
                addSystemMessage(appointmentMsg.toString());

                // Show success message
                showAlert("Berhasil", "Janji temu berhasil dibuat dan disimpan di chat!");
            }

        } catch (Exception e) {
            System.err.println("Error in handleJanjiTemu: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan saat membuat janji temu: " + e.getMessage());
        }
    }

    private String getPriorityIcon(String priority) {
        switch (priority) {
            case "Rendah":
                return "🟢";
            case "Normal":
                return "🟡";
            case "Tinggi":
                return "🟠";
            case "Urgent":
                return "🔴";
            default:
                return "🟡";
        }
    }

    private void addMessageToUI(Chat message, boolean alignRight) {
        try {
            if (chatBox == null || message == null) {
                System.err.println("chatBox or message is null");
                return;
            }
            
            VBox messageContainer = new VBox();
            messageContainer.setSpacing(5);
            
            Label messageLabel = new Label(message.getContent());
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(400);
            
            Label infoLabel = new Label(message.getSender() + " • " + message.getDisplayTime());
            infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888;");
            
            if (message.isSystemMessage()) {
                // System message - center aligned
                messageLabel.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #666666; -fx-padding: 8 12; -fx-background-radius: 15; -fx-font-size: 12px; -fx-font-style: italic;");
                messageContainer.setStyle("-fx-alignment: CENTER;");
                infoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #999999; -fx-alignment: CENTER;");
            } else if (alignRight) {
                // Right aligned message
                messageLabel.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 12 16; -fx-background-radius: 18; -fx-font-size: 14px;");
                messageContainer.setStyle("-fx-alignment: CENTER_RIGHT;");
                infoLabel.setStyle(infoLabel.getStyle() + " -fx-alignment: CENTER_RIGHT;");
            } else {
                // Left aligned message
                messageLabel.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #333333; -fx-padding: 12 16; -fx-background-radius: 18; -fx-font-size: 14px;");
                messageContainer.setStyle("-fx-alignment: CENTER_LEFT;");
                infoLabel.setStyle(infoLabel.getStyle() + " -fx-alignment: CENTER_LEFT;");
            }

            // Add context menu untuk edit/delete pesan
            if (!message.isSystemMessage()) {
                messageLabel.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        ContextMenu menu = new ContextMenu();

                        MenuItem editItem = new MenuItem("Edit Pesan");
                        editItem.setOnAction(e -> editMessage(message));

                        MenuItem deleteItem = new MenuItem("Hapus Pesan");
                        deleteItem.setOnAction(e -> deleteMessage(message));

                        menu.getItems().addAll(editItem, deleteItem);
                        menu.show(messageLabel, event.getScreenX(), event.getScreenY());
                    }
                });
            }

            messageContainer.getChildren().addAll(messageLabel, infoLabel);
            chatBox.getChildren().add(messageContainer);
            
            Platform.runLater(() -> {
                if (chatScrollPane != null) {
                    chatScrollPane.setVvalue(1.0);
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error in addMessageToUI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addSystemMessage(String content) {
        try {
            Chat systemMsg = new Chat("SYSTEM", content);
            messages.add(systemMsg);
            addMessageToUI(systemMsg, false); // System messages don't affect alignment
            
            if (autoSaveEnabled) {
                saveMessages();
            }
        } catch (Exception e) {
            System.err.println("Error in addSystemMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editMessage(Chat message) {
        try {
            TextInputDialog dialog = new TextInputDialog(message.getContent());
            dialog.setTitle("Edit Pesan");
            dialog.setHeaderText("Edit Pesan");
            dialog.setContentText("Pesan:");
            
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                message.setContent(result.get().trim());
                if (autoSaveEnabled) {
                    saveMessages();
                }
                refreshChat();
            }
        } catch (Exception e) {
            System.err.println("Error in editMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteMessage(Chat message) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Hapus Pesan");
            alert.setHeaderText("Konfirmasi Hapus Pesan");
            alert.setContentText("Yakin ingin menghapus pesan ini?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                messages.remove(message);
                if (autoSaveEnabled) {
                    saveMessages();
                }
                refreshChat();
            }
        } catch (Exception e) {
            System.err.println("Error in deleteMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshChat() {
        try {
            if (chatBox != null) {
                chatBox.getChildren().clear();
                boolean currentAlign = true; // Start with right alignment
                
                for (Chat msg : messages) {
                    if (!msg.isSystemMessage()) {
                        addMessageToUI(msg, currentAlign);
                        currentAlign = !currentAlign; // Alternate for next message
                    } else {
                        addMessageToUI(msg, false); // System messages don't affect alignment
                    }
                }
                
                // Update the current alignment state
                isRightAlign = currentAlign;
            }
        } catch (Exception e) {
            System.err.println("Error in refreshChat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void closeWindow() {
        try {
            if (backButton != null) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in closeWindow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        try {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            });
        } catch (Exception e) {
            System.err.println("Error in showAlert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // XML Save/Load Methods
    private void saveMessages() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("chatroom");
            doc.appendChild(root);

            Element metadata = doc.createElement("metadata");
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(currentUser));
            Element timestamp = doc.createElement("last_updated");
            timestamp.appendChild(doc.createTextNode(getCurrentTimestamp()));
            
            metadata.appendChild(user);
            metadata.appendChild(timestamp);
            root.appendChild(metadata);

            Element messagesRoot = doc.createElement("messages");
            for (Chat msg : messages) {
                Element messageElem = doc.createElement("message");

                Element sender = doc.createElement("sender");
                sender.appendChild(doc.createTextNode(msg.getSender()));
                messageElem.appendChild(sender);

                Element content = doc.createElement("content");
                content.appendChild(doc.createTextNode(msg.getContent()));
                messageElem.appendChild(content);

                Element messageTimestamp = doc.createElement("timestamp");
                messageTimestamp.appendChild(doc.createTextNode(msg.getTimestamp()));
                messageElem.appendChild(messageTimestamp);
                
                Element messageId = doc.createElement("messageId");
                messageId.appendChild(doc.createTextNode(msg.getMessageId()));
                messageElem.appendChild(messageId);

                messagesRoot.appendChild(messageElem);
            }
            root.appendChild(messagesRoot);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH));
            transformer.transform(source, result);

            System.out.println("Data chat berhasil disimpan ke " + FILE_PATH);

        } catch (Exception e) {
            System.err.println("Error saving chat data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ArrayList<Chat> loadMessages() {
        ArrayList<Chat> loadedMessages = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("File " + FILE_PATH + " tidak ditemukan. Membuat chat room baru.");
                return loadedMessages;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList metadataList = doc.getElementsByTagName("metadata");
            if (metadataList.getLength() > 0) {
                Element metadata = (Element) metadataList.item(0);
                
                NodeList userList = metadata.getElementsByTagName("user");
                if (userList.getLength() > 0) {
                    currentUser = userList.item(0).getTextContent();
                }
            }

            NodeList nodeList = doc.getElementsByTagName("message");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                
                String sender = elem.getElementsByTagName("sender").item(0).getTextContent();
                String content = elem.getElementsByTagName("content").item(0).getTextContent();
                
                String timestamp = null;
                NodeList timestampList = elem.getElementsByTagName("timestamp");
                if (timestampList.getLength() > 0) {
                    timestamp = timestampList.item(0).getTextContent();
                }
                
                String messageId = null;
                NodeList messageIdList = elem.getElementsByTagName("messageId");
                if (messageIdList.getLength() > 0) {
                    messageId = messageIdList.item(0).getTextContent();
                }
                
                Chat chat;
                if (timestamp != null && messageId != null) {
                    chat = new Chat(sender, content, timestamp, messageId);
                } else if (timestamp != null) {
                    chat = new Chat(sender, content, timestamp);
                } else {
                    chat = new Chat(sender, content);
                }
                
                loadedMessages.add(chat);
            }
            
            System.out.println("Berhasil memuat " + loadedMessages.size() + " pesan dari file XML.");
            
        } catch (Exception e) {
            System.err.println("Error loading chat data: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
        return loadedMessages;
    }

    // Public methods untuk external access
    public void setCurrentUser(String userName) {
        if (userName != null && !userName.trim().isEmpty()) {
            this.currentUser = userName;
        }
    }

    public void setAutoSave(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public int getMessageCount() {
        return messages.size();
    }

    public ArrayList<Chat> getMessages() {
        return new ArrayList<>(messages);
    }

    public void saveData() {
        saveMessages();
    }

    public void addMessage(String content) {
        Chat msg = new Chat(currentUser, content);
        messages.add(msg);
        addMessageToUI(msg, isRightAlign);
        isRightAlign = !isRightAlign;
        
        if (autoSaveEnabled) {
            saveMessages();
        }
    }
}