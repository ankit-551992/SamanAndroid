package com.algorepublic.saman.data.model;

import com.algorepublic.saman.data.model.Product;
import com.algorepublic.saman.data.model.Slider;
import com.algorepublic.saman.data.model.Store;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeScreenData {

    @SerializedName("LatestProducts")
    @Expose
    private List<Product> latestProducts = null;
    @SerializedName("HotPicks")
    @Expose
    private List<Product> hotPicks = null;
    @SerializedName("Stores")
    @Expose
    private List<Store> stores = null;
    @SerializedName("BannerURL")
    @Expose
    private String bannerURL = null;
    @SerializedName("HeaderURLs")
    @Expose
    private List<String> headerURLs = null;
    @SerializedName("SliderBanners")
    @Expose
    private List<Slider> bannerSliderURLs = null;

    public List<Product> getLatestProducts() {
        return latestProducts;
    }

    public void setLatestProducts(List<Product> latestProducts) {
        this.latestProducts = latestProducts;
    }

    public List<Product> getHotPicks() {
        return hotPicks;
    }

    public void setHotPicks(List<Product> hotPicks) {
        this.hotPicks = hotPicks;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public void setBannerURLs(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public List<String> getHeaderURLs() {
        return headerURLs;
    }

    public void setHeaderURLs(List<String> headerURLs) {
        this.headerURLs = headerURLs;
    }


    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public List<Slider> getBannerSliderURLs() {
        return bannerSliderURLs;
    }

    public void setBannerSliderURLs(List<Slider> bannerSliderURLs) {
        this.bannerSliderURLs = bannerSliderURLs;
    }
}
