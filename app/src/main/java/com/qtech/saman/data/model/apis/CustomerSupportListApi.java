package com.qtech.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerSupportListApi implements Serializable {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClassPojo [result = " + result + ", success = " + success + ", message = " + message + "]";
    }

    public class Result implements Serializable {

        @SerializedName("Message")
        @Expose
        private String Message;

        @SerializedName("ID")
        @Expose
        private String ID;

        @SerializedName("SupportImageURLs")
        @Expose
        private ArrayList<String> SupportImageURLs;

        @SerializedName("Subject")
        @Expose
        private String Subject;

        @SerializedName("IsResolved")
        @Expose
        private String IsResolved;

        @SerializedName("Replay")
        @Expose
        private String Replay;

        public String getIsResolved() {
            return IsResolved;
        }

        public void setIsResolved(String isResolved) {
            IsResolved = isResolved;
        }

        public String getReplay() {
            return Replay;
        }

        public void setReplay(String replay) {
            Replay = replay;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public ArrayList<String> getSupportImageURLs() {
            return SupportImageURLs;
        }

        public void setSupportImageURLs(ArrayList<String> SupportImageURLs) {
            this.SupportImageURLs = SupportImageURLs;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String Subject) {
            this.Subject = Subject;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "Message='" + Message + '\'' +
                    ", ID='" + ID + '\'' +
                    ", SupportImageURLs=" + SupportImageURLs +
                    ", Subject='" + Subject + '\'' +
                    ", IsResolved='" + IsResolved + '\'' +
                    ", Replay='" + Replay + '\'' +
                    '}';
        }
    }

}
