package com.qdegrees.activity.ui.notifications;

public class NotificationsListItem {
   String UserId,Notificationid,Title,Message,Type,CreatedAt,UpdatedAt;
   int ReadStatus,Status;

   public NotificationsListItem(String userId,String notificationid,String title,String message,String type,String createdAt,String updatedAt,int readStatus, int status){
      this.UserId=userId;
      this.Notificationid=notificationid;
      this.Title=title;
      this.Message=message;
      this.Type=type;
      this.CreatedAt=createdAt;
      this.UpdatedAt=updatedAt;
      this.ReadStatus=readStatus;
      this.Status=status;
   }

   public String getUserId() {
      return UserId;
   }

   public String getNotificationid() {
      return Notificationid;
   }

   public String getTitle() {
      return Title;
   }

   public String getMessage() {
      return Message;
   }

   public String getType() {
      return Type;
   }

   public String getCreatedAt() {
      return CreatedAt;
   }

   public String getUpdatedAt() {
      return UpdatedAt;
   }

   public int getReadStatus() {
      return ReadStatus;
   }

   public int getStatus() {
      return Status;
   }
}
