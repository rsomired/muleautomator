/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tek.muleautomator.element;


public class FileElement {
    
    
    class FileWriteActivity { 
        private String activityType;
        private String fileName;

        public FileWriteActivity(String activityType, String fileName) {
            this.activityType = activityType;
            this.fileName = fileName;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
    }
    
    class FileCreateActivity {
        private String activityType;
        private String fileName;

        public FileCreateActivity(String activityType, String fileName) {
            this.activityType = activityType;
            this.fileName = fileName;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
    }
    
    class FileReadActivity {
        private String activityType;
        private String fileName;

        public FileReadActivity(String activityType, String fileName) {
            this.activityType = activityType;
            this.fileName = fileName;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
    }
}
