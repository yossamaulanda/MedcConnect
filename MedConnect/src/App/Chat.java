package App;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chat {
    private String sender;
    private String content;
    private String timestamp;
    private String messageId;
    
    public Chat(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = getCurrentTimestamp();
        this.messageId = generateMessageId();
    }
    
    public Chat(String sender, String content, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.messageId = generateMessageId();
    }
    
        public Chat(String sender, String content, String timestamp, String messageId) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.timestamp = getCurrentTimestamp();
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    public String getDisplayTime() {
        try {
            if (timestamp == null || timestamp.isEmpty()) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(timestamp);
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e2) {
                return timestamp != null ? timestamp : LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        }
    }
    
    public boolean isSystemMessage() {
        return "SYSTEM".equals(sender);
    }
    
    public boolean isFromPatient(String patientName) {
        return sender != null && sender.equals(patientName);
    }
    
    public boolean isFromDoctor(String doctorName) {
        return sender != null && sender.equals(doctorName);
    }
    
    public boolean isEmpty() {
        return content == null || content.trim().isEmpty();
    }
    
    public String getPreview(int maxLength) {
        if (content == null) return "";
        if (content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
    }
    
    public boolean containsKeyword(String keyword) {
        if (content == null || keyword == null) return false;
        return content.toLowerCase().contains(keyword.toLowerCase());
    }
    
    @Override
    public String toString() {
        return String.format("Chat{sender='%s', content='%s', timestamp='%s', messageId='%s'}", 
                           sender, 
                           content != null ? (content.length() > 50 ? content.substring(0, 50) + "..." : content) : "null", 
                           timestamp, 
                           messageId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;  
        Chat chat = (Chat) obj;
        if (messageId != null && chat.messageId != null) {
            return messageId.equals(chat.messageId);
        }
        
        if (sender != null ? !sender.equals(chat.sender) : chat.sender != null) return false;
        if (content != null ? !content.equals(chat.content) : chat.content != null) return false;
        return timestamp != null ? timestamp.equals(chat.timestamp) : chat.timestamp == null;
    }

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

    public Chat copy() {
        return new Chat(this.sender, this.content, this.timestamp, this.messageId);
    }
    
    public boolean isValid() {
        return sender != null && !sender.trim().isEmpty() && 
               content != null && !content.trim().isEmpty();
    }
    
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
            return true; 
        }
    }
    
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