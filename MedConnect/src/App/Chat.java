package App;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class untuk menyimpan data chat/pesan dalam aplikasi konsultasi
 * Menyimpan informasi pengirim, konten pesan, dan timestamp
 */
public class Chat {
    private String sender;
    private String content;
    private String timestamp;
    private String messageId;
    
    // Constructor dengan sender dan content
    public Chat(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = getCurrentTimestamp();
        this.messageId = generateMessageId();
    }
    
    // Constructor lengkap dengan timestamp
    public Chat(String sender, String content, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.messageId = generateMessageId();
    }
    
    // Constructor lengkap dengan semua parameter
    public Chat(String sender, String content, String timestamp, String messageId) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }
    
    // Getter untuk sender
    public String getSender() {
        return sender;
    }
    
    // Setter untuk sender
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    // Getter untuk content
    public String getContent() {
        return content;
    }
    
    // Setter untuk content
    public void setContent(String content) {
        this.content = content;
        // Update timestamp ketika content diubah
        this.timestamp = getCurrentTimestamp();
    }
    
    // Getter untuk timestamp
    public String getTimestamp() {
        return timestamp;
    }
    
    // Setter untuk timestamp
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    // Getter untuk message ID
    public String getMessageId() {
        return messageId;
    }
    
    // Setter untuk message ID
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    // Method helper untuk generate timestamp saat ini
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Method helper untuk generate unique message ID
    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    // Method untuk format timestamp ke format display (HH:mm)
    public String getDisplayTime() {
        try {
            if (timestamp == null || timestamp.isEmpty()) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            
            // Coba parse dengan format lengkap dulu
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            try {
                // Coba parse dengan format ISO
                LocalDateTime dateTime = LocalDateTime.parse(timestamp);
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e2) {
                // Jika gagal semua, return timestamp asli atau waktu sekarang
                return timestamp != null ? timestamp : LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        }
    }
    
    // Method untuk cek apakah pesan dari sistem
    public boolean isSystemMessage() {
        return "SYSTEM".equals(sender);
    }
    
    // Method untuk cek apakah pesan dari pasien tertentu
    public boolean isFromPatient(String patientName) {
        return sender != null && sender.equals(patientName);
    }
    
    // Method untuk cek apakah pesan dari dokter tertentu
    public boolean isFromDoctor(String doctorName) {
        return sender != null && sender.equals(doctorName);
    }
    
    // Method untuk cek apakah pesan kosong atau tidak valid
    public boolean isEmpty() {
        return content == null || content.trim().isEmpty();
    }
    
    // Method untuk mendapatkan preview singkat dari pesan
    public String getPreview(int maxLength) {
        if (content == null) return "";
        if (content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
    }
    
    // Method untuk cek apakah pesan mengandung kata kunci tertentu
    public boolean containsKeyword(String keyword) {
        if (content == null || keyword == null) return false;
        return content.toLowerCase().contains(keyword.toLowerCase());
    }
    
    // Override toString untuk debugging
    @Override
    public String toString() {
        return String.format("Chat{sender='%s', content='%s', timestamp='%s', messageId='%s'}", 
                           sender, 
                           content != null ? (content.length() > 50 ? content.substring(0, 50) + "..." : content) : "null", 
                           timestamp, 
                           messageId);
    }
    
    // Override equals untuk perbandingan object
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Chat chat = (Chat) obj;
        
        // Jika messageId ada, gunakan itu untuk perbandingan
        if (messageId != null && chat.messageId != null) {
            return messageId.equals(chat.messageId);
        }
        
        // Jika tidak, bandingkan berdasarkan content dan sender
        if (sender != null ? !sender.equals(chat.sender) : chat.sender != null) return false;
        if (content != null ? !content.equals(chat.content) : chat.content != null) return false;
        return timestamp != null ? timestamp.equals(chat.timestamp) : chat.timestamp == null;
    }
    
    // Override hashCode
    @Override
    public int hashCode() {
        if (messageId != null) {
            return messageId.hashCode();
        }
        
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
    
    // Method untuk membuat copy dari chat message
    public Chat copy() {
        return new Chat(this.sender, this.content, this.timestamp, this.messageId);
    }
    
    // Method untuk validasi data
    public boolean isValid() {
        return sender != null && !sender.trim().isEmpty() && 
               content != null && !content.trim().isEmpty();
    }
    
    // Method untuk format pesan untuk display
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        
        if (isSystemMessage()) {
            sb.append("[SISTEM] ");
        } else {
            sb.append(sender).append(": ");
        }
        
        sb.append(content);
        
        if (timestamp != null) {
            sb.append(" (").append(getDisplayTime()).append(")");
        }
        
        return sb.toString();
    }
    
    // Method untuk mengecek apakah pesan ini adalah pesan terbaru (dalam 1 menit terakhir)
    public boolean isRecent() {
        try {
            if (timestamp == null) return true;
            
            LocalDateTime messageTime;
            try {
                messageTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                messageTime = LocalDateTime.parse(timestamp);
            }
            
            LocalDateTime now = LocalDateTime.now();
            return messageTime.isAfter(now.minusMinutes(1));
        } catch (Exception e) {
            return true; // Jika error parsing, anggap sebagai pesan terbaru
        }
    }
    
    // Method untuk mendapatkan usia pesan dalam menit
    public long getAgeInMinutes() {
        try {
            if (timestamp == null) return 0;
            
            LocalDateTime messageTime;
            try {
                messageTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                messageTime = LocalDateTime.parse(timestamp);
            }
            
            LocalDateTime now = LocalDateTime.now();
            return java.time.Duration.between(messageTime, now).toMinutes();
        } catch (Exception e) {
            return 0;
        }
    }
}