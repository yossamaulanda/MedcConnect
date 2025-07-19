package App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static ObservableList<NotificationItem> notifications = FXCollections.observableArrayList();
    private static List<NotificationItem> notificationList = new ArrayList<>(); 

    public static void addNotification(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        NotificationItem item = new NotificationItem(message, timestamp);
        notifications.add(item);
        notificationList.add(item); // Simpan juga di ArrayList
    }

    public static ObservableList<NotificationItem> getNotifications() {
        return notifications;
    }

    public static List<NotificationItem> getNotificationList() { // Tambahan: getter untuk ArrayList
        return notificationList;
    }

    public static void clearNotifications() {
        notifications.clear();
        notificationList.clear(); // Bersihkan juga ArrayList
    }

    public static class NotificationItem {
        private String message;
        private String timestamp;

        public NotificationItem(String message, String timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
