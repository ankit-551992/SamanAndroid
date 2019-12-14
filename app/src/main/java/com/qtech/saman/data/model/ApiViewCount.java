package com.qtech.saman.data.model;

public class ApiViewCount {

    private AppView appView;

    private String Success;

    public AppView getAppView ()
    {
        return appView;
    }

    public void setAppView (AppView appView)
    {
        this.appView = appView;
    }

    public String getSuccess ()
    {
        return Success;
    }

    public void setSuccess (String Success)
    {
        this.Success = Success;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [appView = "+appView+", Success = "+Success+"]";
    }

    public class AppView
    {
        private String DeviceType;

        private String AppViewID;

        private String ViewCount;

        public String getDeviceType ()
        {
            return DeviceType;
        }

        public void setDeviceType (String DeviceType)
        {
            this.DeviceType = DeviceType;
        }

        public String getAppViewID ()
        {
            return AppViewID;
        }

        public void setAppViewID (String AppViewID)
        {
            this.AppViewID = AppViewID;
        }

        public String getViewCount ()
        {
            return ViewCount;
        }

        public void setViewCount (String ViewCount)
        {
            this.ViewCount = ViewCount;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [DeviceType = "+DeviceType+", AppViewID = "+AppViewID+", ViewCount = "+ViewCount+"]";
        }
    }

}
