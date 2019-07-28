package com.qtech.saman.ui.fragments.store.Tab;

import com.qtech.saman.data.model.apis.GetStores;
import com.qtech.saman.network.WebServicesHandler;

import retrofit2.Call;
import retrofit2.Response;

public class TabPresenter implements TabContractor.Presenter {

    private TabContractor.View view;

    public TabPresenter(TabContractor.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }


    @Override
    public void getStoresByCategory(int categoryID) {
        WebServicesHandler.instance.getStoresByCategory(String.valueOf(categoryID), new retrofit2.Callback<GetStores>() {
            @Override
            public void onResponse(Call<GetStores> call, Response<GetStores> response) {

                GetStores res = response.body();
                if (res != null) {
                    if (res.getSuccess() == 1) {

                        if (res.getStores() != null) {

                            if(view!=null){
                                view.response(res.getStores());
                            }

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<GetStores> call, Throwable t) {
            }
        });
    }

}
